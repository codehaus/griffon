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
package griffon.domain;

import griffon.core.GriffonArtifact;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

/**
 * @author Andres Almiray
 */
public interface GriffonDomain extends GriffonArtifact {
    void onLoad();

    void onSave();

    void beforeLoad();

    void beforeInsert();

    void beforeUpdate();

    void beforeDelete();

    void afterLoad();

    void afterInsert();

    void afterUpdate();

    void afterDelete();

    public class Comparator implements java.util.Comparator<GriffonDomain> {
        public enum Order {ASC, DESC}

        private final String propertyName;
        private final Order order;

        public Comparator(String propertyName) {
            this(propertyName, Order.ASC);
        }

        public Comparator(String propertyName, Order order) {
            this.propertyName = propertyName;
            this.order = order;
        }

        public int compare(GriffonDomain domain1, GriffonDomain domain2) {
            Object value1 = propertyOf(domain1).getValue(domain1);
            Object value2 = propertyOf(domain2).getValue(domain2);
            int result = DefaultTypeTransformation.compareTo(value1, value2);
            return order == Order.ASC ? result : result * -1;
        }

        private GriffonDomainClassProperty propertyOf(GriffonDomain domain) {
            return ((GriffonDomainClass) domain.getGriffonClass()).getPropertyByName(propertyName);
        }
    }
}