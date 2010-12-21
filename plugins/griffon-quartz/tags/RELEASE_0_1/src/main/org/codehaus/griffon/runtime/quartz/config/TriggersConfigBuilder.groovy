/* 
 * Copyright 2004-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.griffon.runtime.quartz.config

import org.codehaus.griffon.runtime.quartz.QuartzConstants
import org.codehaus.griffon.runtime.quartz.CustomTriggerFactoryBean
import org.quartz.Trigger
import org.quartz.CronTrigger
import org.quartz.SimpleTrigger
import org.quartz.CronExpression
import griffon.util.GriffonClassUtils
import griffon.exceptions.GriffonException

/**
 * Groovy Builder for parsing triggers configuration info.
 *
 * @author Sergey Nebolsin (Grails 0.3)
 * @author Andres Almiray
 */
public class TriggersConfigBuilder extends BuilderSupport implements QuartzConstants {    
    private triggerNumber = 0
    private jobName

    Map triggers = [:]

    public TriggersConfigBuilder(String jobName) {
        super()
        this.jobName = jobName
    }

    public build(closure) {
        closure.delegate = this
        closure.call()
        return triggers
    }

    protected void setParent(parent, child) {}

    protected createNode(name) {
        createNode(name, null, null)
    }

    protected createNode(name, value) {
        createNode(name, null, value)
    }

    protected createNode(name, Map attributes) {
        createNode(name, attributes, null)
    }

    protected Object createNode(name, Map attributes, Object value) {
        def trigger = createTrigger(name, attributes, value)
        triggers[trigger.triggerAttributes.name] = trigger
        trigger
    }

    public Expando createTrigger(name, Map attributes, value) {
        def triggerClass
        def triggerAttributes = new HashMap(attributes)

        prepareCommonTriggerAttributes(triggerAttributes)

        def triggerType = name

        switch(triggerType) {
            case 'simple':
                triggerClass = SimpleTrigger
                prepareSimpleTriggerAttributes(triggerAttributes)
                break
            case 'cron':
                triggerClass = CronTrigger
                prepareCronTriggerAttributes(triggerAttributes)
                break
            case 'custom':
                if(!triggerAttributes?.triggerClass) throw new GriffonException("Custom trigger must have 'triggerClass' attribute")
                triggerClass = triggerAttributes.remove('triggerClass')
                if(!Trigger.isAssignableFrom(triggerClass)) throw new GriffonException("Custom trigger class must extend org.quartz.Trigger class.")
                break
            default:
                throw new Exception("Invalid format")
        }

        new Expando(clazz: CustomTriggerFactoryBean, triggerClass: triggerClass, triggerAttributes: triggerAttributes)
    }

    private prepareCommonTriggerAttributes(HashMap triggerAttributes) {
        if(triggerAttributes[NAME] == null) triggerAttributes[NAME] = "${jobName}${triggerNumber++}"
        if(triggerAttributes[GROUP] == null) triggerAttributes[GROUP] = DEFAULT_GROUP
        if(triggerAttributes[START_DELAY] == null) triggerAttributes[START_DELAY] = DEFAULT_START_DELAY
        if(!(triggerAttributes[START_DELAY] instanceof Integer || triggerAttributes[START_DELAY] instanceof Long)) {
            throw new IllegalArgumentException("startDelay trigger property in the job class ${jobName} class must be Integer or Long");
        }
        if(((Number) triggerAttributes[START_DELAY]).longValue() < 0) {
            throw new IllegalArgumentException("startDelay trigger property in the job class ${jobName} is negative (possibly integer overflow error)");
        }
        if(triggerAttributes[VOLATILITY] == null) triggerAttributes[VOLATILITY] = DEFAULT_VOLATILITY
    }

    private def prepareSimpleTriggerAttributes(HashMap triggerAttributes) {
        if (triggerAttributes[REPEAT_INTERVAL] == null) triggerAttributes[REPEAT_INTERVAL] = DEFAULT_REPEAT_INTERVAL
        if (!(triggerAttributes[REPEAT_INTERVAL] instanceof Integer || triggerAttributes[REPEAT_INTERVAL] instanceof Long)) {
            throw new Exception("repeatInterval trigger property in the job class ${jobName} class must be Integer or Long");
        }
        if (((Number) triggerAttributes[REPEAT_INTERVAL]).longValue() < 0) {
            throw new Exception("repeatInterval trigger property for job class ${jobName} is negative (possibly integer overflow error)");
        }
        if (triggerAttributes[REPEAT_COUNT] == null) triggerAttributes[REPEAT_COUNT] = DEFAULT_REPEAT_COUNT
        if (!(triggerAttributes[REPEAT_COUNT] instanceof Integer || triggerAttributes[REPEAT_COUNT] instanceof Long)) {
            throw new Exception("repeatCount trigger property in the job class ${jobName} class must be Integer or Long");
        }
        if (((Number) triggerAttributes[REPEAT_COUNT]).longValue() < 0 && ((Number) triggerAttributes[REPEAT_COUNT]).longValue() != SimpleTrigger.REPEAT_INDEFINITELY) {
            throw new Exception("repeatCount trigger property for job class ${jobName} is negative (possibly integer overflow error)");
        }
    }

    private def prepareCronTriggerAttributes(HashMap triggerAttributes) {
        if (!triggerAttributes?.cronExpression) throw new GriffonException("Cron trigger must have 'cronExpression' attribute")
        if (!CronExpression.isValidExpression(triggerAttributes[CRON_EXPRESSION].toString())) {
            throw new GriffonException("Cron expression '${triggerAttributes[CRON_EXPRESSION]}' in the job class ${jobName} is not a valid cron expression");
        }
    }

    public Map createEmbeddedSimpleTrigger(long startDelay, long timeout, int repeatCount) {
        return [(jobName):createTrigger('simple', [name: jobName, startDelay:startDelay, repeatInterval:timeout, repeatCount:repeatCount], null)]
    }

    public Map createEmbeddedCronTrigger(long startDelay, String cronExpression) {
        return [(jobName):createTrigger('cron', [name: jobName, startDelay:startDelay, cronExpression:cronExpression], null)] 
    }
}
