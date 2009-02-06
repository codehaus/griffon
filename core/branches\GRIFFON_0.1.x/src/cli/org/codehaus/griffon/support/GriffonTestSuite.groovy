/*
 * Copyright 2004-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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
package org.codehaus.griffon.support

import junit.framework.Test
import junit.framework.TestCase
import junit.framework.TestResult
import junit.framework.TestSuite
//import org.codehaus.griffon.commons.GriffonClassUtils
//import org.springframework.beans.factory.config.AutowireCapableBeanFactory
//import org.springframework.context.ApplicationContext
//import org.springframework.context.ApplicationContextAware
//import org.springframework.util.Assert

/**
 * IoC class to inject properties of Griffon test case classes.
 *
 *
 * @author Steven Devijver
 * @since Aug 28, 2005
 */
public class GriffonTestSuite extends TestSuite {

    private static final String CLEANING_ORDER_PROPERTY = "cleaningOrder"
//    private static final String TRANSACTIONAL = "transactional"

//    private ApplicationContext applicationContext = null
    private List cleaningOrder
    private Class testClass

    public GriffonTestSuite(/*ApplicationContext applicationContext,*/ Class clazz) {
        super(clazz)
//        Assert.notNull(applicationContext, "Bean factory should not be null!")
//        this.applicationContext = applicationContext
//        Object order = GriffonClassUtils.getStaticPropertyValue( clazz, CLEANING_ORDER_PROPERTY )
        try {
            def order = clazz."$CLEANING_ORDER_PROPERTY"
            if( order != null && List.class.isAssignableFrom(order.getClass())) {
                cleaningOrder = (List) order
            }
        } catch( MissingPropertyException mpe ) {
            // ignore
        }
        this.testClass = clazz
    }

    public void runTest(Test test, TestResult result) {
//        // Auto-wire the test
//        if (test instanceof TestCase) {
//            applicationContext.getAutowireCapableBeanFactory().autowireBeanProperties(
//                    test,
//                    AutowireCapableBeanFactory.AUTOWIRE_BY_NAME,
//                    false)
//        }
//
//        // If the test is ApplicationContextAware, wire the context in now.
//        if (test instanceof ApplicationContextAware) {
//            ((ApplicationContextAware) test).setApplicationContext(this.applicationContext)
//        }

        test.run(result)
    }

    public List getCleaningOrder() {
        return (cleaningOrder != null) ? cleaningOrder : new ArrayList()
    }

//    public boolean isTransactional() {
//        Object val = GriffonClassUtils.getPropertyValueOfNewInstance(testClass, TRANSACTIONAL)
//        return !(val instanceof Boolean) || ((Boolean) val).booleanValue()
//    }
}
