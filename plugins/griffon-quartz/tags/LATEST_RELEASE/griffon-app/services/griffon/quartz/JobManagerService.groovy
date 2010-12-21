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
package griffon.quartz

import org.quartz.Scheduler
import org.codehaus.griffon.runtime.quartz.JobObject

/**
 *
 */
class JobManagerService {
    Scheduler quartzScheduler

    def getAllJobs() {
        def jobsList = []
        def listJobGroups = quartzScheduler.getJobGroupNames()
        listJobGroups?.each {jobGroup ->
            quartzScheduler.getJobNames(jobGroup)?.each {jobName ->
                JobObject currentJob = new JobObject()
                currentJob.group = jobGroup
                currentJob.name = jobName
                def triggers = quartzScheduler.getTriggersOfJob(jobName, jobGroup)
                if (triggers != null && triggers.size() > 0) {
                    triggers.each {trigger ->
                        currentJob.triggerName = trigger.name
                        currentJob.triggerGroup = trigger.group
                        currentJob.status = quartzScheduler.getTriggerState(trigger.name, trigger.group)                        
                    }
                }
                jobsList.add(currentJob)
            }
        }

        jobsList
    }

    def getJob(String group) {
        if (group != null && !group.equals("")) {
            return quartzScheduler.getJobNames(group)
        } else {
            //TODO: Maybe we can create an exception for this kind of call
            return null
        }
    }

    def getRunningJobs() {
        quartzScheduler.getCurrentlyExecutingJobs()
    }

    def pauseJob(String group, String name) {
        quartzScheduler.pauseJob(name, group)
    }

    def resumeJob (String group, String name) {
        quartzScheduler.resumeJob (name, group)
    }

    def pauseTrigger(String group, String name) {
        quartzScheduler.pauseTrigger (name, group)
    }

    def resumeTrigger(String group, String name) {
        quartzScheduler.resumeTrigger (name, group)
    }

    def pauseTriggerGroup (String group) {
        quartzScheduler.pauseTriggerGroup (group)
    }

    def resumeTriggerGroup (String group) {
        quartzScheduler.resumeTriggerGroup (group)    
    }

    def pauseJobGroup (String group) {
        quartzScheduler.pauseJobGroup (group)
    }

    def resumeJobGroup (String group) {
        quartzScheduler.resumeJobGroup (group)    
    }

    def removeJob (String group, String name) {
        quartzScheduler.deleteJob(name, group)
    }

    def unscheduleJob (String group, String name) {
        quartzScheduler.unscheduleJob(name, group)
     }

    def interruptJob (String group, String name) {
        quartzScheduler.interrupt(name, group)
    }
}
