/*
 * Copyright 2008 the original author or authors.
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

package griffon.builder.fx.factory

import javafx.scene.Group
import javafx.scene.Node
import com.sun.javafx.runtime.location.*
import com.sun.javafx.runtime.sequence.*
import com.sun.javafx.runtime.TypeInfo

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class FxGroupFactory extends FxBeanFactory {
    FxGroupFactory() {
        super( Group, false )
    }

    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        if(!builder.parentContext.children) builder.parentContext.children = []
        builder.parentContext.children << child
    }

    public void onNodeCompleted( FactoryBuilderSupport builder, Object parent, Object node ) {
        if( builder.context.children ) {
            node.location("content").setAsSequence(Sequences.fromCollection(TypeInfo.Object,builder.context.children))
        }

        super.onNodeCompleted( builder, parent, node )
        if(builder.parentFactory) builder.parentFactory.setChild(builder,parent,node)
    }
}