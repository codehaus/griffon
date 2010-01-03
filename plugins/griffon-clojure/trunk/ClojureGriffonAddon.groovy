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

import griffon.util.IGriffonApplication
import clojure.lang.Compiler
import griffon.clojure.ClojureProxy
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

/**
 * @author Andres.Almiray
 */
class ClojureGriffonAddon {
   private final ClojureProxy CLJ_PROXY = new ClojureProxy()
   private String clojurePropertyName
   private IGriffonApplication application

   def addonInit = { app ->
      application = app
      app.addApplicationEventListener(this)
      clojurePropertyName = app.config.griffon?.clojure?.dynamicPropertyName ?: "clj"
      if(clojurePropertyName) {
          clojurePropertyName = clojurePropertyName[0].toUpperCase() + clojurePropertyName[1..-1]
      } else {
          clojurePropertyName = "Clj"
      }
   }

   def onBootstrapEnd = { app ->
      loadSources("classpath*:/clj/**/*.clj")
   }

   def onNewInstance = { klass, type, instance ->
      def types = application.config.griffon?.clojure?.injectInto ?: ["controller"]
      if(!types.contains(type)) return
      instance.metaClass."get${clojurePropertyName}" = { CLJ_PROXY }
      instance.metaClass."${clojurePropertyName[0].toLowerCase() + clojurePropertyName[1..-1]}Load" = loadSources
   }

   private loadSources = { String path ->
      def pathResolver = new PathMatchingResourcePatternResolver(application.class.classLoader)
      application.class.classLoader.loadClass("clojure.lang.Compiler")
      pathResolver.getResources(path).each {
         it.getURL().withReader { reader ->
            Compiler.load reader
         }
      }
   }
}
