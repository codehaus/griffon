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

package griffon.builder.wizard.factory

import java.awt.image.BufferedImage
import javax.swing.UIManager
import javax.imageio.ImageIO

import org.netbeans.spi.wizard.Wizard
import org.netbeans.spi.wizard.WizardPage
import griffon.builder.wizard.impl.GriffonWizardPage
import griffon.builder.wizard.impl.GriffonWizardPanelProvider
import griffon.builder.wizard.impl.WizardResultProducerImpl

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class WizardFactory extends AbstractFactory {
   static final Map PAGES_PER_WIZARD = new WeakHashMap()

   static {
      Wizard.metaClass.initPages = {
         WizardFactory.PAGES_PER_WIZARD[delegate].each { page ->
            if( page instanceof GriffonWizardPage ) page.init()
         }
      }
   }

   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      GriffonWizardPanelProvider wpp = null
      if( value instanceof String || value instanceof GString ) {
         value = value.toString()
         if( !value.endsWith("WizardPanelProvider") ) value += "WizardPanelProvider"
         def wppd = (value as Class).newInstance()
         wpp = new GriffonWizardPanelProvider(wppd, builder)
      }

      String title = attributes.remove("title") ?: "Wizard"
      def pages = attributes.remove("pages")
      if( !wpp ) {
        if( !pages ) {
            throw new RuntimeException("In $name you must define a value for pages: with either a List of GriffonWizardPage instances or page names")
        }

        if( pages instanceof List ) {
            if ( pages.every{ it instanceof String } ) {
                pages = pages.collect { n ->
                if( !n.endsWith("WizardPage") ) n += "WizardPage"
                def pd = (n as Class).newInstance()
                GriffonWizardPage page = new GriffonWizardPage(pd, builder)
                return page
                } as WizardPage[]
            } else {
                throw new RuntimeException("In $name you must define a value for pages: as a List of page names")
            }
        }
      }

      handleWizardImage(builder, name, attributes)

      def resultProducer = attributes.remove("resultProducer")
      if( resultProducer != null ) {
         if( resultProducer instanceof Map && resultProducer.cancel && resultProducer.finish ) {
            resultProducer = resultProducer as WizardPage.WizardResultProducer
         } else if ( !(resultProducer instanceof WizardPage.WizardResultProducer) ){
            throw new RuntimeException("In $name value of resultProducer: must be an instance of WizardPage.WizardResultProducer or a Map with cancel: and finish: attributes")
         }
         builder.context.resultProducer = resultProducer
      } else {
         builder.context.resultProducerImpl = new WizardResultProducerImpl(builder)
         resultProducer = builder.context.resultProducerImpl
      }

      if( wpp ){
         builder.context.wpp = wpp
         return wpp.createWizard()
      }
      Wizard wizard = WizardPage.createWizard(title, pages, resultProducer)
      PAGES_PER_WIZARD[wizard] = pages
      return wizard
   }

   public boolean isHandlesNodeChildren() {
      return true
   }

   public boolean onNodeChildren( FactoryBuilderSupport builder, Object node, Closure childContent ) {
      if( builder.context.resultProducer ) {
         throw new RuntimeException("In ${builder.currentName} you can not define a nested resultProducer if one was set as a property already.")
      }
      builder.context.resultProducerImpl.delegate = childContent.delegate
      childContent.delegate = builder.context.resultProducerImpl
      childContent()
      builder.context.wpp.resultProducer = builder.context.resultProducerImpl
      return false
   }

   private void handleWizardImage( FactoryBuilderSupport builder, Object name, Map attributes ) {
      def value = null

      if (attributes.containsKey("url")) {
         value = attributes.remove("url")
         if (!(value instanceof URL)) {
            throw new RuntimeException("In $name url: attributes must be of type java.net.URL")
         }
      } else if (attributes.containsKey("file")) {
         value = attributes.remove("file")
         if (value instanceof File) {
            value = value.toURI().toURL()
         } else if (value instanceof String) {
            value = new File(value).toURI().toURL()
         } else {
            throw new RuntimeException("In $name file: attributes must be of type java.io.File or a string")
         }
      } else if (attributes.containsKey("inputStream")) {
         value = attributes.remove("inputStream")
         if (!(value instanceof InputStream)) {
            throw new RuntimeException("In $name inputStream: attributes must be of type java.io.InputStream")
         }
      } else if (attributes.containsKey("image")) {
         value = attributes.remove("image")
         if (!(value instanceof BufferedImage)) {
            throw new RuntimeException("In $name image: attributes must be of type java.awt.image.BufferedImage")
         }
      } else if (attributes.containsKey("resource")) {
         def resource = attributes.remove("resource")
         def klass = builder.context.owner
         def origValue = value
         if (attributes.containsKey("class")) {
            klass = attributes.remove("class")
         }
         if (klass == null) {
            klass = WizardFactory
         } else if (!(klass instanceof Class)) {
            klass = klass.class
         }
         value = klass.getResource(resource)
      }

      if (value != null) {
         if( !(value instanceof BufferedImage) ) {
            value = ImageIO.read(value)
         }
         UIManager.put("wizard.sidebar.image", value)
      }
   }
}