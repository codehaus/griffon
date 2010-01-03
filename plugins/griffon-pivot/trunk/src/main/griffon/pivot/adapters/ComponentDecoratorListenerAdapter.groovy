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

package griffon.pivot.adapters
 
import griffon.pivot.impl.BuilderDelegate
import org.apache.pivot.wtk.Component
import org.apache.pivot.collections.Sequence
import org.apache.pivot.wtk.ComponentDecoratorListener
import org.apache.pivot.wtk.effects.Decorator

/**
 * @author Andres Almiray
 */
class ComponentDecoratorListenerAdapter extends BuilderDelegate implements ComponentDecoratorListener {
    private Closure onDecoratorInserted
    private Closure onDecoratorUpdated
    private Closure onDecoratorsRemoved
 
    ComponentDecoratorListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onDecoratorInserted(Closure callback) {
        onDecoratorInserted = callback
        onDecoratorInserted.delegate = this
    }

    void onDecoratorUpdated(Closure callback) {
        onDecoratorUpdated = callback
        onDecoratorUpdated.delegate = this
    }

    void onDecoratorsRemoved(Closure callback) {
        onDecoratorsRemoved = callback
        onDecoratorsRemoved.delegate = this
    }

    void decoratorInserted(Component arg0, int arg1) {
        if(onDecoratorInserted) onDecoratorInserted(arg0, arg1)
    }

    void decoratorUpdated(Component arg0, int arg1, Decorator arg2) {
        if(onDecoratorUpdated) onDecoratorUpdated(arg0, arg1, arg2)
    }

    void decoratorsRemoved(Component arg0, int arg1, Sequence arg2) {
        if(onDecoratorsRemoved) onDecoratorsRemoved(arg0, arg1, arg2)
    }
}