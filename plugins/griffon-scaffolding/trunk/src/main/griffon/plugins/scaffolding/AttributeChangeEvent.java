/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.plugins.scaffolding;

import java.beans.PropertyChangeEvent;

/**
 * @author Andres Almiray
 */
public class AttributeChangeEvent extends PropertyChangeEvent {
    private final BeanModel<?> beanModel;

    public AttributeChangeEvent(BeanModel<?> beanModel, Model<?> source, String attributeName, Object oldValue, Object newValue) {
        super(source, attributeName, oldValue, newValue);
        this.beanModel = beanModel;
    }

    public BeanModel<?> getBeanModel() {
        return beanModel;
    }
}
