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

package griffon.plugin.easyb.test.listener

import org.easyb.listener.ResultsCollector
import org.easyb.listener.ResultsAmalgamator
import org.codehaus.griffon.test.event.GriffonTestEventPublisher
import org.codehaus.griffon.test.io.SystemOutAndErrSwapper
import org.easyb.result.Result
import org.easyb.domain.Behavior
import org.easyb.BehaviorStep
import org.easyb.util.BehaviorStepType
import griffon.plugin.easyb.test.GriffonEasybTestType
import griffon.plugin.easyb.test.report.EasybReportsFactory
import griffon.plugin.easyb.test.inject.InjectTestRunner
import griffon.plugin.easyb.test.inject.InjectTestRunnerFactory
import org.slf4j.LoggerFactory
import org.slf4j.Logger

public class GriffonEasybResultsCollector extends ResultsCollector {
    private static final Logger log = LoggerFactory.getLogger(GriffonEasybResultsCollector)

    final protected GriffonTestEventPublisher eventPublisher
    final protected EasybReportsFactory reportsFactory
    final protected SystemOutAndErrSwapper outAndErrSwapper
    protected GriffonEasybTestType griffonEasybTestType

    protected Behavior currentBehaviour
    protected Stack<BehaviorStep> steps = new Stack<BehaviorStep>()
    protected BehaviorStep currentStep
    protected InjectTestRunner testRunner
    protected List<Behavior> behaviors = new ArrayList<Behavior>()
    private int pendingCount = 0
    private int failedCount = 0
    private int ignoredCount = 0
    private int inReviewCount = 0

    GriffonEasybResultsCollector(GriffonTestEventPublisher eventPublisher, EasybReportsFactory reportsFactory, SystemOutAndErrSwapper outAndErrSwapper, GriffonEasybTestType griffonEasybTestType) {
        super()
        this.eventPublisher = eventPublisher
        this.reportsFactory = reportsFactory
        this.outAndErrSwapper = outAndErrSwapper
        this.griffonEasybTestType = griffonEasybTestType
    }

    int getBehaviorCount() { behaviors.size() }
    int getPendingBehaviorCount() { pendingCount }
    int getFailedBehaviorCount() { failedCount }
    int getIgnoredBehaviorCount() { ignoredCount }
    int getInReviewBehaviorCount() { inReviewCount }

    /**
     * Called when a Story or Specification file before being executed (comparable as a TestCase).
     * Publishes the event testCaseStart.
     */
    @Override
    public void startBehavior(Behavior behavior) {
        super.startBehavior(behavior)
        behaviors << behavior

        eventPublisher.testCaseStart(trucateEventName(behavior.phrase))

        currentBehaviour = behavior
        testRunner = InjectTestRunnerFactory.findMatchingRunner(currentBehaviour, griffonEasybTestType)

        if(! testRunner) {
            testRunner = InjectTestRunnerFactory.getDefault(currentBehaviour, griffonEasybTestType)
        }

        if(testRunner) {
            testRunner.initialize()

            if (testRunner.testCase == null) {
                log.warn "Unable to create expected test runner ${testRunner.runnerType}, using default instead"
                testRunner = InjectTestRunnerFactory.getDefault(currentBehaviour, griffonEasybTestType)
                testRunner?.initialize()
            }
        }
    }

    /**
     * Called when a Story or Specification file after is executed.
     * Publishes the event testCaseEnd.
     */
    @Override
    public void stopBehavior(BehaviorStep behaviorStep, Behavior behavior) {
        super.stopBehavior(behaviorStep, behavior)

        eventPublisher.testCaseEnd(trucateEventName(behavior.phrase))

        currentBehaviour = null
        testRunner = null
    }

    /**
     * Called when a Step is about to be executed. A Step is a closure in a easyb file.
     * Publishes the event testStart.
     */
    @Override
    public synchronized void startStep(BehaviorStep behaviorStep) {
        super.startStep(behaviorStep)

        eventPublisher.testStart(trucateEventName(behaviorStep.name))

        currentStep = behaviorStep
        steps.push(behaviorStep)

        switch (behaviorStep.getStepType()) {
            case BehaviorStepType.EXECUTE:
                currentBehaviour.binding.griffonTest = { String name, style = null ->
                    dynamicallyInjectGriffonTest(style, name);
                }

                testRunner?.injectMethods(currentBehaviour.binding)
                break
            case BehaviorStepType.IT:
            case BehaviorStepType.SCENARIO:
                testRunner?.setUp()
                break
        }
    }

    @Override
    public void stopStep() {
        super.stopStep()

        BehaviorStep step = steps.pop()

        switch(step.getStepType()) {
            case BehaviorStepType.IT:
            case BehaviorStepType.SCENARIO:
                testRunner?.tearDown()
                break
        }
    }

    /**
     * Called when a Step has been executed and has a result.
     * Publishes the event testEnd, and also the event testFailure if the result is marked as failed.
     */
    @Override
    public void gotResult(Result result) {
        super.gotResult(result)

        if(result.failed()) {
            if(result.cause) {
                eventPublisher.testFailure(trucateEventName(currentStep.name), result.cause, true)
            } else {
                eventPublisher.testFailure(trucateEventName(currentStep.name), (String)null, true)
            }
        }

        if(result.pending()) pendingCount++
        if(result.failed()) failedCount++
        if(result.ignored()) ignoredCount++
        if(result.inReview()) inReviewCount++

        eventPublisher.testEnd(trucateEventName(currentStep.name))
    }

    /**
     * Called when all easyb files have been executed.
     * Generates the easyb reports.
     */
    @Override
    public void completeTesting() {
        super.completeTesting()

        reportsFactory.produceReports(new ResultsAmalgamator(behaviors as Behavior[]))
    }

    /**
     * Truncate the event name to a maximum, because sometimes easyb story descriptions are too long, and make the
     * command line very difficult to read.
     * @param name the name o the event
     * @return a truncated event name if the event lenght is longer than the max allowed (70 chars default)
     */
    private String trucateEventName(String name) {
        int delimitator = 70 //TODO put this dellimitator in a config ?                   
        if(name.length() <= delimitator) {
            return name
        } else {
            return name.substring(0, delimitator)
        }
    }

    /**
    * we can have situations where we want to replace the test case attached. This happens in the middle of a scenario or specification
    * so we need to make sure we tell the new test runner about the binding
    */
    private void dynamicallyInjectGriffonTest(String name, String style) {
        testRunner = InjectTestRunnerFactory.findDynamicRunner(style, name, currentBehaviour, griffonEasybTestType)

        if(testRunner) {
            testRunner.initialize()
            testRunner.injectMethods(currentBehaviour.binding)

            // we have missed the start of the scenario or specification as well, so we need to inject the setup
            if(currentStep.stepType == BehaviorStepType.IT || currentStep.stepType == BehaviorStepType.SCENARIO) {
                testRunner.setUp()
            }
        }
    }
}
