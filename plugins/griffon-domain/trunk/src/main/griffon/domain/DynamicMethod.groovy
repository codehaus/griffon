/*
 * Copyright 2010 the original author or authors.
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

package griffon.domain

/**
 * @author Andres Almiray
 */
enum DynamicMethod {
    LIST, COUNT, COUNT_BY('countBy'),
    FIND, FIND_ALL('findAll'),
    FIND_BY('findBy'), FIND_WHERE('findWhere'),
    FIND_ALL_BY('findAllBy'), FIND_ALL_WHERE('findAllWhere'),
    EXECUTE_QUERY('executeQuery'),
    MAKE, SAVE, DELETE, FETCH;

    final String methodName

    DynamicMethod(String methodName = null) {
        this.methodName = methodName ?: name().toLowerCase()
    }

    String toString() {
        this.methodName
    }
}
