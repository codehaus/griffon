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
import java.io.File
import org.apache.pivot.wtk.FileBrowserSheet
import org.apache.pivot.collections.Sequence
import org.apache.pivot.util.Filter
import org.apache.pivot.wtk.FileBrowserSheetListener

/**
 * @author Andres Almiray
 */
class FileBrowserSheetListenerAdapter extends BuilderDelegate implements FileBrowserSheetListener {
    private Closure onRootDirectoryChanged
    private Closure onSelectedFilesChanged
    private Closure onDisabledFileFilterChanged
 
    FileBrowserSheetListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onRootDirectoryChanged(Closure callback) {
        onRootDirectoryChanged = callback
        onRootDirectoryChanged.delegate = this
    }

    void onSelectedFilesChanged(Closure callback) {
        onSelectedFilesChanged = callback
        onSelectedFilesChanged.delegate = this
    }

    void onDisabledFileFilterChanged(Closure callback) {
        onDisabledFileFilterChanged = callback
        onDisabledFileFilterChanged.delegate = this
    }

    void rootDirectoryChanged(FileBrowserSheet arg0, File arg1) {
        if(onRootDirectoryChanged) onRootDirectoryChanged(arg0, arg1)
    }

    void selectedFilesChanged(FileBrowserSheet arg0, Sequence arg1) {
        if(onSelectedFilesChanged) onSelectedFilesChanged(arg0, arg1)
    }

    void disabledFileFilterChanged(FileBrowserSheet arg0, Filter arg1) {
        if(onDisabledFileFilterChanged) onDisabledFileFilterChanged(arg0, arg1)
    }
}