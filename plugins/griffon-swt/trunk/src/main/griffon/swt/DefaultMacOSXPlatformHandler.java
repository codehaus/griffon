/*
 * Copyright 2009-2011 the original author or authors.
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

package griffon.swt;

import static java.util.Arrays.asList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;

import griffon.core.GriffonApplication;
import griffon.swt.support.CocoaUIEnhancer;
import griffon.util.ConfigUtils;
import griffon.util.GriffonNameUtils;
import griffon.util.PlatformHandler;
import griffon.util.RunnableWithArgs;

/**
 * @author Andres Almiray
 */
public class DefaultMacOSXPlatformHandler implements PlatformHandler {    
    @Override
    public void handle(final GriffonApplication app) {
        final AbstractSWTGriffonApplication self = (AbstractSWTGriffonApplication) app;
        final Listener quitHandler = new Listener() {
            public void handleEvent(org.eclipse.swt.widgets.Event e) {
                e.doit = false;
                boolean skipQuit = ConfigUtils.getConfigValueAsBoolean(self.getConfig(), "osx.noquit", false);
                if(skipQuit) {
                    app.event("OSXQuit", asList(self)); 
                } else {
                    self.getWindowManager().handleClose(e.widget);
                } 
            }
        };
        final RunnableWithArgs aboutHandler = new RunnableWithArgs() {
            public void run(Object[] args) {
                boolean skipAbout = ConfigUtils.getConfigValueAsBoolean(self.getConfig(), "osx.noabout", false);
                if(!skipAbout) app.event("OSXAbout", asList(self));
            }           
        };
        final RunnableWithArgs prefsHandler = new RunnableWithArgs() {
            public void run(Object[] args) {
                boolean skipPrefs = ConfigUtils.getConfigValueAsBoolean(self.getConfig(), "osx.noprefs", false);
                if(!skipPrefs) app.event("OSXPres", asList(self));
            }           
        };
        
        String applicationName = GriffonNameUtils.capitalize(ConfigUtils.getConfigValueAsString(self.getConfig(), "application.title", "Griffon"));
        new CocoaUIEnhancer(applicationName).hookApplicationMenu(Display.getDefault(), quitHandler, aboutHandler, prefsHandler);    
    }   
}
