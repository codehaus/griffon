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

package org.codehaus.griffon.runtime.domain;

import org.codehaus.griffon.runtime.core.AbstractGriffonArtifact;
import griffon.domain.GriffonDomain;

/**
 * Base implementation of the GriffonDomain interface.
 *
 * @author Andres Almiray
 */
public abstract class AbstractGriffonDomain extends AbstractGriffonArtifact implements GriffonDomain {
    public void onLoad() {}
    public void onSave() {}
    public void beforeLoad() {}
    public void beforeInsert() {}
    public void beforeUpdate() {}
    public void beforeDelete() {}
    public void afterLoad() {}
    public void afterInsert() {}
    public void afterUpdate() {}
    public void afterDelete() {}
}