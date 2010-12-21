/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import griffon.plugins.quartz.GriffonJobClass
import org.codehaus.griffon.runtime.quartz.*
import org.codehaus.griffon.runtime.quartz.listeners.*
import org.springframework.beans.factory.config.MethodInvokingFactoryBean
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.context.ApplicationContext

import griffon.core.GriffonApplication
import griffon.util.Environment

import org.quartz.Scheduler
import org.quartz.CronTrigger
import org.quartz.Trigger
import org.quartz.SimpleTrigger
import org.quartz.JobDataMap

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Andres Almiray
 */
class QuartzGriffonAddon {
    private static final Logger log = LoggerFactory.getLogger('griffon.addon.quartz.QuartzGriffonAddon')

    void addonInit(GriffonApplication app) {
        app.artifactManager.registerArtifactHandler(new JobArtifactHandler(app))
    }

    def doWithSpring = {
        def config = loadQuartzConfig()

        app.artifactManager.jobClasses.each { jobClass ->
            configureJobBeans.delegate = delegate
            configureJobBeans(jobClass)
        }

        // register global ExceptionPrinterJobListener which will log exceptions occured
        // during job's execution
        "${ExceptionPrinterJobListener.NAME}"(ExceptionPrinterJobListener)

        quartzJobFactory(GriffonJobFactory)

        quartzScheduler(SchedulerFactoryBean) {
            if(config.containsKey( 'props')) {
                def configProps = new Properties()
                config.props.each { key, value -> configProps.setProperty( "org.quartz.$key", "$value") }
                quartzProperties = configProps
            } else {
                configLocation = "classpath:quartz.properties"
            }
            // delay scheduler startup to after-bootstrap stage
            autoStartup = false
            waitForJobsToCompleteOnShutdown = config.waitForJobsToCompleteOnShutdown
            jobFactory = quartzJobFactory
            globalJobListeners = [ref("${ExceptionPrinterJobListener.NAME}")]
        }
    }

    def whenSpringReady = { app ->
        def ctx = app.applicationContext

        def random = new Random()
        Scheduler quartzScheduler = ctx.getBean('quartzScheduler')
        app.artifactManager.jobClasses.each {GriffonJobClass jc ->
            def mc = jc.metaClass
            def jobName = jc.propertyName
            def jobGroup = jc.group

            def generateTriggerName = { ->
                long r = random.nextLong()
                if (r < 0) {
                    r = -r;
                }
                return "GRIFFON_" + Long.toString(r, 30 + (int) (System.currentTimeMillis() % 7));
            }

            mc.'static'.schedule = { String cronExpression, Map params = null ->
                Trigger trigger = new CronTrigger(generateTriggerName(), QuartzConstants.DEFAULT_TRIGGERS_GROUP, jobName, jobGroup, cronExpression)
                if(jc.getVolatility()) trigger.setVolatility(true)
                if(params) trigger.jobDataMap.putAll(params)
                quartzScheduler.scheduleJob(trigger)
            }
            mc.'static'.schedule = {Long interval, Integer repeatCount = SimpleTrigger.REPEAT_INDEFINITELY, Map params = null ->
                Trigger trigger = new SimpleTrigger(generateTriggerName(), QuartzConstants.DEFAULT_TRIGGERS_GROUP, jobName, jobGroup, new Date(), null, repeatCount, interval)
                if(jc.getVolatility()) trigger.setVolatility(true)
                if(params) trigger.jobDataMap.putAll(params)
                quartzScheduler.scheduleJob(trigger)
            }
            mc.'static'.schedule = {Date scheduleDate ->
                Trigger trigger = new SimpleTrigger(generateTriggerName(), QuartzConstants.DEFAULT_TRIGGERS_GROUP, jobName, jobGroup, scheduleDate, null, 0, 0)
                if(jc.getVolatility()) trigger.setVolatility(true)
                quartzScheduler.scheduleJob(trigger)
            }
            mc.'static'.schedule = {Date scheduleDate, Map params ->
                Trigger trigger = new SimpleTrigger(generateTriggerName(), QuartzConstants.DEFAULT_TRIGGERS_GROUP, jobName, jobGroup, scheduleDate, null, 0, 0)
                if(jc.getVolatility()) trigger.setVolatility(true)
                if(params) trigger.jobDataMap.putAll(params)
                quartzScheduler.scheduleJob(trigger)
            }
            mc.'static'.schedule = {Trigger trigger ->
                trigger.jobName = jobName
                trigger.jobGroup = jobGroup
                quartzScheduler.scheduleJob(trigger)
            }
            mc.'static'.triggerNow = { Map params = null ->
                if(jc.getVolatility()){
                    quartzScheduler.triggerJobWithVolatileTrigger(jobName, jobGroup, params ? new JobDataMap(params) : null)
                } else {
                    quartzScheduler.triggerJob(jobName, jobGroup, params ? new JobDataMap(params) : null)
                }
            }
            mc.'static'.removeJob = {
                quartzScheduler.deleteJob(jobName, jobGroup)
            }

            mc.'static'.reschedule = {Trigger trigger ->
                trigger.jobName = jobName
                trigger.jobGroup = jobGroup
                quartzScheduler.rescheduleJob(trigger.name, trigger.group, trigger)
            }

            mc.'static'.unschedule = {String triggerName, String triggerGroup = QuartzConstants.DEFAULT_TRIGGERS_GROUP ->
                quartzScheduler.unscheduleJob(triggerName, triggerGroup)
            }
        }

        app.artifactManager.jobClasses.each { jobClass ->
            scheduleJob.delegate = delegate
            scheduleJob(jobClass, ctx)
        }

        if(app.config?.quartz?.autoStartup) ctx.quartzScheduler.start()
    }

    def scheduleJob = {GriffonJobClass jobClass, ApplicationContext ctx ->
        def scheduler = ctx.getBean("quartzScheduler")
        if(scheduler) {
            def fullName = jobClass.propertyName
            // add job to scheduler, and associate triggers with it
            scheduler.addJob(ctx.getBean("${fullName}Detail"), true)
            jobClass.triggers.each {key, trigger ->
                log.debug("Scheduling $fullName with trigger $key: ${trigger}")
                if(scheduler.getTrigger(trigger.triggerAttributes.name, trigger.triggerAttributes.group)) {
                    scheduler.rescheduleJob(trigger.triggerAttributes.name, trigger.triggerAttributes.group, ctx.getBean("${key}Trigger"))
                } else {
                    scheduler.scheduleJob(ctx.getBean("${key}Trigger"))
                }
            }
            log.debug("Job ${jobClass.fullName} scheduled")
        } else {
            log.warn("Failed to register job triggers: scheduler not found")
        }
    }

    def configureJobBeans = { GriffonJobClass jobClass ->
        def fullName = jobClass.propertyName

        "${fullName}Detail"(JobDetailFactoryBean) {
            name = fullName
            group = jobClass.group
            concurrent = jobClass.concurrent
            volatility = jobClass.volatility
            durability = jobClass.durability
            requestsRecovery = jobClass.requestsRecovery
        }

        // registering triggers
        jobClass.triggers.each {name, trigger ->
            "${name}Trigger"(trigger.clazz) {
                jobDetail = ref("${fullName}Detail")
                trigger.properties.findAll {it.key != 'clazz'}.each {
                    delegate["${it.key}"] = it.value
                }
            }
        }
        log.trace("Configured job $fullName")
    }

    private ConfigObject loadQuartzConfig() {
        def config = app.config
        GroovyClassLoader classLoader = new GroovyClassLoader(getClass().classLoader)

        // merging default Quartz config into main application config
        config.merge(new ConfigSlurper(Environment.current.name).parse(classLoader.loadClass('DefaultQuartzConfig')))

        // merging user-defined Quartz config into main application config if provided
        try {
            config.merge(new ConfigSlurper(Environment.current.name).parse(classLoader.loadClass('QuartzConfig')))
        } catch (Exception ignored) {
            // ignore, just use the defaults
        }

        return config.quartz
    }
}
