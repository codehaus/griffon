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

package griffon.builder.css

import groovy.swing.SwingBuilder
import java.awt.Container
import javax.swing.JComponent
import com.feature50.clarity.ClarityConstants
import com.feature50.util.SwingUtils
/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class CSSBuilder extends SwingBuilder {
   public static final String DELEGATE_PROPERTY_CSS_CLASS = "_delegateProperty:cssClass"
   public static final String DEFAULT_DELEGATE_PROPERTY_CSS_CLASS = "cssClass"

   static {
      enhanceSwingClasses()
   }

   public CSSBuilder( boolean init = true ) {
      super( init )
      this[DELEGATE_PROPERTY_CSS_CLASS] = DEFAULT_DELEGATE_PROPERTY_CSS_CLASS
   }

   public void registerCSS() {
      addAttributeDelegate(CSSBuilder.&cssAttributeDelegate)
      addPostNodeCompletionDelegate(CSSBuilder.&cssPostNodeCompletionDelegate)
      registerExplicitMethod("\$", CSSBuilder.&$)
      registerExplicitMethod("\$s", CSSBuilder.&$s)
      registerExplicitMethod("\$\$", CSSBuilder.&$$)
   }

   static cssAttributeDelegate( FactoryBuilderSupport builder, node, Map attributes ) {
      def cssAttr = builder.getAt(DELEGATE_PROPERTY_CSS_CLASS) ?: DEFAULT_DELEGATE_PROPERTY_CSS_CLASS
      builder.getContext()[cssAttr] = attributes.remove(cssAttr)
   }

   static cssPostNodeCompletionDelegate( FactoryBuilderSupport builder, parent, node ) {
      def cssAttr = builder.getAt(DELEGATE_PROPERTY_CSS_CLASS) ?: DEFAULT_DELEGATE_PROPERTY_CSS_CLASS
      def cssClass = builder.getContext()[cssAttr]
      if( cssClass && node instanceof JComponent ) {
         node.putClientProperty(ClarityConstants.CLIENT_PROPERTY_CLASS_KEY, cssClass)
      }
   }

   static enhanceSwingClasses() {
      Class klass = Container
      if( !AbstractSyntheticMetaMethods.hasBeenEnhanced(klass) ) {
         AbstractSyntheticMetaMethods.enhance(klass,[
            "\$": { String name -> CSSBuilder.$(delegate,name) },
            "\$s": { String... names -> CSSBuilder.$s(delegate,names) },
            "\$\$": { String selector -> CSSBuilder.$$(delegate,selector) }
         ])
      }
   }

   static $( Container target, String name ) {
       SwingUtils.getComponentByName(target, name)
   }

   static $s( Container target, String... names ) {
       List<JComponent> components = new ArrayList<JComponent>()

       for (int i = 0; i < names.length; i++) {
           JComponent c = SwingUtils.getComponentByName(target, names[i])
           if( c ) components << c
       }

       components.toArray(new JComponent[components.size()])
   }

   static $$( Container target, String selector ) {
      SwingUtils.parseSelector(selector, SwingUtils.getAllJComponents(target))
   }
}