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

package griffon.domain.orm;

import java.util.ArrayList;
import java.util.List;

import groovy.lang.Closure;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.BooleanExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.syntax.Types;

/**
 * @author Andres Almiray
 */
public class CriteriaVisitor extends CodeVisitorSupport {
    private Object current;

    public static Criterion visit(Closure closure) {
        CriteriaVisitor visitor = new CriteriaVisitor();
        if (closure != null) {
            ClassNode classNode = closure.getMetaClass().getClassNode();
            if (classNode == null) {
                throw new CriteriaVisitException(
                        "Could not find the ClassNode for MetaClass: " + closure.getMetaClass());
            }
            List methods = classNode.getDeclaredMethods("doCall");
            if (!methods.isEmpty()) {
                MethodNode method = (MethodNode) methods.get(0);
                if (method != null) {
                    Statement statement = method.getCode();
                    if (statement != null) {
                        statement.visit(visitor);
                    }
                }
            }
        }

        return visitor.getCriterion();
    }

    public Criterion getCriterion() {
        return (Criterion) current;
    }

    public void visitReturnStatement(ReturnStatement statement) {
        statement.getExpression().visit(this);
    }

    public void visitBinaryExpression(BinaryExpression expression) {
        Expression left = expression.getLeftExpression();
        Expression right = expression.getRightExpression();
        boolean leaf = (right instanceof ConstantExpression) ||
                       (right instanceof PropertyExpression) ||
                       (right instanceof VariableExpression);

        Token token = expression.getOperation();
        Operator op = parseOperator(token.getText());
        left.visit(this);
        Object lhs = current;
        right.visit(this);
        Object rhs = current;

        if(lhs instanceof Criterion) {
            if(rhs instanceof Criterion &&
               (op == Operator.AND || op == Operator.OR)) {
                  current = new CompositeCriterion(op, (Criterion) lhs, (Criterion) rhs);
                  return;
            } else {
               throw new CriteriaVisitException(
                   "Invalid expression '" + rhs + "' after '"+ lhs +"' with operator " + op);
            }
        } else if(rhs instanceof Criterion) {
            throw new CriteriaVisitException(
               "Invalid expression '" + lhs + "' before '"+ rhs +"' with operator " + op);
        }
 
        if(lhs instanceof Property) {
            Property p = (Property) lhs;
            if(rhs instanceof Property) {
               current = new griffon.domain.orm.PropertyExpression(p.value, op, ((Property)rhs).value);
               return;
            } else if(rhs instanceof Value) {
               Value v = (Value) rhs;
               if("null".equals(v.value) || v.value == null) {
                   current = new griffon.domain.orm.UnaryExpression(p.value, op == Operator.EQUAL ? Operator.IS_NULL : Operator.IS_NOT_NULL);
               } else {
                   current = new griffon.domain.orm.BinaryExpression(p.value, op, v.value);
               }
               return;
            }
        } else if(lhs instanceof Value) {
            Value v = (Value) lhs;
            if(rhs instanceof Property) {
               Property p = (Property) rhs;
               if("null".equals(v.value) || v.value == null) {
                   current = new griffon.domain.orm.UnaryExpression(p.value, op == Operator.EQUAL ? Operator.IS_NULL : Operator.IS_NOT_NULL);
               } else if(op == Operator.EQUAL || op == Operator.NOT_EQUAL){
                   current = new griffon.domain.orm.BinaryExpression(p.value, op, v.value);
               } else {
                   current = Restrictions.not(new griffon.domain.orm.BinaryExpression(p.value, op, v.value));
               }
               return;
            } else if(rhs instanceof Value) {
               throw new CriteriaVisitException(
                   "Invalid expression " + lhs + " "+ op +" " + rhs);
            }
        }
    }

    public void visitBooleanExpression(BooleanExpression expression) {
        expression.getExpression().visit(this);
    }

    public void visitConstantExpression(ConstantExpression expression) {
        current = new Value(expression.getValue());
    }

    public void visitVariableExpression(VariableExpression expression) {
        current = new Property(expression.getName());
    }

    public void visitPropertyExpression(PropertyExpression expression) {
        current = new Property(expression.getPropertyAsString());
    }
    
    private static Operator parseOperator(String text) {
        if("==".equals(text)) return Operator.EQUAL;
        else if("!=".equals(text)) return Operator.NOT_EQUAL;
        else if(">".equals(text)) return Operator.GREATER_THAN;
        else if(">=".equals(text)) return Operator.GREATER_THAN_OR_EQUAL;
        else if("<".equals(text)) return Operator.LESS_THAN;
        else if("<=".equals(text)) return Operator.LESS_THAN_OR_EQUAL;
        else if("&&".equals(text)) return Operator.AND;
        else if("||".equals(text)) return Operator.OR;
        throw new CriteriaVisitException(
            "Invalid operator '" + text + "'. Valid operators are ==, !=, >, >=, <, <=, &&, ||");
    }

    private static class Property {
        String value;
        Property(String value) {
            this.value = value;
        }
        public String toString() { return value; }
    }

    private static class Value {
        Object value;
        Value(Object value) {
            this.value = value;
        }
        public String toString() { return String.valueOf(value); }
    }
}
