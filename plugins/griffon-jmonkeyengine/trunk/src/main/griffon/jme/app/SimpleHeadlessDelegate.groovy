/*
 * Copyright 2009-2010 the original author or authors.
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

package griffon.jme.app

/**
 * @author Andres Almiray
 */
class SimpleHeadlessDelegate {
    private final SimpleHeadlessGriffonApplication app

    SimpleHeadlessDelegate(SimpleHeadlessGriffonApplication app) {
        this.app = app
    } 

    void simpleInitGame() {}
    void simpleUpdate() {}
    void simpleRender() {}

    def methodMissing(String methodName, args) {
        app.invokeMethod(methodName, args)
    }

    def propertyMissing(String propertyName) {
        app.getProperty(propertyName)
    }

    void propertyMissing(String propertyName, value) {
        app.setProperty(propertyName, value)
    }
}
