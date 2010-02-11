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

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * @author Andres Almiray
 */
public class PropertyExpression implements Criterion {
    private final String propertyName;
    private final String otherPropertyName;
    private final Operator operator;

    public PropertyExpression(String propertyName, String otherPropertyName, Operator operator) {
        this.propertyName = propertyName; 
        this.otherPropertyName = otherPropertyName; 
        this.operator = operator; 
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getOtherPropertyName() {
        return otherPropertyName;
    }

    public Operator getOperator() {
        return operator;
    }

    public String toString() {
        return propertyName +" "+ operator +" "+ otherPropertyName;
    }

    public int hashCode() {
       return new HashCodeBuilder(17, 37)
           .append(propertyName)
           .append(otherPropertyName)
           .append(operator)
           .toHashCode();
    }

    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(obj.getClass() != getClass()) return false; 
        PropertyExpression rhs = (PropertyExpression) obj;
        return new EqualsBuilder()
                      .appendSuper(super.equals(obj))
                      .append(propertyName, rhs.propertyName)
                      .append(otherPropertyName, rhs.otherPropertyName)
                      .append(operator, rhs.operator)
                      .isEquals();
    } 
}
