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
import java.awt.Font
import org.apache.pivot.wtk.HorizontalAlignment
import org.apache.pivot.wtk.media.drawing.Text
import org.apache.pivot.wtk.media.drawing.TextListener

/**
 * @author Andres Almiray
 */
class TextListenerAdapter extends BuilderDelegate implements TextListener {
    private Closure onTextChanged
    private Closure onAlignmentChanged
    private Closure onFontChanged
    private Closure onWidthChanged
 
    TextListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onTextChanged(Closure callback) {
        onTextChanged = callback
        onTextChanged.delegate = this
    }

    void onAlignmentChanged(Closure callback) {
        onAlignmentChanged = callback
        onAlignmentChanged.delegate = this
    }

    void onFontChanged(Closure callback) {
        onFontChanged = callback
        onFontChanged.delegate = this
    }

    void onWidthChanged(Closure callback) {
        onWidthChanged = callback
        onWidthChanged.delegate = this
    }

    void textChanged(Text arg0, String arg1) {
        if(onTextChanged) onTextChanged(arg0, arg1)
    }

    void alignmentChanged(Text arg0, HorizontalAlignment arg1) {
        if(onAlignmentChanged) onAlignmentChanged(arg0, arg1)
    }

    void fontChanged(Text arg0, Font arg1) {
        if(onFontChanged) onFontChanged(arg0, arg1)
    }

    void widthChanged(Text arg0, int arg1) {
        if(onWidthChanged) onWidthChanged(arg0, arg1)
    }
}