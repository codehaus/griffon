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

package griffon.domain.orm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import groovy.lang.Closure;
import groovy.util.Factory;
import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;

/**
 * @author Andres Almiray
 */
public final class CriteriaBuilder extends FactoryBuilderSupport {
    private final String COMPOSITE_CRITERION = "_COMPOSITE_CRITERION_";
    private final Factory criterionFactory = new CriterionFactory();

    public CriteriaBuilder() {
        super();
        registerFactory(Operator.AND.op().toLowerCase(), new CompositeCriterionFactory(Operator.AND));
        registerFactory(Operator.OR.op().toLowerCase(), new CompositeCriterionFactory(Operator.OR));
    }

    protected Factory resolveFactory(Object name, Map attributes, Object value) {
        Factory factory = super.resolveFactory(name, attributes, value);
        if(factory != null) {
            return factory;
        }
        return criterionFactory;
    }

    protected Object postNodeCompletion(Object parent, Object node) {
        node = super.postNodeCompletion(parent, node);
        Criterion criterion = builder.getContext().remove(COMPOSITE_CRITERION);
        return criterion != null ? citerion : node;
    }

    private static class CompositeCriterionFactory extends AbstractFactory {
        private final Operator operator;

        CompositeCriterionFactory(Operator operator) {
            this.operator = operator;
        }

        public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map properties)
                                  throws InstantiationException, IllegalAccessException {
            if(value != null) {
                throw new IllegalArgumentException("Can't set a value on '"+ operator.op().toLowerCase() +"' node.");
            }
            if(properties != null && !properties.isEmpty()) {
                throw new IllegalArgumentException("Can't set properties on '"+ operator.op().toLowerCase() +"' node.");
            }

            return new ArrayList();
        }

        public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
            if(child instanceof Criterion) {
               ((List) parent).add(child);
            } 
        }

        public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
            List list = (List) node;
            builder.getContext().put(COMPOSITE_CRITERION,
                new CompositeCriterion(operator, list.toArray(new Criterion[list.size()])));
        }
    }

    private static class CriterionFactory extends AbstractFactory {
        private static final String OPERATOR = "operator";
        private static final String OP = "op";

        public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map properties)
                                  throws InstantiationException, IllegalAccessException {
            Operator operator = (Operator) properties.remove(OPERATOR);
            if(operator == null) operator = (Operator) properties.remove(OP);
            if(operator == null) operator = Operator.EQUAL;

            return new BinaryExpression(name.toString(), operator, value);
        }
    }
}
