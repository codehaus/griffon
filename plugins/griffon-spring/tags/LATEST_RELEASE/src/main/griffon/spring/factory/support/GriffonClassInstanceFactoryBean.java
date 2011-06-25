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

package griffon.spring.factory.support;

import griffon.core.GriffonClass;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author Andres Almiray
 */
public class GriffonClassInstanceFactoryBean implements FactoryBean {
    private GriffonClass griffonClass;
    private boolean singleton = true;

    public void setGriffonClass(GriffonClass griffonClass) {
        this.griffonClass = griffonClass;
    }

    public Object getObject() throws Exception {
        return griffonClass != null ? griffonClass.newInstance() : null;
    }

    public Class getObjectType() {
        return griffonClass != null ? griffonClass.getClazz() : null;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }
}
