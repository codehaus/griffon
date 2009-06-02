/*
 * Copyright 2009 the original author or authors.
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


package griffon.builder.abeilleform

import groovy.swing.SwingBuilder
import griffon.builder.abeilleform.factory.FormPanelFactory



/**
 * @author Jim Shingler <shinglerjim@gmail.com>
 */
public class AbeilleFormBuilder extends SwingBuilder {

  public AbeilleFormBuilder(boolean init = true) {
    super(init)
  }

  // taken from groovy.swing.SwingBuilder
  /*
   public static objectIDAttributeDelegate(def builder, def node, def attributes) {
      def idAttr = builder.getAt(DELEGATE_PROPERTY_OBJECT_ID) ?: DEFAULT_DELEGATE_PROPERTY_OBJECT_ID
      def theID = attributes.remove(idAttr)
      if (theID) {
          builder.setVariable(theID, node)
      }
   }
   */

  def registerAbeilleForm() {
    registerFactory("formPanel", new FormPanelFactory())
  }
}