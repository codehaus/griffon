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

package griffon.plugin.easyb.test.inject

import org.easyb.domain.Behavior
import griffon.plugin.easyb.test.GriffonEasybTestType

/**
 * User: richard
 */
public interface TestRunnerFactory {

    /**
     * always called before attempting to call any other functions
     *
     * @param currentBehaviour
     * @param gett
     * @return
     */
    boolean willRespond(Behavior currentBehaviour, GriffonEasybTestType gett);

    /**
     * this method is used on initial load (startBehavior) of the behavior - and determines automatically which test case type to
     * associate with this behavior.
     *
     * @param expectedMatchingGriffonClass
     * @param currentBehaviour
     * @param gett
     * @return
     */
    InjectTestRunner findMatchingRunner(String expectedMatchingGriffonClass, Behavior currentBehaviour, GriffonEasybTestType gett);

    /**
     * this is used by griffonTest style, name - such as griffonTest "controller", "Person" - would cause this to try and load a PersonController using the same package
     * as this behavior file. griffonTest "taglib", "com.bluetrainsoftware.testapp.taglibs.MyTagLib" would find the taglib wherever it was however.
     *
     * @param style
     * @param name
     * @param expectedMatchingGriffonClass
     * @param currentBehavior
     * @param gett
     * @return
     */
    InjectTestRunner findDynamicRunner(String style, String name, String expectedMatchingGriffonClass, Behavior currentBehavior, GriffonEasybTestType gett);

    /**
     * if we don't find a specific matching runner or if the initialize fails, then we need to go back and ask for a default runner for this type. For unit it will be InjectGriffonTestRunner
     *
     * @param gett
     * @return
     */
    InjectTestRunner getDefaultTestRunner(GriffonEasybTestType gett);
}
