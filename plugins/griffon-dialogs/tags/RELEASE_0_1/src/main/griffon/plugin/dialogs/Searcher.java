/*
 * griffon-dialogs: Dialog provider Griffon plugin
 * Copyright 2010 and beyond, Andres Almiray
 *
 * griffon-dialogs is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package griffon.plugins.dialogs;

import javax.swing.JComponent;

/**
 * @author Andres Almiray
 */
public interface Searcher {
    boolean find(JComponent subject, String text, boolean forward, boolean matchCase, boolean wholeWord, boolean regex);

    boolean replace(JComponent subject, String toFind, String replaceWith, boolean forward, boolean matchCase, boolean wholeWord, boolean regex);

    int replaceAll(JComponent subject, String toFind, String replaceWith, boolean matchCase, boolean wholeWord, boolean regex);
}
