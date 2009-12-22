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
import java.io.File
import org.apache.pivot.wtk.FileBrowser
import org.apache.pivot.collections.Sequence
import org.apache.pivot.util.Filter
import org.apache.pivot.wtk.FileBrowserListener

/**
 * @author Andres Almiray
 */
class FileBrowserListenerAdapter extends BuilderDelegate implements FileBrowserListener {
    private Closure onRootDirectoryChanged
    private Closure onSelectedFileAdded
    private Closure onSelectedFileRemoved
    private Closure onSelectedFilesChanged
    private Closure onMultiSelectChanged
    private Closure onDisabledFileFilterChanged
 
    FileBrowserListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onRootDirectoryChanged(Closure callback) {
        onRootDirectoryChanged = callback
        onRootDirectoryChanged.delegate = this
    }

    void onSelectedFileAdded(Closure callback) {
        onSelectedFileAdded = callback
        onSelectedFileAdded.delegate = this
    }

    void onSelectedFileRemoved(Closure callback) {
        onSelectedFileRemoved = callback
        onSelectedFileRemoved.delegate = this
    }

    void onSelectedFilesChanged(Closure callback) {
        onSelectedFilesChanged = callback
        onSelectedFilesChanged.delegate = this
    }

    void onMultiSelectChanged(Closure callback) {
        onMultiSelectChanged = callback
        onMultiSelectChanged.delegate = this
    }

    void onDisabledFileFilterChanged(Closure callback) {
        onDisabledFileFilterChanged = callback
        onDisabledFileFilterChanged.delegate = this
    }

    void rootDirectoryChanged(FileBrowser arg0, File arg1) {
        if(onRootDirectoryChanged) onRootDirectoryChanged(arg0, arg1)
    }

    void selectedFileAdded(FileBrowser arg0, File arg1) {
        if(onSelectedFileAdded) onSelectedFileAdded(arg0, arg1)
    }

    void selectedFileRemoved(FileBrowser arg0, File arg1) {
        if(onSelectedFileRemoved) onSelectedFileRemoved(arg0, arg1)
    }

    void selectedFilesChanged(FileBrowser arg0, Sequence arg1) {
        if(onSelectedFilesChanged) onSelectedFilesChanged(arg0, arg1)
    }

    void multiSelectChanged(FileBrowser arg0) {
        if(onMultiSelectChanged) onMultiSelectChanged(arg0)
    }

    void disabledFileFilterChanged(FileBrowser arg0, Filter arg1) {
        if(onDisabledFileFilterChanged) onDisabledFileFilterChanged(arg0, arg1)
    }
}