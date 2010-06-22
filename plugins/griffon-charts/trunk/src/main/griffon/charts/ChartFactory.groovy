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

package griffon.charts

import com.thecoderscorner.groovychart.chart.ChartBuilder
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart

/**
 * @author Andres.Almiray
 */
class ChartFactory extends AbstractFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {

        JFreeChart chart = null
        if(value instanceof JFreeChart) {
            chart = value
        } else if(value instanceof Class) {
            chart = buildChart(builder, value)
        } else {
            throw new IllegalArgumentException("In $name you must define a value of type ${JFreeChart} or ${Class}")
        }

        new ChartPanel(chart,
                toInt(attributes.remove('width'), ChartPanel.DEFAULT_WIDTH),
                toInt(attributes.remove('height'), ChartPanel.DEFAULT_HEIGHT),
                toInt(attributes.remove('minimumDrawWidth'), ChartPanel.DEFAULT_MINIMUM_DRAW_WIDTH),
                toInt(attributes.remove('minimumDrawHeight'), ChartPanel.DEFAULT_MINIMUM_DRAW_HEIGHT),
                toInt(attributes.remove('maximumDrawWidth'), ChartPanel.DEFAULT_MAXIMUM_DRAW_WIDTH),
                toInt(attributes.remove('maximumDrawHeight'), ChartPanel.DEFAULT_MAXIMUM_DRAW_HEIGHT),
                toBoolean(attributes.remove('useBuffer'), true),
                toBoolean(attributes.remove('properties'), true),
                toBoolean(attributes.remove('copy'), true),
                toBoolean(attributes.remove('save'), true),
                toBoolean(attributes.remove('print'), true),
                toBoolean(attributes.remove('zoom'), true),
                toBoolean(attributes.remove('tooltips'), true)
            )
    }

    private static JFreeChart buildChart(FactoryBuilderSupport builder, Class chartScriptClass) {
        final ChartBuilder chartBuilder = new ChartBuilder()
        final Script delegateScript = chartScriptClass.newInstance()
        delegateScript.metaClass.with {
            propertyMissing = {String name -> chartBuilder.getProperty(name) }
            propertyMissing = {String name, value -> chartBuilder.setProperty(name, value) }
            methodMissing = {String name, args -> chartBuilder."$name"(*args) }
        }

        Script script = new Script() {
            public Object run() {
                return delegateScript.run()
            }

            def propertyMissing(String name) {
                chartBuilder.getProperty(name)
            }

            def propertyMissing(String name, Object value) {
                chartBuilder.setProperty(name, value)
            }

            def methodMissing(String name, args) {
                chartBuilder."$name"(*args) 
            }
        }
        delegateScript.binding = builder
        script.binding = builder
        return script.run().chart
    }

    private static int toInt(value, int defaultValue = 0i) {
        if(value == null) {
            return defaultValue
        } else if(value instanceof Number) {
            return value.intValue()
        } else {
            try {
                return Integer.parseInt(String.valueOf(value))
            } catch(NumberFormatException nfe) {
                return defaultValue
            }
        }
    }

    private static boolean toBoolean(value, boolean defaultValue = false) {
        if(value == null) {
            return defaultValue
        } else if(value instanceof Boolean) {
            return value.booleanValue()
        } else {
            try {
                return Boolean.parseBoolean(String.valueOf(value))
            } catch(Exception e) {
                return defaultValue
            }
        }
    }
}
