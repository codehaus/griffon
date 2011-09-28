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

package griffon.domain.orm;

import griffon.domain.DomainClassUtils;
import groovy.lang.Closure;

/**
 * @author Andres Almiray
 */
public abstract class AbstractCriterionEvaluator implements CriterionEvaluator {
    public final boolean eval(Object target, Closure criterion) {
        return eval(target, DomainClassUtils.getInstance().buildCriterion(criterion));
    }

    public final boolean eval(Object target, Criterion criterion) {
        if (criterion == null) {
            throw new IllegalArgumentException("Criterion is null!");
        }
        if (target == null) {
            throw new IllegalArgumentException("Target is null!");
        }
        if (criterion instanceof CompositeCriterion) {
            CompositeCriterion compositeCriterion = (CompositeCriterion) criterion;
            for (Criterion c : compositeCriterion.getCriteria()) {
                if (!eval(target, c)) {
                    return false;
                }
            }
            return true;
        } else if (criterion instanceof UnaryExpression) {
            return evalUnary((UnaryExpression) criterion, target);
        } else if (criterion instanceof BinaryExpression) {
            return evalBinary((BinaryExpression) criterion, target);
        } else if (criterion instanceof PropertyExpression) {
            return evalProperty((PropertyExpression) criterion, target);
        }
        throw new IllegalArgumentException("Don't know how to evaluate criterion " + criterion);
    }

    protected abstract boolean evalUnary(UnaryExpression criterion, Object target);

    protected abstract boolean evalBinary(BinaryExpression criterion, Object target);

    protected abstract boolean evalProperty(PropertyExpression criterion, Object target);
}
