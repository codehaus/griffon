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
import org.apache.pivot.wtk.Component
import org.apache.pivot.wtk.Container
import org.apache.pivot.wtk.Cursor
import org.apache.pivot.wtk.ComponentListener
import org.apache.pivot.wtk.DragSource
import org.apache.pivot.wtk.DropTarget
import org.apache.pivot.wtk.MenuHandler

/**
 * @author Andres Almiray
 */
class ComponentListenerAdapter extends BuilderDelegate implements ComponentListener {
    private Closure onParentChanged
    private Closure onPreferredSizeChanged
    private Closure onPreferredWidthLimitsChanged
    private Closure onPreferredHeightLimitsChanged
    private Closure onLocationChanged
    private Closure onVisibleChanged
    private Closure onCursorChanged
    private Closure onTooltipTextChanged
    private Closure onDragSourceChanged
    private Closure onDropTargetChanged
    private Closure onMenuHandlerChanged
    private Closure onStyleUpdated
    private Closure onSizeChanged
 
    ComponentListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onParentChanged(Closure callback) {
        onParentChanged = callback
        onParentChanged.delegate = this
    }

    void onPreferredSizeChanged(Closure callback) {
        onPreferredSizeChanged = callback
        onPreferredSizeChanged.delegate = this
    }

    void onPreferredWidthLimitsChanged(Closure callback) {
        onPreferredWidthLimitsChanged = callback
        onPreferredWidthLimitsChanged.delegate = this
    }

    void onPreferredHeightLimitsChanged(Closure callback) {
        onPreferredHeightLimitsChanged = callback
        onPreferredHeightLimitsChanged.delegate = this
    }

    void onLocationChanged(Closure callback) {
        onLocationChanged = callback
        onLocationChanged.delegate = this
    }

    void onVisibleChanged(Closure callback) {
        onVisibleChanged = callback
        onVisibleChanged.delegate = this
    }

    void onCursorChanged(Closure callback) {
        onCursorChanged = callback
        onCursorChanged.delegate = this
    }

    void onTooltipTextChanged(Closure callback) {
        onTooltipTextChanged = callback
        onTooltipTextChanged.delegate = this
    }

    void onDragSourceChanged(Closure callback) {
        onDragSourceChanged = callback
        onDragSourceChanged.delegate = this
    }

    void onDropTargetChanged(Closure callback) {
        onDropTargetChanged = callback
        onDropTargetChanged.delegate = this
    }

    void onMenuHandlerChanged(Closure callback) {
        onMenuHandlerChanged = callback
        onMenuHandlerChanged.delegate = this
    }

    void onStyleUpdated(Closure callback) {
        onStyleUpdated = callback
        onStyleUpdated.delegate = this
    }

    void onSizeChanged(Closure callback) {
        onSizeChanged = callback
        onSizeChanged.delegate = this
    }

    void parentChanged(Component arg0, Container arg1) {
        if(onParentChanged) onParentChanged(arg0, arg1)
    }

    void preferredSizeChanged(Component arg0, int arg1, int arg2) {
        if(onPreferredSizeChanged) onPreferredSizeChanged(arg0, arg1, arg2)
    }

    void preferredWidthLimitsChanged(Component arg0, int arg1, int arg2) {
        if(onPreferredWidthLimitsChanged) onPreferredWidthLimitsChanged(arg0, arg1, arg2)
    }

    void preferredHeightLimitsChanged(Component arg0, int arg1, int arg2) {
        if(onPreferredHeightLimitsChanged) onPreferredHeightLimitsChanged(arg0, arg1, arg2)
    }

    void locationChanged(Component arg0, int arg1, int arg2) {
        if(onLocationChanged) onLocationChanged(arg0, arg1, arg2)
    }

    void visibleChanged(Component arg0) {
        if(onVisibleChanged) onVisibleChanged(arg0)
    }

    void cursorChanged(Component arg0, Cursor arg1) {
        if(onCursorChanged) onCursorChanged(arg0, arg1)
    }

    void tooltipTextChanged(Component arg0, String arg1) {
        if(onTooltipTextChanged) onTooltipTextChanged(arg0, arg1)
    }

    void dragSourceChanged(Component arg0, DragSource arg1) {
        if(onDragSourceChanged) onDragSourceChanged(arg0, arg1)
    }

    void dropTargetChanged(Component arg0, DropTarget arg1) {
        if(onDropTargetChanged) onDropTargetChanged(arg0, arg1)
    }

    void menuHandlerChanged(Component arg0, MenuHandler arg1) {
        if(onMenuHandlerChanged) onMenuHandlerChanged(arg0, arg1)
    }

    void styleUpdated(Component arg0, String arg1, Object arg2) {
        if(onStyleUpdated) onStyleUpdated(arg0, arg1, arg2)
    }

    void sizeChanged(Component arg0, int arg1, int arg2) {
        if(onSizeChanged) onSizeChanged(arg0, arg1, arg2)
    }
}