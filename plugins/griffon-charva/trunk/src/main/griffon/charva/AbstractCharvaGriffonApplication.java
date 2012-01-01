/*
 * Copyright 2008-2011 the original author or authors.
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
package griffon.charva;

import griffon.application.StandaloneGriffonApplication;
import griffon.core.UIThreadManager;
import griffon.util.GriffonExceptionHandler;
import griffon.util.UIThreadHandler;
import org.codehaus.griffon.runtime.core.AbstractGriffonApplication;

import charva.awt.*;
import charvax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Basic implementation of {@code GriffonApplication} that runs in standalone/webstart mode using Charva.
 *
 * @author Andres Almiray
 */
public abstract class AbstractCharvaGriffonApplication extends AbstractGriffonApplication implements CharvaGriffonApplication, StandaloneGriffonApplication {
    private final WindowManager windowManager;
    private WindowDisplayHandler windowDisplayHandler;
    private final WindowDisplayHandler defaultWindowDisplayHandler = new ConfigurableWindowDisplayHandler();
    private static final Class[] CTOR_ARGS = new Class[]{String[].class};

    public AbstractCharvaGriffonApplication() {
        this(AbstractCharvaGriffonApplication.EMPTY_ARGS);
    }

    public AbstractCharvaGriffonApplication(String[] args) {
        super(args);
        windowManager = new WindowManager(this);
        UIThreadManager.getInstance().setUIThreadHandler(getUIThreadHandler());
        addShutdownHandler(windowManager);
    }

    public WindowManager getWindowManager() {
        return windowManager;
    }

    public WindowDisplayHandler getWindowDisplayHandler() {
        return windowDisplayHandler;
    }

    public void setWindowDisplayHandler(WindowDisplayHandler windowDisplayHandler) {
        this.windowDisplayHandler = windowDisplayHandler;
    }

    protected UIThreadHandler getUIThreadHandler() {
        return new CharvaUIThreadHandler();
    }

    public final WindowDisplayHandler resolveWindowDisplayHandler() {
        return windowDisplayHandler != null ? windowDisplayHandler : defaultWindowDisplayHandler;
    }

    public void bootstrap() {
        initialize();
    }

    public void realize() {
        startup();
    }

    public void show() {
        windowManager.show(windowManager.getStartingWindow());
        callReady();
    }

    public boolean shutdown() {
        if (super.shutdown()) {
            exit();
        }
        return false;
    }

    public void exit() {
        System.exit(0);
    }

    public Object createApplicationContainer() {
        Window window = CharvaUtils.createApplicationFrame(this);
        windowManager.attach(window);
        return window;
    }

    /**
     * Calls the ready lifecycle method after the UI thread calms down
     */
    protected void callReady() {
        // wait for EDT to empty out.... somehow
        final boolean[] empty = {false};
        while (true) {
            UIThreadManager.getInstance().executeSync(new Runnable() {
                public void run() {
                    empty[0] = Toolkit.getDefaultToolkit().getSystemEventQueue().queuesAreEmpty();
                }
            });
            if (empty[0]) break;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // ignore
            }
        }

        ready();
    }

    public static void run(Class applicationClass, String[] args) {
        GriffonExceptionHandler.registerExceptionHandler();
        StandaloneGriffonApplication app = null;
        try {
            app = (StandaloneGriffonApplication) applicationClass.getDeclaredConstructor(CTOR_ARGS).newInstance(new Object[]{args});
            app.bootstrap();
            app.realize();
            app.show();
        } catch (InstantiationException e) {
            GriffonExceptionHandler.sanitize(e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            GriffonExceptionHandler.sanitize(e);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            GriffonExceptionHandler.sanitize(e);
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            GriffonExceptionHandler.sanitize(e);
            e.printStackTrace();
        }
    }
}
