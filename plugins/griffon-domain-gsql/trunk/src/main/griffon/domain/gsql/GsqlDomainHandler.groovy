/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package griffon.domain.gsql

import griffon.core.GriffonApplication
import griffon.domain.DomainHandler
import griffon.domain.gsql.metaclass.*

/**
 * @author Andres Almiray
 */
class GsqlDomainHandler implements DomainHandler {
    final GriffonApplication app
    final Map dynamicMethods

    GsqlDomainHandler(GriffonApplication app) {
        this.app = app

        dynamicMethods = [
            count: new CountPersistentMethod(this),
            make: new MakePersistentMethod(this),
            save: new SavePersistentMethod(this),
            delete: new DeletePersistentMethod(this),
            fetch: new FetchPersistentMethod(this),
            list: new ListPersistentMethod(this),
            find: new FindPersistentMethod(this),
            findAll: new FindAllPersistentMethod(this),
            findWhere: new FindPersistentMethod(this),
            findAllWhere: new FindAllPersistentMethod(this)
        ]
    }

    def invokeInstance(Object target, String methodName, Object... args) {
        dynamicMethods[methodName].invoke(target, methodName, args)
    }

    def invokeStatic(Class clazz, String methodName, Object... args) {
        dynamicMethods[methodName].invoke(clazz, methodName, args)
    }
}
