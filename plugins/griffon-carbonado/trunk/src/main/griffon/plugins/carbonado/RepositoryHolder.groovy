/*
 * Copyright 2011 the original author or authors.
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

package griffon.plugins.carbonado

import com.amazon.carbonado.Repository
import griffon.util.CallableWithArgs

/**
 * @author Andres Almiray
 */
@Singleton
class RepositoryHolder {
    Repository repository

    Object withCarbonado(Closure closure) {
        return closure(repository)
    }

    public <T> T withCarbonado(CallableWithArgs<T> callable) {
        callable.args = [repository] as Object[]
        return callable.run()
    }
}
