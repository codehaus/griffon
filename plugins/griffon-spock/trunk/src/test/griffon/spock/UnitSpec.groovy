/* 
 * Copyright 2009-2010 the original author or authors.
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

package griffon.spock

import org.codehaus.griffon.commons.*

import griffon.test.GriffonMock
import griffon.util.GriffonUtil

import spock.lang.Specification

/**
 * Support class for writing unit tests in Griffon. It mainly provides
 * access to various mocking options, while making sure that the meta-
 * class magic does not leak outside of a single test.
 */
class UnitSpec extends Specification {
    private savedMetaClasses

    def setup() {
        savedMetaClasses = [:]
    }

    def cleanup() {
        ConfigurationHolder.config = null
        // Restore all the saved meta classes.
        savedMetaClasses.each {clazz, metaClass ->
            GroovySystem.metaClassRegistry.removeMetaClass(clazz)
            GroovySystem.metaClassRegistry.setMetaClass(clazz, metaClass)
        }
    }

    /**
     * Use this method when you plan to perform some meta-programming
     * on a class. It ensures that any modifications you make will be
     * cleared at the end of the test.
     * @param clazz The class to register.
     */
    protected void registerMetaClass(Class clazz) {
        // If the class has already been registered, then there's
        // nothing to do.
        if (savedMetaClasses.containsKey(clazz)) return

        // Save the class's current meta class.
        savedMetaClasses[clazz] = clazz.metaClass

        // Create a new EMC for the class and attach it.
        def emc = new ExpandoMetaClass(clazz, true, true)
        emc.initialize()
        GroovySystem.metaClassRegistry.setMetaClass(clazz, emc)
    }

    /**
     * Creates a new Griffon mock for the given class. Use it as you
     * would use MockFor and StubFor.
     * @param clazz The class to mock.
     * @param loose If <code>true</code>, the method returns a loose-
     * expectation mock, otherwise it returns a strict one. The default
     * is a strict mock.
     */
    protected GriffonMock mockFor(Class clazz, boolean loose = false) {
        registerMetaClass(clazz)
        return new GriffonMock(clazz, loose)
    }

    protected void mockConfig(String config) {
        def c = new ConfigSlurper().parse(config)
        ConfigurationHolder.config = c
    }
}
