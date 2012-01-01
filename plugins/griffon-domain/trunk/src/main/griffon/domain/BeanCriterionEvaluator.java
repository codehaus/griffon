/*
 * Copyright 2010-2011 the original author or authors.
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

import griffon.domain.orm.AbstractCriterionEvaluator;
import griffon.domain.orm.BinaryExpression;
import griffon.domain.orm.PropertyExpression;
import griffon.domain.orm.UnaryExpression;

import static org.codehaus.groovy.runtime.InvokerHelper.getProperty;
import static org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation.compareEqual;
import static org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation.compareTo;

/**
 * @author Andres Almiray
 */
public class BeanCriterionEvaluator extends AbstractCriterionEvaluator {
    @Override
    protected boolean evalUnary(UnaryExpression criterion, Object target) {
        switch (criterion.getOperator()) {
            case IS_NULL:
                return getPropertyValue(target, criterion.getPropertyName()) == null;
            case IS_NOT_NULL:
                return getPropertyValue(target, criterion.getPropertyName()) != null;
            default:
                throw new IllegalArgumentException("Invalid UnaryExpression " + criterion);
        }
    }

    @Override
    protected boolean evalBinary(BinaryExpression criterion, Object target) {
        Object propertyValue = getPropertyValue(target, criterion.getPropertyName());
        switch (criterion.getOperator()) {
            case EQUAL:
                return compareEqual(propertyValue, criterion.getValue());
            case NOT_EQUAL:
                return !compareEqual(propertyValue, criterion.getValue());
            case GREATER_THAN:
                return compareTo(propertyValue, criterion.getValue()) > 0;
            case GREATER_THAN_OR_EQUAL:
                return compareTo(propertyValue, criterion.getValue()) >= 0;
            case LESS_THAN:
                return compareTo(propertyValue, criterion.getValue()) < 0;
            case LESS_THAN_OR_EQUAL:
                return compareTo(propertyValue, criterion.getValue()) <= 0;
            case LIKE:
            case NOT_LIKE:
            default:
                throw new IllegalArgumentException("Invalid BinaryExpression " + criterion);
        }
    }

    @Override
    protected boolean evalProperty(PropertyExpression criterion, Object target) {
        Object propertyValue1 = getPropertyValue(target, criterion.getPropertyName());
        Object propertyValue2 = getPropertyValue(target, criterion.getOtherPropertyName());
        switch (criterion.getOperator()) {
            case EQUAL:
                return compareEqual(propertyValue1, propertyValue2);
            case NOT_EQUAL:
                return !compareEqual(propertyValue1, propertyValue2);
            case GREATER_THAN:
                return compareTo(propertyValue1, propertyValue2) > 0;
            case GREATER_THAN_OR_EQUAL:
                return compareTo(propertyValue1, propertyValue2) >= 0;
            case LESS_THAN:
                return compareTo(propertyValue1, propertyValue2) < 0;
            case LESS_THAN_OR_EQUAL:
                return compareTo(propertyValue1, propertyValue2) <= 0;
            default:
                throw new IllegalArgumentException("Invalid BinaryExpression " + criterion);
        }
    }

    private Object getPropertyValue(Object owner, String propertyName) {
        return getProperty(owner, propertyName);
    }
}
