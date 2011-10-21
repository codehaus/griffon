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

package griffon.plugin.easyb.test.inject.unit

/*
 * User: richard
 * Date: Jun 7, 2009
 * Time: 9:51:13 PM
 */

import griffon.test.GriffonUnitTestCase
// import org.codehaus.griffon.commons.ApplicationHolder
import griffon.plugin.easyb.test.inject.InjectTestRunner

public class InjectGriffonTestRunner extends InjectTestRunner {
    protected void initialize() {
        runnerType = "Griffon Unit Test"
        this.testCase = new GriffonUnitTestCase()
    }

    public void injectMethods(Binding binding) {
        super.injectMethods(binding)
        //println "second level inject"

//        if (ApplicationHolder.application) {
//            binding.inject = {beanName ->
//                // time to get *really* meta
//                binding."${beanName}" = ApplicationHolder.application.mainContext.getBean(beanName)
//            }
//        }

        binding.registerMetaClass = {Class clazz ->
            if (testCase) {
                testCase.registerMetaClass clazz
            } else {
                throw new RuntimeException("no test case associated with story/scenario")
            }
        }

        binding.mockFor = {Class clazz, boolean loose = false ->
            if (testCase) {
                return testCase.mockFor(clazz, loose)
            } else {
                throw new RuntimeException("no test case associated with story/scenario")
            }
        }

        binding.mockConfig = {String config ->
            if (testCase) {
                testCase.mockConfig(config)
            } else {
                throw new RuntimeException("no test case associated with story/scenario")
            }
        }
    }
}
