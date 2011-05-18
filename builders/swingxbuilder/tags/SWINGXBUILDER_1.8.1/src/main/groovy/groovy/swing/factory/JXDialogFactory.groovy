/*
 * $Id:  $
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package groovy.swing.factory

import groovy.swing.SwingBuilder
import org.jdesktop.swingx.JXDialog
import org.jdesktop.swingx.JXPanel


public class JXDialogFactory extends RootPaneContainerFactory {

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        def dialog
        if (SwingBuilder.checkValueIsType(value, name, JXDialog.class)) {
            dialog = value
        } else {

            def owner = attributes.remove("documentRoot")
            if (attributes.containsKey("owner")) {
                def tempOwner = attributes.remove("owner")
                if (owner == null) {
                    owner = tempOwner;
                }
            }
            // if owner not explicit, use the last window type in the list
            LinkedList containingWindows = builder.containingWindows
            if ((owner == null) && !containingWindows.isEmpty()) {
                owner = containingWindows.getLast()
            }

            def content = attributes.remove("content")
            if (content == null) {
                content = new JXPanel()
            }

            if (owner) {
                dialog = new JXDialog(owner, content);
            } else {
                dialog = new JXDialog(content)
            }
        }

        handleRootPaneTasks(builder, dialog, attributes)

        return dialog
    }

}
