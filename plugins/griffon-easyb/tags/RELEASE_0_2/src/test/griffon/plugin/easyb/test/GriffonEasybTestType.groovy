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

package griffon.plugin.easyb.test

import org.codehaus.griffon.test.support.GriffonTestTypeSupport
import org.codehaus.griffon.test.GriffonTestTypeResult
import org.codehaus.griffon.test.event.GriffonTestEventPublisher
import griffon.plugin.easyb.test.listener.GriffonEasybListener
import griffon.plugin.easyb.test.report.EasybReportsFactory
import org.easyb.Configuration
import org.easyb.BehaviorRunner
import org.easyb.domain.BehaviorFactory

public class GriffonEasybTestType extends GriffonTestTypeSupport {
    protected final List<Class> easybFiles = []
    public final String testType

    GriffonEasybTestType(String name, String relativeSourcePath) {
        super(name, relativeSourcePath)
        testType = relativeSourcePath
    }

    public String sourceFileToClassName(File source) {
        return super.sourceFileToClassName(source)
    }

    /**
     * Gather all the tests that easyb should run. *.Story and *.Specification
     * @return the amount of stories + specifications that will be executed.
     */
    protected int doPrepare() {
        testTargetPatterns.each { testTargetPattern ->
            findSourceFiles(testTargetPattern).each { file ->
                if (isEasybSourceFile(file)) {
                    easybFiles << file
                }
            }
        }

        //TODO should count stories and specifications instead of easybFiles.size()
        return easybFiles.size()
    }

    /**
     * Run the tests through BehaviourRunner from easyb.
     * @return the test results encapsulated by GriffonTestTypeResult
     */
    protected GriffonTestTypeResult doRun(GriffonTestEventPublisher griffonTestEventPublisher) {
        def easybListener = new GriffonEasybListener(griffonTestEventPublisher, createEasybReportsFactory(), createSystemOutAndErrSwapper(), this)
        def easybRunner = new BehaviorRunner(new Configuration(), easybListener)
        easybRunner.runBehaviors(BehaviorRunner.getBehaviors(easybFiles as String[]))
        return new GriffonEasybTestTypeResult(easybListener)
    }

    public List<String> getTestExtensions() {
        return ["groovy", "story", "specification"]
    }

    public List<String> getTestSuffixes() {
        return ["*"]
    }

    EasybReportsFactory createEasybReportsFactory() {
        EasybReportsFactory.createFromBuildBinding(buildBinding)
    }

    /**
     * Verifies if the given file is an easyb test file.
     * @param file the file to test if is an easyb source file
     * @return true if is easyb source file, false otherwise
     */
    private boolean isEasybSourceFile(File file) {
        def boolean isValidExtension = file.absolutePath.endsWith(".specification") ||
                file.absolutePath.endsWith(".story") ||
                file.absolutePath.endsWith("Story.groovy") ||
                file.absolutePath.endsWith("Specification.groovy")

        if(!isValidExtension) {
            return false
        }

        try {
            BehaviorFactory.createBehavior(file)
            return true
        } catch (e) {
            return false
        }
    }
}
