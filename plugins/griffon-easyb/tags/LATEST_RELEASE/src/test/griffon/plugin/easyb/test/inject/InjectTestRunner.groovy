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

/*
 * Created by IntelliJ IDEA.
 * User: richard
 * Date: Jun 9, 2009
 * Time: 11:34:07 PM
 */
public class InjectTestRunner {
    protected def testCase
    protected Binding binding
    String runnerType = "Uninitialized"

    protected void initialize() {
    }

    public void injectMethods(Binding binding) {
        //println "top level inject"
        this.binding = binding

        initialize()
        //println "and out"
    }

    public boolean isConfigured() {
        return this.testCase != null
    }


    public void setUp() {
        //println "settup testcase"
        if (testCase) {
            testCase.setUp()
        }
    }

    public void tearDown() {
        //println "teardown testcase"
        if (testCase) {
            testCase.tearDown()
        }
    }
}
