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

package org.codehaus.griffon.runtime.scaffolding;

import java.util.List;
import groovy.lang.Closure;
import griffon.plugins.scaffolding.Model;

/**
 * @author Andres Almiray
 */
public class CommandCompositeModel<T> extends AbstractCompositeModel<T> {
    private static final Object[] EMPTY_ARGS = new Object[0];
    private final Closure command;

    public CommandCompositeModel(List<Model<T>> models, Closure command) {
        super(models);
        this.command = command;
        command.setDelegate(this);
        command.setResolveStrategy(Closure.DELEGATE_FIRST);
    }

    protected T calculateValue() {
        return (T) command.call(EMPTY_ARGS);
    }
}
