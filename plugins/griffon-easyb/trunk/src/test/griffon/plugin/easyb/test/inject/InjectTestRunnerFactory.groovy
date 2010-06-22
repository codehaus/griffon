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

/*
 * User: richard
 * This is used to determine what injection factory should be used
 * Date: Jun 9, 2009
 * Time: 10:38:54 PM
 */
public class InjectTestRunnerFactory {
    static List<TestRunnerFactory> externalFactories = new ArrayList<TestRunnerFactory>()

    public static registerExternalFactory(TestRunnerFactory factory) {
        externalFactories << factory
    }

    private static String getExpectedClasspath(GriffonEasybTestType gett, Behavior currentBehaviour, String name = null) {
        String expectedMatchingGriffonClass

        expectedMatchingGriffonClass = gett.sourceFileToClassName(currentBehaviour.file)

        gett.getTestSuffixes().each {String ending ->
            if (expectedMatchingGriffonClass.endsWith(ending)) {
                expectedMatchingGriffonClass = expectedMatchingGriffonClass.substring(0, expectedMatchingGriffonClass.length() - ending.length())
            }
        }

        return expectedMatchingGriffonClass
    }

    /**
     * can return null, no matching runner for test type
     */
    public static InjectTestRunner findMatchingRunner(Behavior currentBehaviour, GriffonEasybTestType gett) {
        String classpath = getExpectedClasspath(gett, currentBehaviour, gett.testType)

        InjectTestRunner matchingRunner = null

        for (TestRunnerFactory fact: externalFactories) {
            if (fact.willRespond(currentBehaviour, gett)) {
                matchingRunner = fact.findMatchingRunner(classpath, currentBehaviour, gett)
                if (matchingRunner) {
                    break
                }
            }
        }

        if (!matchingRunner) {
            for (TestRunnerFactory fact: externalFactories) {
                if (fact.willRespond(currentBehaviour, gett)) {
                    matchingRunner = fact.getDefaultTestRunner(gett)
                    if (matchingRunner) {
                        break
                    }
                }
            }
        }

        return matchingRunner
    }

    public static InjectTestRunner getDefault(Behavior currentBehaviour, GriffonEasybTestType gett) {
        InjectTestRunner matchingRunner = null

        for (TestRunnerFactory fact: externalFactories) {
            if (fact.willRespond(currentBehaviour, gett)) {
                matchingRunner = fact.getDefaultTestRunner(gett)
                if (matchingRunner) {
                    break
                }
            }
        }

        return matchingRunner
    }

    public static InjectTestRunner findDynamicRunner(String style, String name, Behavior currentBehaviour, GriffonEasybTestType gett) {
        InjectTestRunner matchingRunner = null

        String expectedMatchingGriffonClass = getExpectedClasspath(gett, currentBehaviour, name)

        // use external iterator as can break from it
        for (TestRunnerFactory fact: externalFactories) {
            if (fact.willRespond(currentBehaviour, gett)) {
                matchingRunner = fact.findDynamicRunner(style, name, expectedMatchingGriffonClass, currentBehaviour, gett)
                if (matchingRunner) {
                    break
                }
            }
        }

        return matchingRunner
    }
}
