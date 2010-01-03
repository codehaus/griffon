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

package griffon.pivot.adapters
 
import griffon.pivot.impl.BuilderDelegate
import org.apache.pivot.util.Vote
import org.apache.pivot.wtk.Dialog
import org.apache.pivot.wtk.DialogStateListener

/**
 * @author Andres Almiray
 */
class DialogStateListenerAdapter extends BuilderDelegate implements DialogStateListener {
    private Closure onPreviewDialogClose
    private Closure onDialogCloseVetoed
    private Closure onDialogClosed
 
    DialogStateListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onPreviewDialogClose(Closure callback) {
        onPreviewDialogClose = callback
        onPreviewDialogClose.delegate = this
    }

    void onDialogCloseVetoed(Closure callback) {
        onDialogCloseVetoed = callback
        onDialogCloseVetoed.delegate = this
    }

    void onDialogClosed(Closure callback) {
        onDialogClosed = callback
        onDialogClosed.delegate = this
    }

    Vote previewDialogClose(Dialog arg0, boolean arg1) {
        if(onPreviewDialogClose) onPreviewDialogClose(arg0, arg1); else Vote.APPROVE
    }

    void dialogCloseVetoed(Dialog arg0, Vote arg1) {
        if(onDialogCloseVetoed) onDialogCloseVetoed(arg0, arg1)
    }
 
    void dialogClosed(Dialog arg0, boolean arg1) {
        if(onDialogClosed) onDialogClosed(arg0, arg1)
    }
}
