/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hackergarten.griffon.jtreemap;

import net.sf.jtreemap.swing.JTreeMap;
import net.sf.jtreemap.swing.TreeMapNode;

/**
 * @author Hackergarten
 */
public class GriffonTreeMap extends JTreeMap {
    public GriffonTreeMap() {
        super(createDummyNode());
    }

    private static TreeMapNode createDummyNode() {
        return new TreeMapNode("Dummy");
    }

    @Override
    public void setRoot(final TreeMapNode newRoot) {
        super.setRoot(newRoot);
        repaint();
    }

    @Override
    public void setDisplayedRoot(final TreeMapNode newDisplayedRoot) {
        super.setDisplayedRoot(newDisplayedRoot);
        repaint();
    }
}
