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

package griffon.pivot.adapters
 
import griffon.pivot.impl.BuilderDelegate
import org.apache.pivot.util.Vote
import org.apache.pivot.wtk.Display
import org.apache.pivot.wtk.Window
import org.apache.pivot.wtk.WindowStateListener

/**
 * @author Andres Almiray
 */
class WindowStateListenerAdapter extends BuilderDelegate implements WindowStateListener {
    private Closure onPreviewWindowOpen
    private Closure onWindowOpenVetoed
    private Closure onPreviewWindowClose
    private Closure onWindowCloseVetoed
    private Closure onWindowClosed
    private Closure onWindowOpened
 
    WindowStateListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onPreviewWindowOpen(Closure callback) {
        onPreviewWindowOpen = callback
        onPreviewWindowOpen.delegate = this
    }

    void onWindowOpenVetoed(Closure callback) {
        onWindowOpenVetoed = callback
        onWindowOpenVetoed.delegate = this
    }

    void onPreviewWindowClose(Closure callback) {
        onPreviewWindowClose = callback
        onPreviewWindowClose.delegate = this
    }

    void onWindowCloseVetoed(Closure callback) {
        onWindowCloseVetoed = callback
        onWindowCloseVetoed.delegate = this
    }

    void onWindowClosed(Closure callback) {
        onWindowClosed = callback
        onWindowClosed.delegate = this
    }

    void onWindowOpened(Closure callback) {
        onWindowOpened = callback
        onWindowOpened.delegate = this
    }

    Vote previewWindowOpen(Window arg0, Display arg1) {
        if(onPreviewWindowOpen) onPreviewWindowOpen(arg0, arg1); else Vote.APPROVE
    }

    void windowOpenVetoed(Window arg0, Vote arg1) {
        if(onWindowOpenVetoed) onWindowOpenVetoed(arg0, arg1)
    }

    Vote previewWindowClose(Window arg0) {
        if(onPreviewWindowClose) onPreviewWindowClose(arg0); else Vote.APPROVE
    }

    void windowCloseVetoed(Window arg0, Vote arg1) {
        if(onWindowCloseVetoed) onWindowCloseVetoed(arg0, arg1)
    }

    void windowClosed(Window arg0, Display arg1, Window arg2) {
        if(onWindowClosed) onWindowClosed(arg0, arg1, arg2)
    }

    void windowOpened(Window arg0) {
        if(onWindowOpened) onWindowOpened(arg0)
    }
}