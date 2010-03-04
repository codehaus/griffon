/*
 * Copyright 2009 the original author or authors.
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
 
import java.util.ArrayList
import com.google.inject.*
import griffon.core.BaseGriffonApplication
 
 /**
  * @author jwill
  */
class GuiceGriffonAddon {
	private boolean modulesLoaded = false
    private modules = new ArrayList<Module>()
    
    def addonInit(app) {
    
    }
    
    def addonPostInit = { app ->
	   	def controllers = app.artifactManager.controllerClasses
		getModules()
	
		def injector = (modules.size() == 0) ? Guice.createInjector() : Guice.createInjector(modules)
		controllers.each { c ->
				
				def membersInjector = injector.getMembersInjector(c)
				c.metaClass.injector = membersInjector
			}
	
    }
    
    // ===============================================
    def getModules = {
        if (modulesLoaded) return
        
        def map = app.config.guice
        if (map) {
			map.modules?.each {
				def moduleClass = getClass().classLoader.loadClass(it)
				modules.add(moduleClass.newInstance())
			}
			
        }
		modulesLoaded = true
    }
}
