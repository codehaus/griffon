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

package griffon.domain.db4o

import griffon.core.GriffonApplication
import griffon.domain.DomainClassEnhancerDelegate
import com.db4o.ObjectContainer

/**
 * @author Andres Almiray
 */
class Db4oDomainClassEnhancerDelegate implements DomainClassEnhancerDelegate {
    private final GriffonApplication app
    private final ObjectContainer db

    Db4oDomainClassEnhancerDelegate(GriffonApplication app, ObjectContainer db) {
        this.app = app
        this.db = db
    }

    Collection findAllBy(Class klass, String propertyName, value, String methodName) {
        []
    }

    Object findBy(Class klass, String propertyName, value, String methodName) {
        null
    }

    Collection findAllWhere(Map args) {
        []
    }

    Object findWhere(Map args) {
        null
    }

    Collection list() {
        []
    }

    int count() {
        0
    }

    int countBy(Class klass, String propertyName, value, String methodName) {
        0
    }

    Object saveOrUpdate(instance) {
        instance
    }

    Object delete(instance) {
        instance
    }
}
