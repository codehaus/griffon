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

package griffon.pivot

import griffon.util.UIThreadHandler
import java.awt.EventQueue

/**
 * Executes code using AWT's EventQueue.
 *
 * @author Andres Almiray
 */
class PivotUIThreadHandler implements UIThreadHandler {
    boolean isUIThread() {
        EventQueue.isDispatchThread()
    }

    void executeAsync(Runnable runnable) {
        EventQueue.invokeLater(runnable)
    }

    void executeSync(Runnable runnable) {
        if(isUIThread()) {
            runnable.run()
        } else {
            EventQueue.invokeAndWait(runnable)
        }
    }

    void executeOutside(Runnable runnable) {
        if(!isUIThread()) {
            runnable.run()
        } else {
            // TODO use a ThreadPool
            Thread.start(runnable)
        }
    }
}
