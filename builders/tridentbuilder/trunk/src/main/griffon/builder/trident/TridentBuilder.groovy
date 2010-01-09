/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.builder.trident

import griffon.builder.trident.factory.*

import org.pushingpixels.trident.*
import org.pushingpixels.trident.ease.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class TridentBuilder extends FactoryBuilderSupport {
   public static final String DELEGATE_PROPERTY_OBJECT_ID = "_delegateProperty:id";
   public static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_ID = "id";

   public TridentBuilder( boolean init = true ) {
      super( init )
      this[DELEGATE_PROPERTY_OBJECT_ID] = DEFAULT_DELEGATE_PROPERTY_OBJECT_ID
   }

   // taken from groovy.swing.SwingBuilder
   public static objectIDAttributeDelegate(def builder, def node, def attributes) {
      def idAttr = builder.getAt(DELEGATE_PROPERTY_OBJECT_ID) ?: DEFAULT_DELEGATE_PROPERTY_OBJECT_ID
      def theID = attributes.remove(idAttr)
      if (theID) {
          builder.setVariable(theID, node)
      }
   }

   def registerTrident() {
      registerFactory("timeline", new TimelineFactory())
      registerFactory("timelineCallback", new TimelineCallbackFactory())
      registerFactory("interpolatedProperty", new InterpolatedPropertyFactory())
      registerFactory("keyFrames", new KeyFramesFactory())
      registerFactory("keyFrame", new KeyFrameFactory())
      registerFactory("linearEase", new EaseFactory(Linear))
      registerFactory("sineEase", new EaseFactory(Sine))
      registerFactory("splineEase", new SplineEaseFactory())
      // scenarios
      registerFactory("timelineScenario",  new TimelineScenarioFactory(TimelineScenario))
      registerFactory("parallelScenario",  new TimelineScenarioFactory(TimelineScenario.Parallel))
      registerFactory("sequenceScenario",  new TimelineScenarioFactory(TimelineScenario.Sequence))
      registerFactory("rendevouzScenario", new TimelineScenarioFactory(TimelineScenario.RendezvousSequence))
      registerFactory("timelineScenarioCallback", new TimelineScenarioCallbackFactory())
      //
      registerFactory("swingRepaintTimeline", new SwingRepaintTimelineFactory())
      registerFactory("timelineRunnable", new TimelineRunnableFactory())
   }
}