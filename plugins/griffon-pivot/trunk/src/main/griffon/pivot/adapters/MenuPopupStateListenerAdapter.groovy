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
import org.apache.pivot.wtk.MenuPopup
import org.apache.pivot.wtk.MenuPopupStateListener

/**
 * @author Andres Almiray
 */
class MenuPopupStateListenerAdapter extends BuilderDelegate implements MenuPopupStateListener {
    private Closure onPreviewMenuPopupClose
    private Closure onMenuPopupCloseVetoed
    private Closure onMenuPopupClosed
 
    MenuPopupStateListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onPreviewMenuPopupClose(Closure callback) {
        onPreviewMenuPopupClose = callback
        onPreviewMenuPopupClose.delegate = this
    }

    void onMenuPopupCloseVetoed(Closure callback) {
        onMenuPopupCloseVetoed = callback
        onMenuPopupCloseVetoed.delegate = this
    }

    void onMenuPopupClosed(Closure callback) {
        onMenuPopupClosed = callback
        onMenuPopupClosed.delegate = this
    }

    Vote previewMenuPopupClose(MenuPopup arg0, boolean arg1) {
        if(onPreviewMenuPopupClose) onPreviewMenuPopupClose(arg0, arg1); else Vote.APPROVE
    }

    void menuPopupCloseVetoed(MenuPopup arg0, Vote arg1) {
        if(onMenuPopupCloseVetoed) onMenuPopupCloseVetoed(arg0, arg1)
    }

    void menuPopupClosed(MenuPopup arg0) {
        if(onMenuPopupClosed) onMenuPopupClosed(arg0)
    }
}