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

// import clojure.lang.Compiler
import griffon.core.GriffonApplication
import griffon.clojure.ClojureProxy
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

/**
 * @author Andres.Almiray
 */
class ClojureGriffonAddon {
   private final ClojureProxy CLJ_PROXY = new ClojureProxy()
   private String clojurePropertyName

   def addonInit = { app ->
      clojurePropertyName = app.config.griffon?.clojure?.dynamicPropertyName ?: 'clj'
      if(clojurePropertyName) {
          clojurePropertyName = clojurePropertyName[0].toUpperCase() + clojurePropertyName[1..-1]
      } else {
          clojurePropertyName = 'Clj'
      }
   }

   def events = [
      BootstrapEnd: { app ->
          loadSources(app, 'classpath*:/clj/**/*.clj')
      },
      NewInstance: { klass, type, instance ->
         def types = app.config.griffon?.clojure?.injectInto ?: ['controller']
         if(!types.contains(type)) return
         instance.metaClass."get${clojurePropertyName}" = { CLJ_PROXY }
         instance.metaClass."${clojurePropertyName[0].toLowerCase() + clojurePropertyName[1..-1]}Load" = loadSources.curry(app)
      }
   ]

   private loadSources = { GriffonApplication app, String path ->
      def pathResolver = new PathMatchingResourcePatternResolver(app.class.classLoader)
      Class compilerClass = app.class.classLoader.loadClass('clojure.lang.Compiler')
      pathResolver.getResources(path).each {
         it.getURL().withReader { reader ->
            compilerClass.load reader
         }
      }
   }
}
