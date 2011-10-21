/*
 * Copyright 2010-2011 the original author or authors.
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
package griffon.plugins.berkeleydb

import com.sleepycat.je.Environment
import griffon.util.CallableWithArgs

/**
 * @author Andres Almiray
 */
@Singleton
class EnvironmentHolder {
    Environment environment

    Object withBerkeleyEnv(Closure closure) {
        return closure(environment)
    }
    
    public <T> T withBerkeleyEnv(CallableWithArgs<T> callable) {
        callable.args = [environment] as Object[]
        return callable.run()
    }
}
