/*
 * Copyright 2010 the original author or authors.
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

package griffon.plugins.ratpack

import javax.servlet.http.HttpServletRequest
import com.bleedingwolf.ratpack.TemplateRenderer

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import groovy.text.SimpleTemplateEngine

/**
 * @author Andres Almiray
 */
class GriffonTemplateRenderer extends TemplateRenderer {
     private static final Logger log = LoggerFactory.getLogger(GriffonTemplateRenderer)
     
     GriffonTemplateRenderer(String templateRoot) {
         super(templateRoot)
     } 
     
     String render(templateName, Map context = [:]) {
         String text = ''
         String fullTemplateFilename = [templateRoot, templateName].join('/')
         log.trace("template -> $fullTemplateFilename")
        
         try {
             text += loadResource(fullTemplateFilename).text
         } catch(java.io.IOException ex) {
             text += loadResource('com/bleedingwolf/ratpack/exception.html').text
             context = [
                  title: 'Template Not Found',
                  message: 'Template Not Found',
                  metadata: [
                      'Template Name': templateName,
                  ],
                  stacktrace: ""
             ]
         }

         renderTemplate(text, context)
     }

     String renderError(Map context) {
         String text = loadResource('com/bleedingwolf/ratpack/exception.html').text

         renderTemplate(text, context)
     }

     String renderException(Throwable ex, HttpServletRequest req) {
         def stackInfo = super.decodeStackTrace(ex)
                  
         String text = loadResource('com/bleedingwolf/ratpack/exception.html').text
         Map context = [
             title: ex.class.name,
             message: ex.message,
             metadata: [
                'Request Method': req.method.toUpperCase(),
                'Request URL': req.requestURL,
                'Exception Type': ex.class.name,
                'Exception Location': "${stackInfo.rootCause.fileName}, line ${stackInfo.rootCause.lineNumber}",
             ],
             stacktrace: stackInfo.html
         ]
         
         renderTemplate(text, context)
     }

    private String renderTemplate(String text, Map context) {
         SimpleTemplateEngine engine = new SimpleTemplateEngine()
         def template = engine.createTemplate(text).make(context)
         return template.toString()
    }

    private InputStream loadResource(String path) {
        Thread.currentThread().contextClassLoader.getResourceAsStream(path)
    }
 }
