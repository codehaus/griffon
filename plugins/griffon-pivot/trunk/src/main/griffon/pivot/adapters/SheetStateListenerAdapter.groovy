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
import org.apache.pivot.wtk.Sheet
import org.apache.pivot.wtk.SheetStateListener

/**
 * @author Andres Almiray
 */
class SheetStateListenerAdapter extends BuilderDelegate implements SheetStateListener {
    private Closure onPreviewSheetClose
    private Closure onSheetCloseVetoed
 
    SheetStateListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onPreviewSheetClose(Closure callback) {
        onPreviewSheetClose = callback
        onPreviewSheetClose.delegate = this
    }

    void onSheetCloseVetoed(Closure callback) {
        onSheetCloseVetoed = callback
        onSheetCloseVetoed.delegate = this
    }

    Vote previewSheetClose(Sheet arg0, boolean arg1) {
        if(onPreviewSheetClose) onPreviewSheetClose(arg0, arg1); else Vote.APPROVE
    }

    void sheetCloseVetoed(Sheet arg0, Vote arg1) {
        if(onSheetCloseVetoed) onSheetCloseVetoed(arg0, arg1)
    }
}