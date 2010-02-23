/*
 * Copyright 2008-2010 the original author or authors.
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
 * See the License for the specific language govnerning permissions and
 * limitations under the License.
 */

package griffon.pivot

import griffon.core.BaseGriffonApplication
import griffon.util.GriffonApplicationHelper
import griffon.util.UIThreadHelper

import org.apache.pivot.collections.Map as PivotMap
import org.apache.pivot.wtk.Application
import org.apache.pivot.wtk.ApplicationContext
import org.apache.pivot.wtk.Display
import org.apache.pivot.wtk.Alert
import org.apache.pivot.wtk.Component
import org.apache.pivot.wtk.DialogCloseListener
import org.apache.pivot.wtk.MessageType
import org.apache.pivot.wtk.Prompt
import org.apache.pivot.wtk.SheetCloseListener
import org.apache.pivot.wtk.BoxPane
import org.apache.pivot.wtk.media.Image

/**
 * @author Andres.Almiray
 */
abstract class AbstractPivotApplication implements Application {
    @Delegate private final BaseGriffonApplication _base
    protected Display display

    AbstractPivotApplication() {
        _base = new BaseGriffonApplication(this)
        UIThreadHelper.instance.setUIThreadHandler(new PivotUIThreadHandler())
        loadApplicationProperties()
    }

    void startup(Display display, PivotMap<String, String> properties) {
        this.display = display
        bootstrap()
        realize()
        show()
    }

    // ------------------------------------------

    boolean shutdown(boolean optional) {
        shutdown()
        false
    }

    void suspend() {
        event("AppSuspend", [this])
    }

    void resume() {
        event("AppResume", [this])
    }

    // ------------------------------------------

    void alert(Map args) {
        Map params = [type: MessageType.INFO, owner: (windows.size()?windows[0]:null), message: ''] + args
        if(!params.display && !params.owner) {
            throw new IllegalArgumentException("In alert() you must define a value for either display: or owner:")
        }

        List realArgs = [params.type, params.message]
        if(params.body && !(params.body instanceof Component)) {
            throw new IllegalArgumentException("In alert() you must define a value body: of type ${Component.class.name}")
        }
        BoxPane realBody = new BoxPane()
        if(params.body) realBody.add(params.body)
        realArgs << realBody
        realArgs << (params.display ? params.display : params.owner)
        realArgs << params.listener instanceof DialogCloseListener ? params.listener : (params.listener as DialogCloseListener)
        Alert.alert(*realArgs) 
    }

    void prompt(Map args) {
        Map params = [type: MessageType.INFO, owner: (windows.size()?windows[0]:null), message: ''] + args
        if(!params.display && !params.owner) {
            throw new IllegalArgumentException("In prompt() you must define a value for either display: or owner:")
        }

        List realArgs = [params.type, params.message]
        if(params.body && !(params.body instanceof Component)) {
            throw new IllegalArgumentException("In prompt() you must define a value body: of type ${Component.class.name}")
        }
        BoxPane realBody = new BoxPane()
        if(params.body) realBody.add(params.body)
        realArgs << realBody
        realArgs << (params.display ? params.display : params.owner)
        realArgs << params.listener instanceof SheetCloseListener ? params.listener : (params.listener as SheetCloseListener)
        Prompt.prompt(*realArgs) 
    }

    // ------------------------------------------

    void schedule(long delay, Closure callback) {
        ApplicationContext.scheduleCallback(callback, delay)
    }

    void scheduleRecurring(long period, Closure callback) {
        ApplicationContext.scheduleRecurringCallback(callback, period)
    }

    void scheduleRecurring(long delay, long period, Closure callback) {
        ApplicationContext.scheduleRecurringCallback(callback, delay, period)
    }

    void queue(boolean wait = false, Closure callback) {
        ApplicationContext.queueCallback(callback, wait)
    }

    ApplicationContext.ResourceCacheDictionary getResourceCache() {
        ApplicationContext.getResourceCache()
    }

    Image loadImage(URL imageURL) {
        Image.load(imageURL)
    }

    // ------------------------------------------

    public void ready() {
        event("ReadyStart",[this])
        GriffonApplicationHelper.runScriptInsideUIThread("Ready", this)
        event("ReadyEnd",[this])
    }

    public void shutdown() {
        _base.shutdown()
    }
}
