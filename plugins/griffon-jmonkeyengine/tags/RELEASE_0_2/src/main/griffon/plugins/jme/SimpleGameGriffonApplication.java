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

package griffon.plugins.jme;

import java.util.Map;
import java.util.LinkedHashMap;
import com.jme3.system.AppSettings;
import org.codehaus.griffon.runtime.core.AbstractGriffonApplication;
import griffon.swing.AbstractSwingGriffonApplication;
import griffon.util.ConfigUtils;

/**
 * 
 * @author Andres Almiray
 */
public class SimpleGameGriffonApplication extends AbstractSwingGriffonApplication implements GriffonGameApplication {
    private boolean gameDispensed = false;
    
    private final GriffonGameDelegate game;

    public SimpleGameGriffonApplication() {
        this(AbstractGriffonApplication.EMPTY_ARGS);
    }

    public SimpleGameGriffonApplication(String[] args) {
        super(args);
        game = new GriffonGameDelegate(this);
        game.setShowSettings(false);
    }
    
    public GriffonGameDelegate getGame() {
        return game;
    }

    public void realize() {
        super.realize();
        
        AppSettings appSettings = new AppSettings(true);
        Map settings = (Map) ConfigUtils.getConfigValue(getConfig(), "jme.settings");
        if(settings == null || settings.isEmpty()) {
            settings = new LinkedHashMap();
            settings.put("Title", ConfigUtils.getConfigValue(getConfig(), "application.title"));
        }
        appSettings.putAll(settings); 
        
        game.setSettings(appSettings);
    }

    public void show() {
        Object value = ConfigUtils.getConfigValue(getConfig(), "jme.settings.show");
        boolean showSettings = value instanceof Boolean? ((Boolean) value).booleanValue() : false;
        game.setShowSettings(showSettings);
        
        game.start();
        callReady();
    }
    
    public void exit() {
        game.quit();
        super.exit();
    }

    public Object createApplicationContainer() {
        if(gameDispensed) {
            return super.createApplicationContainer();
        } else {
            gameDispensed = true;
            return game;
        }
    }
    
    public static void main(String[] args) {
        AbstractSwingGriffonApplication.run(SimpleGameGriffonApplication.class, args);
    }
}
