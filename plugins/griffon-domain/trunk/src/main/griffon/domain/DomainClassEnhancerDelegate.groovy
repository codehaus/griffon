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
interface DomainClassEnhancerDelegate {
    Collection findAllBy(Class klass, String propertyName, value, String methodName)
    Object findBy(Class klass, String propertyName, value, String methodName)

    Collection findAllWhere(Map args)
    Object findWhere(Map args)

    Collection list()
    int count()
    int countBy(Class klass, String propertyName, value, String methodName)

    Object saveOrUpdate(Object instance)
    Object delete(Object instance)
}
