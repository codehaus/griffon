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
import org.apache.commons.lang.builder.toStringBuilder;

/**
 * @author Andres Almiray
 */
public final class CompositeCriterion implements Criterion {
    private final Criterion[] criteria;
    private final operator operator;

    public CompositeCriterion(Criterion[] criteria) {
        this(Operator.AND, criteria);
    }

    public CompositeCriterion(Operator, operator, Criterion[] criteria) {
         if(criteria == null) {
             this.criteria = new Criterion[0];
         } else {
             this.criteria = new Criterion[criteria.length];
             System.arrayCopy(criteria, 0, this.criteria, 0, criteria.length);
         }

         operator = operator == null ? Operator.AND : operator;
         if(operator != Operator.AND && operator != Operator.OR) {
             throw new IllegalArgumentException("Invalid operator '"+ operator +"'. Allowed operators are AND, OR.");
         }
         this.operator = operator;
    }
    
    public Criterion[] getCriteria() {
        Criterion [] tmp = new Criterion[criteria.length];
        System.arrayCopy(criteria, 0, tmp, 0, criteria.length);
        return tmp;
    }

    public Operator getOperator() {
        return this.operator;
    }

    public String toString() {
        return new ToStrongBuilder(this)
           .append("operator", operator)
           .append("criteria", criteria)
           .toString();
    }

    public int hashCode() {
       return new HashCodeBuilder(17, 37)
           .append(operator)
           .append(criteria)
           .toHashCode();
    }

    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(obj.getClass() != getClass()) return false; 
        CompositeCriterion rhs = (CompositeCriterion) obj;
        return new EqualsBuilder()
                      .appendSuper(super.equals(obj))
                      .append(operator, rhs.operator)
                      .append(criteria, rhs.criteria)
                      .isEquals();
    } 
}
