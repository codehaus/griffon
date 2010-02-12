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

package griffon.domain.db4o.query

import com.db4o.query.Predicate

/**
 * @author Andres Almiray
 */
class PropertiesPredicate<O> extends Predicate<O> {
    private final Map props = [:]

    PropertiesPredicate(Map props) {
       if(props) this.props.putall(props)
    }

    public boolean match(O o) {
        MetaClass mc = o.metaClass
        props.each { propertyName, propertyValue ->
            if(!mc.hasProperty(propertyName, o)) return false
            if(o[propertyName] != propertyValue) return false
        }
        return true
    }
}
