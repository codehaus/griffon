/*
 * Copyright 2009 the original author or authors.
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

package griffon.swt

import griffon.util.UIThreadHandler
import org.eclipse.swt.widgets.Display

/**
 * Executes code using SWT's Display.
 *
 * @author Andres Almiray
 */
class SWTUIThreadHandler implements UIThreadHandler {
    boolean isUIThread() {
        Display.default == Display.findDisplay(Thread.currentThread())
    }

    void executeAsync(Runnable runnable) {
        Display.default.asyncExec(runnable)
    }

    void executeSync(Runnable runnable) {
        if(isUIThread()) {
            runnable.run()
        } else {
            Display.default.syncExec(runnable)
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
