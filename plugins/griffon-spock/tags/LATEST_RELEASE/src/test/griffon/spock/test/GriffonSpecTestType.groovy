/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.spock.test

import griffon.spock.test.listener.OverallRunListener

import org.spockframework.buildsupport.SpecClassFileFinder
import org.spockframework.runtime.SpecUtil

import org.junit.runner.JUnitCore

import org.codehaus.griffon.test.GriffonTestTypeResult
import org.codehaus.griffon.test.event.GriffonTestEventPublisher
import org.codehaus.griffon.test.support.GriffonTestTypeSupport
import org.codehaus.griffon.test.report.junit.JUnitReportsFactory

class GriffonSpecTestType extends GriffonTestTypeSupport {
    private final List<Class> specClasses = []
    
    GriffonSpecTestType(String name, String relativeSourcePath) {
        super(name, relativeSourcePath)
    }
    
    protected List<String> getTestExtensions() {
        ["groovy"]
    }

    protected List<String> getTestSuffixes() { 
        ["Spec", "Specification"]
    }
    
    JUnitReportsFactory createJUnitReportsFactory() {
        JUnitReportsFactory.createFromBuildBinding(buildBinding)
    }
    
    protected int doPrepare() {
        eachSourceFile { testTargetPattern, specSourceFile ->
            def specClass = sourceFileToClass(specSourceFile)
            if (SpecUtil.isRunnableSpec(specClass)) specClasses << specClass
        }
        
        specClasses.sum 0, { SpecUtil.getFeatureCount(it) }
    }
    
    GriffonTestTypeResult doRun(GriffonTestEventPublisher eventPublisher) {
        def junit = new JUnitCore()
        junit.addListener(new OverallRunListener(eventPublisher, createJUnitReportsFactory(), createSystemOutAndErrSwapper()))
        new GriffonSpecTestTypeResult(junit.run(specClasses as Class[]))
    }
}
