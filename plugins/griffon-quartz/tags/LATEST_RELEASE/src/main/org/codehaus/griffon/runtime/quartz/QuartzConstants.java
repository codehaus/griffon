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
package org.codehaus.griffon.runtime.quartz;

import org.quartz.SimpleTrigger;

/**
 * @author Andres Almiray
 */
public interface QuartzConstants {
    long DEFAULT_REPEAT_INTERVAL = 60000l; // one minute
    long DEFAULT_TIMEOUT = DEFAULT_REPEAT_INTERVAL;
    long DEFAULT_START_DELAY = 0l; // no delay by default
    int DEFAULT_REPEAT_COUNT = SimpleTrigger.REPEAT_INDEFINITELY;
    String DEFAULT_CRON_EXPRESSION = "0 0 6 * * ?";
    String DEFAULT_GROUP = "GRIFFON_JOBS";
    boolean DEFAULT_CONCURRENT = true;
    boolean DEFAULT_SESSION_REQUIRED = true;
    String DEFAULT_TRIGGERS_GROUP = "GRIFFON_TRIGGERS";
    boolean DEFAULT_VOLATILITY = true;
    boolean DEFAULT_DURABILITY = true;
    boolean DEFAULT_REQUESTS_RECOVERY = false;

    String EXECUTE = "execute";
    String START_DELAY = "startDelay";
    String CRON_EXPRESSION = "cronExpression";
    String NAME = "name";
    String GROUP = "group";        
    String CONCURRENT = "concurrent";
    String SESSION_REQUIRED = "sessionRequired";
    String TIMEOUT = "timeout";
    String REPEAT_INTERVAL = "repeatInterval";
    String REPEAT_COUNT = "repeatCount";
    String VOLATILITY = "volatility";
    String DURABILITY = "durability";
    String REQUESTS_RECOVERY = "requestsRecovery";
}