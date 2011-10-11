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
import java.util.Map;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.BasicEventList;

import static griffon.util.GriffonNameUtils.isBlank;

/**
 * @author Andres Almiray
 */
public class FinderModel extends AbstractDialogModel {
    public static final String KEY_TO_FIND = "toFind";
    public static final String KEY_REPLACE_WITH = "replaceWith";
    public static final String KEY_MATCH_CASE = "matchCase";
    public static final String KEY_REGEX = "regex";
    public static final String KEY_WHOLE_WORLD = "wholeWord";
    public static final String KEY_ENTER_TYPED = "enterTyped";
    public static final String KEY_OUTPUT = "output";
    public static final String DIALOG_NAME = "finder";

    private static final int MAX_ENTRIES = 10;
    private String toFind = "";
    private String replaceWith = "";
    private boolean matchCase;
    private boolean regex;
    private boolean wholeWord;
    private boolean enterTyped;
    private String output;
    private JComponent subject;
    private EventList<String> recentFinds = new BasicEventList<String>();
    private EventList<String> recentReplacements = new BasicEventList<String>();

    public void mvcGroupInit(Map<String, Object> args) {
        super.mvcGroupInit(args);
        setModal(false);
        setResizable(false);
        setCenter(false);
    }

    protected String getDialogKey() {
        return "Find";
    }

    protected String getDialogTitle() {
        return "Find";
    }

    public String getToFind() {
        return toFind;
    }

    public void setToFind(String toFind) {
        firePropertyChange(KEY_TO_FIND, this.toFind, this.toFind = toFind);
    }

    public String getReplaceWith() {
        return replaceWith;
    }

    public void setReplaceWith(String replaceWith) {
        firePropertyChange(KEY_REPLACE_WITH, this.replaceWith, this.replaceWith = replaceWith);
    }

    public boolean isMatchCase() {
        return matchCase;
    }

    public void setMatchCase(boolean matchCase) {
        firePropertyChange(KEY_REPLACE_WITH, this.matchCase, this.matchCase = matchCase);
    }

    public boolean isRegex() {
        return regex;
    }

    public void setRegex(boolean regex) {
        firePropertyChange(KEY_REGEX, this.regex, this.regex = regex);
    }

    public boolean isWholeWord() {
        return wholeWord;
    }

    public void setWholeWord(boolean wholeWord) {
        firePropertyChange(KEY_WHOLE_WORLD, this.wholeWord, this.wholeWord = wholeWord);
    }

    public boolean isEnterTyped() {
        return enterTyped;
    }

    public void setEnterTyped(boolean enterTyped) {
        firePropertyChange(KEY_ENTER_TYPED, this.enterTyped, this.enterTyped = enterTyped);
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        firePropertyChange(KEY_OUTPUT, this.output, this.output = output);
    }

    public JComponent getSubject() {
        return subject;
    }

    public void setSubject(JComponent subject) {
        this.subject = subject;
    }

    public EventList<String> getRecentFinds() {
        return recentFinds;
    }

    public EventList<String> getRecentReplacements() {
        return recentReplacements;
    }

    public void updateRecentFinds() {
        updateList(toFind, recentFinds);
    }

    public void updateRecentReplacements() {
        updateList(replaceWith, recentReplacements);
    }

    private void updateList(String value, EventList<String> list) {
        if (isBlank(value)) return;
        int index = list.indexOf(value);
        if (index > 0) {
            list.remove(value);
            list.add(0, value);
        } else if (index < 0) {
            list.add(0, value);
            if (list.size() > MAX_ENTRIES) {
                list.remove(MAX_ENTRIES - 1);
            }
        }
    }
}
