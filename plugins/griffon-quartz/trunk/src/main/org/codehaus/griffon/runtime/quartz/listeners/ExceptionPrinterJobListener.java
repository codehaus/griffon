/*
 * Copyright 2006-2010 the original author or authors.
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
package org.codehaus.griffon.runtime.quartz.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;

/**
 * JobListener implementation which logs an exceptions occured during job's execution.
 *
 * @author Sergey Nebolsin (Grails 0.2)
 */
public class ExceptionPrinterJobListener extends JobListenerSupport {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionPrinterJobListener.class);

    public static final String NAME = "exceptionPrinterListener";

    public String getName() {
        return NAME;
    }

    public void jobWasExecuted(JobExecutionContext context, JobExecutionException exception) {
        if(exception != null) {
            LOG.error("Exception occured in job: " + context.getJobDetail().getFullName(), exception);
        }
    }
}
