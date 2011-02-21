/*
 * Copyright 2011 the original author or authors.
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

package griffon.uispec4j

import org.uispec4j.*
import java.util.concurrent.CountDownLatch
import griffon.swing.SwingGriffonApplication
import griffon.test.*
import griffon.util.UIThreadHelper
import org.codehaus.griffon.commons.ConfigurationHolder

/**
 * @author Andres Almiray
 */
abstract class GriffonUISpecTestCase extends UISpecTestCase {
    private boolean realized = false
    private final Object realizedLock = new Object()

    protected SwingGriffonApplication app

    protected final void setUp() {
        super.setUp()
        setUpTestCase()
        initApp()
        setAdapter(initAdapter())
        onSetUp()
    }

    protected final void tearDown() {
        onTearDown()
        tearDownTestCase()
        for(w in app.windowManager.windows.findAll{it.visible}) app.windowManager.hide(w)
        super.tearDown()
    }
 
    protected void setupConfig(SwingGriffonApplication app) { }
    protected void onSetUp() { }
    protected void onTearDown() { }
 
    private final void initApp() {
        synchronized(realizedLock) {
            if(!realized) {
                app.metaClass.exit = { ->
                    // NOOP
                }

                def latch = new CountDownLatch(1)
                app.config.application.autoShutdown = false
                setupConfig(app)
                app.execSync {
                    app.realize()
                    latch.countDown()
                }
                latch.await()
                realized = true
            }
        }
    }
 
    protected UISpecAdapter initAdapter() {
        if(app.windowManager.windows.size() > 0) return new GriffonUISpecAdapter(app)
        throw new IllegalStateException("Application does not have managed Windows!")
    }

    // from GriffonUnitTestCase

    private Map savedMetaClasses
    private previousConfig

    private void setUpTestCase() {
        savedMetaClasses = [:]
        previousConfig = ConfigurationHolder.config
    }

    private void tearDownTestCase() {
        // Restore all the saved meta classes.
        savedMetaClasses.each { clazz, metaClass ->
            GroovySystem.metaClassRegistry.removeMetaClass(clazz) 
            GroovySystem.metaClassRegistry.setMetaClass(clazz, metaClass)
        }

        ConfigurationHolder.config = previousConfig
    }

    /**
     * Use this method when you plan to perform some meta-programming
     * on a class. It ensures that any modifications you make will be
     * cleared at the end of the test.
     * @param clazz The class to register.
     */
    protected void registerMetaClass(Class clazz) {
        // If the class has already been registered, then there's nothing to do.
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
        ConfigurationHolder.config = new ConfigSlurper().parse(config)
    }

    /** Executes code synchronously inside the UI thread */
    def execSync = UIThreadHelper.instance.&executeSync
    /** Executes code asynchronously inside the UI thread */
    def execAsync = UIThreadHelper.instance.&executeAsync
    /** Executes code outside the UI thread */
    def execOutside = UIThreadHelper.instance.&executeOutside
    /** True if the current thread is the UI thread */
    def isUIThread = UIThreadHelper.instance.&isUIThread
    /** Schedules a block of code as a Future */
    def execFuture = { Object... args ->
        UIThreadHelper.instance.executeFuture(*args)
    }
}
