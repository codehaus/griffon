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

import static griffon.util.GriffonExceptionHandler.sanitize;
import griffon.application.StandaloneGriffonApplication;
import griffon.core.UIThreadManager;
import griffon.util.GriffonExceptionHandler;
import griffon.util.UIThreadHandler;

import java.lang.reflect.InvocationTargetException;

import org.codehaus.griffon.runtime.core.AbstractGriffonApplication;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Basic implementation of {@code GriffonApplication} that runs in standalone mode using SWT.
 * 
 * @author Andres Almiray
 */
public abstract class AbstractSWTGriffonApplication extends AbstractGriffonApplication implements SWTGriffonApplication,
        StandaloneGriffonApplication {
    private final WindowManager windowManager;
    private WindowDisplayHandler windowDisplayHandler;
    private final WindowDisplayHandler defaultWindowDisplayHandler = new ConfigurableWindowDisplayHandler();
    @SuppressWarnings("rawtypes")
    private static final Class[] CTOR_ARGS = new Class[] {String[].class};
    protected final Display defaultDisplay;
    
    public AbstractSWTGriffonApplication() {
        this(AbstractSWTGriffonApplication.EMPTY_ARGS);
    }
    
    public AbstractSWTGriffonApplication(String[] args) {
        super(args);
        defaultDisplay = Display.getDefault();
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
        return new SWTUIThreadHandler();
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
        Shell startingWindow = windowManager.getStartingWindow();
        windowManager.show(startingWindow);
        callReady(startingWindow);
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
        final Shell[] shell = new Shell[1];
        UIThreadManager.getInstance().executeSync(new Runnable() {
            public void run() {
                shell[0] = new Shell(defaultDisplay, SWT.BORDER | SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.TITLE);
            }
        });
        return shell[0];
    }
    
    /**
     * Calls the ready lifecycle method after the UI thread calms down
     */
    protected void callReady(Shell startingWindow) {
        ready();
        while (!startingWindow.isDisposed()) {
            if (!defaultDisplay.readAndDispatch()) {
                defaultDisplay.sleep();
            }
        }
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void run(Class applicationClass, String[] args) {
        GriffonExceptionHandler.registerExceptionHandler();
        StandaloneGriffonApplication app = null;
        try {
            app = (StandaloneGriffonApplication)applicationClass.getDeclaredConstructor(CTOR_ARGS).newInstance(new Object[] {args});
            app.bootstrap();
            app.realize();
            app.show();
        } catch (InstantiationException e) {
            sanitize(e).printStackTrace();
        } catch (IllegalAccessException e) {
            sanitize(e).printStackTrace();
        } catch (InvocationTargetException e) {
            sanitize(e).printStackTrace();
        } catch (NoSuchMethodException e) {
            sanitize(e).printStackTrace();
        }
    }
}
