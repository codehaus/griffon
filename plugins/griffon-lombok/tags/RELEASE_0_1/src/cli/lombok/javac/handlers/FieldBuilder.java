/*
 * Created on Nov 30, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package lombok.javac.handlers;

import static com.sun.tools.javac.util.List.nil;
import static lombok.core.util.Names.splitNameOf;
import static lombok.javac.handlers.JavacHandlerUtil.chainDots;

import lombok.javac.JavacNode;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.List;

/**
 * Simplifies creation of fields.
 *
 * @author Alex Ruiz
 */
class FieldBuilder {
    static FieldBuilder newField() {
        return new FieldBuilder();
    }

    private Class<?> type;
    private String name;
    private long modifiers;
    private List<JCExpression> args = nil();

    FieldBuilder ofType(Class<?> newType) {
        type = newType;
        return this;
    }

    FieldBuilder withName(String newName) {
        name = newName;
        return this;
    }

    FieldBuilder withModifiers(long newModifiers) {
        modifiers = newModifiers;
        return this;
    }

    FieldBuilder withArgs(JCExpression... newArgs) {
        args = List.from(newArgs);
        return this;
    }

    JCVariableDecl buildWith(JavacNode node) {
        TreeMaker treeMaker = node.getTreeMaker();
        JCExpression classType = chainDots(treeMaker, node, splitNameOf(type));
        JCExpression newVar = treeMaker.NewClass(null, null, classType, args, null);
        return treeMaker.VarDef(treeMaker.Modifiers(modifiers), node.toName(name), classType, newVar);
    }

    private FieldBuilder() {
    }
}
