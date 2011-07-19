/*
 * Copyright 2003-2007 the original author or authors.
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
 */
package griffon.charva.impl;

import charva.awt.Component;
import charva.awt.GridBagConstraints;
import charva.awt.GridBagLayout;
import charva.awt.Insets;

import charvax.swing.JPanel;

/** 
 * Represents a HTML style table layout
 *
 * @author <a href="mailto:james@coredevelopers.net">James Strachan</a>
 * @version $Revision: 10744 $
 */
public class TableLayout extends JPanel {

    private int rowCount;
    private int cellpadding;

    public TableLayout() {
        setLayout(new GridBagLayout());
    }

    /**
     * @deprecated Simply returns this
     * @return this
     */
    public Component getComponent() {
        return this;
    }
    
    public int getCellpadding() {
        return cellpadding;
    }

    public void setCellpadding(int cellpadding) {
        this.cellpadding = cellpadding;
    }

    /**
     * Adds a new cell to the current grid
     * @param cell the td component
     */
    public void addCell(TableLayoutCell cell) {
        GridBagConstraints constraints = cell.getConstraints();
        constraints.insets = new Insets(cellpadding, cellpadding, cellpadding, cellpadding);
        add(cell.getComponent(), constraints);
    }

    /**
     * Creates a new row index for child <tr> tags 
     * @return nextRowIndex the row number
     */
    public int nextRowIndex() {
        return rowCount++;
    }

}
