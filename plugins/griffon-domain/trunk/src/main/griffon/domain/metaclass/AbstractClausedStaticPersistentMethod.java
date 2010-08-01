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
package griffon.domain.metaclass;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

import griffon.domain.GriffonDomainClass;
import org.codehaus.griffon.runtime.domain.DomainHandler;
import griffon.domain.orm.Criterion;
import griffon.domain.orm.Restrictions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import groovy.lang.MissingMethodException;

import java.util.regex.Pattern;

/**
 * @author Andres Almiray
 */
public abstract class AbstractClausedStaticPersistentMethod extends AbstractPersistentStaticDynamicMethodInvocation {
    private static final Log LOG = LogFactory.getLog(AbstractClausedStaticPersistentMethod.class);
    private final String[] operators;
    private final Pattern[] operatorPatterns;

    public AbstractClausedStaticPersistentMethod(DomainHandler domainHandler, Pattern pattern, String[] operators) {
        super(domainHandler, pattern);
        this.operators = operators;
        this.operatorPatterns = new Pattern[this.operators.length];
        for (int i = 0; i < operators.length; i++) {
            this.operatorPatterns[i] = Pattern.compile("(\\w+)("+this.operators[i]+")(\\p{Upper})(\\w+)");
        }
    }

    protected Object invokeInternal(final GriffonDomainClass domainClass, String methodName, Object[] arguments) {
        List expressions = new ArrayList();
        if(arguments == null) arguments = new Object[0];
        Matcher match = super.getPattern().matcher(methodName);
        // find match
        match.find();

        String[] queryParameters;
        int totalRequiredArguments = 0;
        // get the sequence clauses
        final String querySequence;
        int groupCount = match.groupCount();
        if(groupCount == 4) {
            String booleanProperty = match.group(2);
            Boolean arg = Boolean.TRUE;
            if(booleanProperty.matches("Not[A-Z].*")) {
                booleanProperty = booleanProperty.substring(3);
                arg = Boolean.FALSE;
            }
            GriffonMethodExpression booleanExpression = GriffonMethodExpression.create(domainClass, booleanProperty);
            booleanExpression.setArguments(new Object[]{arg});
            expressions.add(booleanExpression);
            querySequence = match.group(4);
        } else {
            querySequence = match.group(2);
        }
        // if it contains operator and split
        boolean containsOperator = false;
        String operatorInUse = null;

        for (int i = 0; i < operators.length; i++) {
            Matcher currentMatcher = operatorPatterns[i].matcher(querySequence);
            if(currentMatcher.find()) {
                containsOperator = true;
                operatorInUse = this.operators[i];

                queryParameters = new String[2];
                queryParameters[0] = currentMatcher.group(1);
                queryParameters[1] = currentMatcher.group(3) + currentMatcher.group(4);

                // loop through query parameters and create expressions
                // calculating the numBer of arguments required for the expression
                int argumentCursor = 0;
                for (String queryParameter : queryParameters) {
                    GriffonMethodExpression currentExpression = GriffonMethodExpression.create(domainClass, queryParameter);
                    totalRequiredArguments += currentExpression.argumentsRequired;
                    // populate the arguments into the GriffonExpression from the argument list
                    Object[] currentArguments = new Object[currentExpression.argumentsRequired];
                    if ((argumentCursor + currentExpression.argumentsRequired) > arguments.length)
                        throw new MissingMethodException(methodName, domainClass.getClazz(), arguments);

                    for (int k = 0; k < currentExpression.argumentsRequired; k++, argumentCursor++) {
                        currentArguments[k] = arguments[argumentCursor];
                    }
                    try {
                        currentExpression.setArguments(currentArguments);
                    }
                    catch (IllegalArgumentException iae) {
                        LOG.debug(iae.getMessage(), iae);
                        throw new MissingMethodException(methodName, domainClass.getClazz(), arguments);
                    }
                    // add to list of expressions
                    expressions.add(currentExpression);
                }
                break;
            }
        }

        // otherwise there is only one expression
        if(!containsOperator) {
            GriffonMethodExpression solo = GriffonMethodExpression.create(domainClass, querySequence);

            if(solo.argumentsRequired > arguments.length)
                throw new MissingMethodException(methodName, domainClass.getClazz(), arguments);

            totalRequiredArguments += solo.argumentsRequired;
            Object[] soloArgs = new Object[solo.argumentsRequired];

            System.arraycopy(arguments, 0, soloArgs, 0, solo.argumentsRequired);
            try {
                solo.setArguments(soloArgs);
            }
            catch(IllegalArgumentException iae) {
                LOG.debug(iae.getMessage(),iae);
                throw new MissingMethodException(methodName, domainClass.getClazz(), arguments);
            }
            expressions.add(solo);
        }

        // if the total of all the arguments necessary does not equal the number of arguments
        // throw exception
        if(totalRequiredArguments > arguments.length)
            throw new MissingMethodException(methodName, domainClass.getClazz(), arguments);
        
        // calculate the remaining arguments
        Object[] remainingArguments = new Object[arguments.length - totalRequiredArguments];
        if(remainingArguments.length > 0) {        
            for (int i = 0, j = totalRequiredArguments; i < remainingArguments.length; i++,j++) {
                remainingArguments[i] = arguments[j];
            }
        }
        
        if(LOG.isTraceEnabled())
            LOG.trace("Calculated expressions: " + expressions);
        
        return doInvokeInternalWithExpressions(domainClass, methodName, remainingArguments, expressions, operatorInUse);
    }
    
    protected abstract Object doInvokeInternalWithExpressions(GriffonDomainClass domainClass, String methodName, Object[] arguments, List expressions, String operatorInUse);

    protected abstract static class GriffonMethodExpression {
        private static final String LESS_THAN = "LessThan";
        private static final String LESS_THAN_OR_EQUAL = "LessThanEquals";
        private static final String GREATER_THAN = "GreaterThan";
        private static final String GREATER_THAN_OR_EQUAL = "GreaterThanEquals";
        private static final String LIKE = "Like";
        // private static final String ILIKE = "Ilike";
        // private static final String BETWEEN = "Between";
        // private static final String IN_LIST= "InList";
        private static final String IS_NOT_NULL = "IsNotNull";
        private static final String IS_NULL = "IsNull";
        private static final String NOT = "Not";
        private static final String EQUAL = "Equal";
        private static final String NOT_EQUAL = "NotEqual";

        protected String propertyName;
        protected Object[] arguments;
        protected int argumentsRequired;
        protected boolean negation;
        protected String type;
        protected GriffonDomainClass domainClass;
        // private TypeConverter converter = new SimpleTypeConverter();

        GriffonMethodExpression(GriffonDomainClass domainClass, String propertyName, String type, int argumentsRequired, boolean negation) {
            this.domainClass = domainClass;
            this.propertyName = propertyName;
            this.type = type;
            this.argumentsRequired = argumentsRequired;
            this.negation = negation;
        }

        public String toString() {
            StringBuffer buf = new StringBuffer("[GriffonMethodExpression] ");
            buf.append(propertyName)
                .append(" ")
                .append(type)
                .append(" ");

            for (int i = 0; i < arguments.length; i++) {
                buf.append(arguments[i]);
                if(i != arguments.length)
                    buf.append(" and ");
            }
            return buf.toString();
        }

        void setArguments(Object[] args)
            throws IllegalArgumentException {
/*
            if(args.length != argumentsRequired)
                throw new IllegalArgumentException("Method expression '"+this.type+"' requires " + argumentsRequired + " arguments");

            GriffonDomainClassProperty prop = domainClass.getPropertyByName(propertyName);

            if(prop == null)
                throw new IllegalArgumentException("Property "+propertyName+" doesn't exist for method expression '"+this.type+"'");

            for (int i = 0; i < args.length; i++) {
                if(args[i] == null) continue;
                // convert GStrings to strings
                if(prop.getType() == String.class && (args[i] instanceof CharSequence)) {
                    args[i] = args[i].toString();
                }
                else if(!prop.getType().isAssignableFrom( args[i].getClass() ) && !(GriffonClassUtils.isMatchBetweenPrimitiveAndWrapperTypes(prop.getType(), args[i].getClass()))) {
                    try {
                        if(type.equals(IN_LIST)) {
                            args[i] = converter.convertIfNecessary(args[i], Collection.class);
                        }
                        else {
                            args[i] = converter.convertIfNecessary(args[i], prop.getType());
                        }
                    } catch (TypeMismatchException tme) {
                        // if we cannot perform direct conversion and argument is subclass of Number
                        // we can try to convert it through it's String representation
                        if(Number.class.isAssignableFrom(args[i].getClass())) {
                            try {
                                args[i] = converter.convertIfNecessary(args[i].toString(), prop.getType());
                            } catch(TypeMismatchException tme1) {

                                throw new IllegalArgumentException("Cannot convert value " + args[i] + " of property '"+propertyName+"' to required type " + prop.getType() + ": " + tme1.getMessage());
                            }
                        } else {
                            throw new IllegalArgumentException("Cannot convert value " + args[i] + " of property '"+propertyName+"' to required type " + prop.getType());
                        }
                    }
                }
            }

            this.arguments = args;
*/
        }

        abstract Criterion createCriterion();

        protected Criterion getCriterion() {
            if(arguments == null)
                throw new IllegalStateException("Parameters array must be set before retrieving Criterion");

            if(negation) {
                return Restrictions.not(createCriterion());
            }
            else {
                return createCriterion();
            }
        }

        protected static GriffonMethodExpression create(GriffonDomainClass domainClass, String queryParameter) {
            if(queryParameter.endsWith( LESS_THAN_OR_EQUAL )) {
                return new GriffonMethodExpression(
                        domainClass,
                        calcPropertyName(queryParameter, LESS_THAN_OR_EQUAL),
                        LESS_THAN_OR_EQUAL,
                        1,
                        isNegation(queryParameter, LESS_THAN_OR_EQUAL) ) {
                    Criterion createCriterion() {
                        return Restrictions.le( this.propertyName, arguments[0] );
                    }
                };
            }
            else if(queryParameter.endsWith( LESS_THAN )) {
                return new GriffonMethodExpression(
                        domainClass,
                        calcPropertyName(queryParameter, LESS_THAN),
                        LESS_THAN,
                        1, // argument count
                        isNegation(queryParameter, LESS_THAN) ) {
                    Criterion createCriterion() {
                        if(arguments[0] == null) return Restrictions.isNull(this.propertyName);
                        return Restrictions.lt( this.propertyName, arguments[0] );
                    }
                };
            }
            else if(queryParameter.endsWith( GREATER_THAN_OR_EQUAL )) {
                return new GriffonMethodExpression(
                        domainClass,
                        calcPropertyName(queryParameter, GREATER_THAN_OR_EQUAL),
                        GREATER_THAN_OR_EQUAL,
                        1,
                        isNegation(queryParameter, GREATER_THAN_OR_EQUAL) ) {
                    Criterion createCriterion() {
                        if(arguments[0] == null) return Restrictions.isNull(this.propertyName);
                        return Restrictions.ge( this.propertyName, arguments[0] );
                    }
                };
            }
            else if(queryParameter.endsWith( GREATER_THAN )) {
                return new GriffonMethodExpression(
                        domainClass,
                        calcPropertyName(queryParameter, GREATER_THAN),
                        GREATER_THAN,
                        1,
                        isNegation(queryParameter, GREATER_THAN) ) {
                    Criterion createCriterion() {
                        if(arguments[0] == null) return Restrictions.isNull(this.propertyName);
                        return Restrictions.gt( this.propertyName, arguments[0] );
                    }

                };
            }
//            else if(queryParameter.endsWith( LIKE )) {
//                return new GriffonMethodExpression(
//                        domainClass,
//                        calcPropertyName(queryParameter, LIKE),
//                        LIKE,
//                        1,
//                        isNegation(queryParameter, LIKE) ) {
//                    Criterion createCriterion() {
//                        if(arguments[0] == null) return Restrictions.isNull(this.propertyName);
//                        return Restrictions.like( this.propertyName, arguments[0] );
//                    }
//
//                };
//            }
//            else if(queryParameter.endsWith( ILIKE )) {
//                return new GriffonMethodExpression(
//                        domainClass,
//                        calcPropertyName(queryParameter, ILIKE),
//                        ILIKE,
//                        1,
//                        isNegation(queryParameter, ILIKE) ) {
//                    Criterion createCriterion() {
//                        if(arguments[0] == null) return Restrictions.isNull(this.propertyName);
//                        return Restrictions.ilike( this.propertyName, arguments[0] );
//                    }
//
//                };
//            }
            else if(queryParameter.endsWith( IS_NOT_NULL )) {
                return new GriffonMethodExpression(
                        domainClass,
                        calcPropertyName(queryParameter, IS_NOT_NULL),
                        IS_NOT_NULL,
                        0,
                        isNegation(queryParameter, IS_NOT_NULL) ) {
                    Criterion createCriterion() {
                            return Restrictions.isNotNull( this.propertyName );
                    }

                };
            }
            else if(queryParameter.endsWith( IS_NULL )) {
                return new GriffonMethodExpression(
                        domainClass,
                        calcPropertyName(queryParameter, IS_NULL),
                        IS_NULL,
                        0,
                        isNegation(queryParameter, IS_NULL) ) {
                    Criterion createCriterion() {
                        return Restrictions.isNull( this.propertyName );
                    }

                };
            }
//            else if(queryParameter.endsWith( BETWEEN )) {
//                return new GriffonMethodExpression(
//                        domainClass,
//                        calcPropertyName(queryParameter, BETWEEN),
//                        BETWEEN,
//                        2,
//                        isNegation(queryParameter, BETWEEN) ) {
//                    Criterion createCriterion() {
//                        return Restrictions.between( this.propertyName,this.arguments[0], this.arguments[1] );
//                    }
//
//                };
//            }
//            else if(queryParameter.endsWith( IN_LIST )) {
//
//                return new GriffonMethodExpression(
//                        domainClass,
//                        calcPropertyName(queryParameter, IN_LIST),
//                        IN_LIST,
//                        1,
//                        isNegation(queryParameter, IN_LIST) ) {
//                    Criterion createCriterion() {
//                        return Restrictions.in( this.propertyName,(Collection)this.arguments[0] );
//                    }
//
//                };
//            }
            else if(queryParameter.endsWith( NOT_EQUAL )) {
                return new GriffonMethodExpression(
                        domainClass,
                        calcPropertyName(queryParameter, NOT_EQUAL),
                        NOT_EQUAL,
                        1,
                        isNegation(queryParameter, NOT_EQUAL) ) {
                    Criterion createCriterion() {
                        if(arguments[0] == null) return Restrictions.isNotNull(this.propertyName);
                        return Restrictions.ne( this.propertyName,this.arguments[0]);
                    }

                };
            }
            else {
                return new GriffonMethodExpression(
                        domainClass,
                        calcPropertyName(queryParameter, null),
                        EQUAL,
                        1,
                        isNegation(queryParameter, EQUAL) ) {
                    Criterion createCriterion() {
                        if(arguments[0] == null) return Restrictions.isNull(this.propertyName);
                        return Restrictions.eq(this.propertyName, this.arguments[0]);
                    }
                };
            }
        }

        private static boolean isNegation(String queryParameter, String clause) {
            String propName;
            if(clause != null && !clause.equals( EQUAL )) {
                int i = queryParameter.indexOf(clause);
                propName = queryParameter.substring(0,i);
            }
            else {
                propName = queryParameter;
            }
            return propName.endsWith(NOT);
        }

        private static String calcPropertyName(String queryParameter, String clause) {
            String propName;
            if(clause != null && !clause.equals( EQUAL )) {
                int i = queryParameter.indexOf(clause);
                propName = queryParameter.substring(0,i);
            }
            else {
                propName = queryParameter;
            }
            if(propName.endsWith(NOT)) {
                int i = propName.lastIndexOf(NOT);
                propName = propName.substring(0, i);
            }
            return propName.substring(0,1).toLowerCase(Locale.ENGLISH)
                + propName.substring(1);
        }
    }
}
