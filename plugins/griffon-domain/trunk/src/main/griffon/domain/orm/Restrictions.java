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

/**
 * @author Andres Almiray
 */
public final class Restrictions {
    public static BinaryExpression eq(String propertyName, Object value) {
        return new BinaryExpression(propertyName, value, Operator.EQUAL);
    }

    public static BinaryExpression ne(String propertyName, Object value) {
        return new BinaryExpression(propertyName, value, Operator.NOT_EQUAL);
    }

    public static BinaryExpression gt(String propertyName, Object value) {
        return new BinaryExpression(propertyName, value, Operator.GREATER_THAN);
    }

    public static BinaryExpression ge(String propertyName, Object value) {
        return new BinaryExpression(propertyName, value, Operator.GREATER_THAN_OR_EQUAL);
    }

    public static BinaryExpression lt(String propertyName, Object value) {
        return new BinaryExpression(propertyName, value, Operator.LESS_THAN);
    }

    public static BinaryExpression le(String propertyName, Object value) {
        return new BinaryExpression(propertyName, value, Operator.LESS_THAN_OR_EQUAL);
    }

    public static BinaryExpression like(String propertyName, Object value) {
        return new BinaryExpression(propertyName, value, Operator.LIKE);
    }

    public static BinaryExpression notLike(String propertyName, Object value) {
        return new BinaryExpression(propertyName, value, Operator.NOT_LIKE);
    }

    public static PropertyExpression eqProperty(String propertyName, String otherPropertyValue) {
        return new PropertyExpression(propertyName, otherPropertyValue, Operator.EQUAL);
    }

    public static PropertyExpression neProperty(String propertyName, String otherPropertyValue) {
        return new PropertyExpression(propertyName, otherPropertyValue, Operator.NOT_EQUAL);
    }

    public static PropertyExpression gtProperty(String propertyName, String otherPropertyValue) {
        return new PropertyExpression(propertyName, otherPropertyValue, Operator.GREATER_THAN);
    }

    public static PropertyExpression geProperty(String propertyName, String otherPropertyValue) {
        return new PropertyExpression(propertyName, otherPropertyValue, Operator.GREATER_THAN_OR_EQUAL);
    }

    public static PropertyExpression ltProperty(String propertyName, String otherPropertyValue) {
        return new PropertyExpression(propertyName, otherPropertyValue, Operator.LESS_THAN);
    }

    public static PropertyExpression leProperty(String propertyName, String otherPropertyValue) {
        return new PropertyExpression(propertyName, otherPropertyValue, Operator.LESS_THAN_OR_EQUAL);
    }

    public static UnaryExpression isNull(String propertyName) {
        return new UnaryExpression(propertyName, Operator.IS_NULL);
    }

    public static UnaryExpression isNotNull(String propertyName) {
        return new UnaryExpression(propertyName, Operator.IS_NOT_NULL);
    }

    public static Criterion not(Criterion criterion) {
        if(criterion instanceof BinaryExpression) {
            BinaryExpression exp = (BinaryExpression) criterion;
            switch(exp.getOperator()) {
                case EQUAL: return ne(exp.getPropertyName(), exp.getValue());
                case NOT_EQUAL: return eq(exp.getPropertyName(), exp.getValue());
                case GREATER_THAN: return le(exp.getPropertyName(), exp.getValue());
                case GREATER_THAN_OR_EQUAL: return lt(exp.getPropertyName(), exp.getValue());
                case LESS_THAN: return ge(exp.getPropertyName(), exp.getValue());
                case LESS_THAN_OR_EQUAL: return gt(exp.getPropertyName(), exp.getValue());
                case LIKE: return notLike(exp.getPropertyName(), exp.getValue());
                case NOT_LIKE: return like(exp.getPropertyName(), exp.getValue());
            }
        } else if(criterion instanceof PropertyExpression) {
            PropertyExpression exp = (PropertyExpression) criterion;
            switch(exp.getOperator()) {
                case EQUAL: return neProperty(exp.getPropertyName(), exp.getOtherPropertyName());
                case NOT_EQUAL: return eqProperty(exp.getPropertyName(), exp.getOtherPropertyName());
                case GREATER_THAN: return leProperty(exp.getPropertyName(), exp.getOtherPropertyName());
                case GREATER_THAN_OR_EQUAL: return ltProperty(exp.getPropertyName(), exp.getOtherPropertyName());
                case LESS_THAN: return geProperty(exp.getPropertyName(), exp.getOtherPropertyName());
                case LESS_THAN_OR_EQUAL: return gtProperty(exp.getPropertyName(), exp.getOtherPropertyName());
            }
        } else if(criterion instanceof UnaryExpression) {
            UnaryExpression exp = (UnaryExpression) criterion;
            switch(exp.getOperator()) {
                case IS_NULL: return isNotNull(exp.getPropertyName());
                case IS_NOT_NULL: return isNull(exp.getPropertyName());
            }
        } 
        return criterion;
    }
}
