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

/*
 * User: richard
 * Date: Jun 18, 2009
 * Time: 9:20:50 PM
 */
package griffon.plugin.easyb.test.inject.unit

import org.easyb.domain.Behavior
import griffon.plugin.easyb.test.GriffonEasybTestType
import griffon.plugin.easyb.test.inject.TestRunnerFactory
import griffon.plugin.easyb.test.inject.InjectTestRunner

public class InjectUnitTestRunnerFactory implements TestRunnerFactory {
    private static String pathPartMustExist = "test" + File.separator + "unit"

    @Override
    public boolean willRespond(Behavior currentBehaviour, GriffonEasybTestType gett) {
        return gett.testType == "unit" && currentBehaviour.file.absolutePath.indexOf(pathPartMustExist) >= 0
    }

    @Override
    public InjectTestRunner findMatchingRunner(String expectedMatchingGriffonClass, Behavior currentBehaviour, GriffonEasybTestType gett) {
        return getDefaultTestRunner(gett)
    }

    @Override
    public InjectTestRunner getDefaultTestRunner(GriffonEasybTestType gett) {
        return new InjectGriffonTestRunner()
    }

    @Override
    public InjectTestRunner findDynamicRunner(String style, String name, String expectedMatchingGriffonClass, Behavior currentBehavior, GriffonEasybTestType gett) {
        return new InjectGriffonTestRunner()
    }
}
