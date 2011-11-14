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

import griffon.core.ApplicationPhase;
import griffon.core.GriffonApplication;
import griffon.core.ShutdownHandler;
import griffon.util.GriffonNameUtils;
import griffon.util.ConfigUtils;
import groovy.util.ConfigObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

/**
 * Controls a set of windows that belong to the application.
 * <p>
 * Windows that are controlled by a WindowManager can be shown/hidden using a custom strategy ({@code WindowDisplayHandler})
 * 
 * @see griffon.swt.WindowDisplayHandler
 * @author Andres Almiray
 */
public final class WindowManager implements ShutdownHandler {
    private static final Logger LOG = LoggerFactory.getLogger(WindowManager.class);
    private final SWTGriffonApplication app;
    private final WindowHelper windowHelper = new WindowHelper();
    private final ShowHelper showHelper = new ShowHelper();
    private final HideHelper hideHelper = new HideHelper();
    private final Map<String, Shell> windows = new ConcurrentHashMap<String, Shell>();
    
    /**
     * Creates a new WindowManager tied to an specific application.
     * 
     * @param app an application
     */
    public WindowManager(SWTGriffonApplication app) {
        this.app = app;
    }
    
    /**
     * Finds a Window by name.
     * 
     * @param name the value of the name: property
     * @return a Window if a match is found, null otherwise.
     */
    public Shell findWindow(String name) {
        if (!GriffonNameUtils.isBlank(name)) {
            return windows.get(name);
        }
        return null;
    }
    
    public String findWindowName(Shell window) {
        if (window != null) {
            for (Map.Entry<String, Shell> entry : windows.entrySet()) {
                if (entry.getValue() == window) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }
    
    /**
     * Finds the Window that should be displayed during the Ready phase of an application.
     * <p>
     * The WindowManager expects a configuration flag <code>swt.windowManager.startingWindow</code> to be present in order to
     * determine which Window will be displayed during the Ready phase. If no configuration is found the WindowManmager will pick the
     * first Window found in the list of managed windows.
     * <p>
     * The configuration flag accepts two value types:
     * <ul>
     * <li>a String that defines the name of the Window. You must make sure the Window has a matching name property.</li>
     * <li>a Number that defines the index of the Window in the list of managed windows.</li>
     * </ul>
     * 
     * @return a Window that matches the given criteria or null if no match is found.
     */
    public Shell getStartingWindow() {
        Shell window = null;
        Object value = ConfigUtils.getConfigValue(app.getConfig(), "swt.windowManager.startingWindow");
        if (LOG.isDebugEnabled()) {
            LOG.debug("swt.windowManager.startingWindow configured to " + value);
        }
        if (value == null || value instanceof ConfigObject) {
            if (windows.size() > 0) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("No startingWindow configured, selecting the first one in the list of windows");
                }
                window = windows.values().iterator().next();
            }
        } else if (value instanceof String) {
            String windowName = (String) value;
            if (LOG.isDebugEnabled()) {
                LOG.debug("Selecting window " + windowName + " as starting window");
            }
            window = findWindow(windowName);
        } else if (value instanceof Number) {
            int index = ((Number)value).intValue();
            if (index >= 0 && index < windows.size()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Selecting window at index " + index + " as starting window");
                }
                int i = 0;
                for (Iterator<Shell> iter = windows.values().iterator(); iter.hasNext(); i++) {
                    if (i == index) {
                        window = iter.next();
                        break;
                    }
                }
            }
        }
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Starting window is " + window);
        }

        return window;
    }
    
    /**
     * Returns the list of windows managed by this manager.
     * 
     * @return a List of currently managed windows
     */
    public Collection<Shell> getWindows() {
        return Collections.<Shell> unmodifiableCollection(windows.values());
    }
    
    /**
     * Registers a window on this manager if an only if the window is not null
     * and it's not registered already.
     * 
     * @param window the window to be added to the list of managed windows
     */
    public void attach(String name, Shell window) {
        if (window == null || windows.values().contains(window)) {
            return;
        }
        window.addShellListener(windowHelper);
        window.addListener(SWT.Show, showHelper);
        window.addListener(SWT.Hide, hideHelper);
        windows.put(name, window);
    }
    
    /**
     * Removes the window from the list of manages windows if and only if it
     * is registered with this manager.
     * 
     * @param window the window to be removed
     */
    public void detach(Shell window) {
        if (window == null) {
            return;
        }
        if (windows.values().contains(window)) {
            window.removeShellListener(windowHelper);
            window.removeListener(SWT.Show, showHelper);
            window.removeListener(SWT.Hide, hideHelper);
            windows.remove(findWindowName(window));
        }
    }
    
    /**
     * Shows the window.
     * <p>
     * This method is executed <b>SYNCHRONOUSLY</b> in the UI thread.
     * 
     * @param window the window to show
     */
    public void show(final Shell window) {
        if (window == null) {
            return;
        }
        app.execSync(new Runnable() {
            public void run() {
                app.resolveWindowDisplayHandler().show(window, app);
            }
        });
    }
    
    /**
     * Hides the window.
     * <p>
     * This method is executed <b>SYNCHRONOUSLY</b> in the UI thread.
     * 
     * @param window the window to hide
     */
    public void hide(final Shell window) {
        if (window == null) {
            return;
        }
        app.execSync(new Runnable() {
            public void run() {
                app.resolveWindowDisplayHandler().hide(window, app);
            }
        });
    }
    
    public boolean canShutdown(GriffonApplication app) {
        return true;
    }
    
    /**
     * Hides all visible windows
     */
    public void onShutdown(GriffonApplication app) {
        for (Shell window : windows.values()) {
            if (!window.isDisposed() && window.isVisible()) {
                hide(window);
            }
        }
    }
    
    public void handleClose(Widget widget) {
       if (app.getPhase() == ApplicationPhase.SHUTDOWN) {
            return;
        }
        int visibleWindows = 0;
        for (Shell window : windows.values()) {
            if (!window.isDisposed() && window.isVisible()) {
                visibleWindows++;
            }
        }
        
        Boolean autoShutdown = (Boolean)app.getConfig().flatten().get("application.autoShutdown");
        if (visibleWindows <= 1 && autoShutdown != null && autoShutdown.booleanValue()) {
            if (!app.shutdown())
                show((Shell) widget);
        }
    }
    
    /**
     * WindowAdapter that invokes hide() when the window is about to be closed.
     * 
     * @author Andres Almiray
     */
    private class WindowHelper extends ShellAdapter {
        public void shellClosed(ShellEvent event) {
            hide((Shell)event.getSource());
            handleClose((Shell)event.getSource());
        }
    }
    
    /**
     * Listener that triggers application events when a window is shown.
     * 
     * @author Andres Almiray
     */
    private class ShowHelper implements Listener {
        /**
         * Triggers a <tt>WindowShown</tt> event with the window as sole argument
         */
        public void handleEvent(Event event) {
            app.event(GriffonApplication.Event.WINDOW_SHOWN.getName(), Arrays.asList(event.widget));
        }
    }
    
    /**
     * Listener that triggers application events when a window is hidden.
     * 
     * @author Andres Almiray
     */
    private class HideHelper implements Listener {
        /**
         * Triggers a <tt>WindowHidden</tt> event with the window as sole argument
         */
        public void handleEvent(Event event) {
            app.event(GriffonApplication.Event.WINDOW_HIDDEN.getName(), Arrays.asList(event.widget));
        }
    }
}
