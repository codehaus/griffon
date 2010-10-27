/*
 * Copyright 2009-2010 the original author or authors.
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

package griffon.plugin.easyb.test.listener

import org.easyb.listener.ListenerBuilder
import org.easyb.listener.ExecutionListener
import org.codehaus.griffon.test.event.GriffonTestEventPublisher
import org.codehaus.griffon.test.io.SystemOutAndErrSwapper
import org.easyb.util.BehaviorStepType
import griffon.plugin.easyb.test.GriffonEasybTestType
import griffon.plugin.easyb.test.report.EasybReportsFactory

class GriffonEasybListener implements ListenerBuilder {
    private GriffonEasybResultsCollector resultsCollector

    GriffonEasybListener(GriffonTestEventPublisher eventPublisher, EasybReportsFactory reportsFactory, SystemOutAndErrSwapper outAndErrSwapper, GriffonEasybTestType griffonEasybTestType) {
        this.resultsCollector = new GriffonEasybResultsCollector(eventPublisher, reportsFactory, outAndErrSwapper, griffonEasybTestType)
    }

    ExecutionListener get() {
        return resultsCollector
    }
}
