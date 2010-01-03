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

package griffon.plugins.easyb

import org.disco.easyb.*
import org.disco.easyb.ant.*
import org.disco.easyb.util.*
import org.disco.easyb.report.*
import org.disco.easyb.listener.*

/**
 * @author Andres Almiray
 */
class BehaviorRunner {
   private reports = []
   private app

   public BehaviorRunner( reports, app ) {
      this.reports = addDefaultReports(reports)
      this.app = app
   }

   /**
    * @param specs collection of files that contain the specifications
    * @throws Exception if unable to write report file
    */
   public void runBehavior(Collection specs) throws Exception {
      BehaviorListener listener = new DefaultListener();

      executeSpecifications(specs, listener);

      System.out.println("\n" +
            // prints "1 behavior run" or "x behaviors run"
            (listener.getBehaviorCount() > 1 ? listener.getBehaviorCount() + " total behaviors run" : "1 behavior run")
            // outer ternary prints either 1..X failure(s) or no failures
            // inner ternary determines if more than one failure and makes it plural if so
            + (listener.getFailedBehaviorCount() > 0 ? " with "
                  + (listener.getFailedBehaviorCount() == 1 ? "1 failure" : listener.getFailedBehaviorCount() + " failures") : " with no failures"));

      produceReports(listener);

      if (listener.getFailedBehaviorCount() > 0) {
         System.exit(-6);
      }
   }
   /**
    * @param listener
    */
   private void produceReports(BehaviorListener listener) {
      reports.each { report ->
         if (report.getFormat().concat(report.getType()).equals(Report.XML_EASYB)) {
            new XmlReportWriter(report, listener).writeReport();
         } else if (report.getFormat().concat(report.getType()).equals(Report.TXT_STORY)) {
            new TxtStoryReportWriter(report, listener).writeReport();
         } else if (report.getFormat().concat(report.getType()).equals(Report.TXT_SPECIFICATION)) {
            new TxtSpecificationReportWriter(report, listener).writeReport();
         }
      }
   }

   private void executeSpecifications(final Collection behaviorFiles, final BehaviorListener listener) throws IOException {
      behaviorFiles.each { behaviorFile ->
         def behavior = null;
         try {
            behavior = BehaviorFactory.createBehavior(behaviorFile);
         } catch(IllegalArgumentException iae) {
            System.out.println(iae.getMessage());
            System.exit(-1);
         }

         long startTime = System.currentTimeMillis();
         System.out.println(
               "Running ${behavior.getPhrase()} ${((behavior instanceof Story) ? ' story' : ' specification')} (${behaviorFile.getName()})"
         );


         BehaviorStep currentStep;
         GroovyShell g = null;
         if (behavior instanceof Story) {
            currentStep = listener.startStep(BehaviorStepType.STORY, behavior.getPhrase());
            g = new GroovyShell(app.class.classLoader,StoryBinding.getBinding(listener));
         } else {
            currentStep = listener.startStep(BehaviorStepType.SPECIFICATION, behavior.getPhrase());
            g = new GroovyShell(app.class.classLoader,SpecificationBinding.getBinding(listener));
         }
         g.getContext().setVariable("app",app);
         g.evaluate(behaviorFile);
         listener.stopStep();

         long endTime = System.currentTimeMillis();

         printMetrics(behavior, startTime, currentStep, endTime);
      }
   }

   private void printMetrics(Behavior behavior, long startTime, BehaviorStep currentStep, long endTime) {
      if(behavior instanceof Story) {
         System.out.println((currentStep.getFailedScenarioCountRecursively() == 0 ? "" : "FAILURE ") +
               "Scenarios run: " + currentStep.getScenarioCountRecursively() +
               ", Failures: " + currentStep.getFailedScenarioCountRecursively() +
               ", Pending: " + currentStep.getPendingScenarioCountRecursively() +
               ", Time Elapsed: " + (endTime - startTime) / 1000f + " sec")
      } else {
         System.out.println((currentStep.getFailedSpecificationCountRecursively() == 0 ? "" : "FAILURE ") +
               "Specifications run: " + currentStep.getSpecificationCountRecursively() +
               ", Failures: " + currentStep.getFailedSpecificationCountRecursively() +
               ", Pending: " + currentStep.getPendingSpecificationCountRecursively() +
               ", Time Elapsed: " + (endTime - startTime) / 1000f + " sec")
      }
   }

   private List addDefaultReports(List userConfiguredReports) {
      def configuredReports = []

      if (userConfiguredReports ) {
         configuredReports.addAll(userConfiguredReports)
      }

      return configuredReports
   }
}
