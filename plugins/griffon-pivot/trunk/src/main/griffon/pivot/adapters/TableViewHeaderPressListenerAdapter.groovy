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
import org.apache.pivot.wtk.TableViewHeader
import org.apache.pivot.wtk.TableViewHeaderPressListener

/**
 * @author Andres Almiray
 */
class TableViewHeaderPressListenerAdapter extends BuilderDelegate implements TableViewHeaderPressListener {
    private Closure onHeaderPressed
 
    TableViewHeaderPressListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onHeaderPressed(Closure callback) {
        onHeaderPressed = callback
        onHeaderPressed.delegate = this
    }

    void headerPressed(TableViewHeader arg0, int arg1) {
        if(onHeaderPressed) onHeaderPressed(arg0, arg1)
    }
}