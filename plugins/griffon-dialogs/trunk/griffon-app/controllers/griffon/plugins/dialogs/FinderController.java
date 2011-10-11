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

import griffon.plugins.i18n.MessageSourceHolder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Andres Almiray
 */
public class FinderController extends DialogController {
    private static final String KEY_NOT_FOUND = ".notfound";
    private static final String KEY_NOTHING = ".nothing";
    private static final String KEY_MATCHES = ".matches";
    private static final String MSG_NOT_FOUND = "Not found";
    private static final String MSG_NOTHING = "Nothing";
    private static final String MSG_REPLACED = " replaced";

    public void findPrevious() {
        findPrevious(null);
    }

    public void findPrevious(ActionEvent event) {
        model().setOutput("");
        model().updateRecentFinds();
        Searcher searcher = Finder.getInstance().searcherFor(model().getSubject());
        if (getLog().isDebugEnabled()) {
            getLog().debug("FindPrevious toFind: " + model().getToFind() +
                    ", matchCase: " + model().isMatchCase() +
                    ", wholeWord: " + model().isWholeWord() +
                    ", regex: " + model().isRegex());
        }
        boolean found = searcher.find(model().getSubject(), model().getToFind(), false, model().isMatchCase(), model().isWholeWord(), model().isRegex());
        if (getLog().isDebugEnabled()) {
            getLog().debug("Found " + found);
        }
        if (!found) model().setOutput(msg(Finder.class.getName() + KEY_NOT_FOUND, MSG_NOT_FOUND));
    }

    public void findNext() {
        findNext(null);
    }

    public void findNext(ActionEvent event) {
        model().setOutput("");
        model().updateRecentFinds();
        Searcher searcher = Finder.getInstance().searcherFor(model().getSubject());
        if (getLog().isDebugEnabled()) {
            getLog().debug("FindNext toFind: " + model().getToFind() +
                    ", matchCase: " + model().isMatchCase() +
                    ", wholeWord: " + model().isWholeWord() +
                    ", regex: " + model().isRegex());
        }
        boolean found = searcher.find(model().getSubject(), model().getToFind(), true, model().isMatchCase(), model().isWholeWord(), model().isRegex());
        if (getLog().isDebugEnabled()) {
            getLog().debug("Found " + found);
        }
        if (!found) {
            model().setOutput(msg(Finder.class.getName() + KEY_NOT_FOUND, MSG_NOT_FOUND));
        } else {
            if (model().isEnterTyped()) hide();
        }
    }

    public void replace() {
        replace(null);
    }

    public void replace(ActionEvent event) {
        model().setOutput("");
        model().updateRecentFinds();
        model().updateRecentReplacements();
        Searcher searcher = Finder.getInstance().searcherFor(model().getSubject());
        if (getLog().isDebugEnabled()) {
            getLog().debug("Replace toFind: " + model().getToFind() +
                    ", replaceWith: " + model().getReplaceWith() +
                    ", matchCase: " + model().isMatchCase() +
                    ", wholeWord: " + model().isWholeWord() +
                    ", regex: " + model().isRegex());
        }
        boolean replaced = searcher.replace(model().getSubject(), model().getToFind(), model().getReplaceWith(), true, model().isMatchCase(), model().isWholeWord(), model().isRegex());
        if (getLog().isDebugEnabled()) {
            getLog().debug("Replace " + replaced);
        }
        if (!replaced) model().setOutput(msg(Finder.class.getName() + KEY_NOT_FOUND, MSG_NOT_FOUND));
    }

    public void replaceAll() {
        replaceAll(null);
    }

    public void replaceAll(ActionEvent event) {
        model().setOutput("");
        model().updateRecentFinds();
        model().updateRecentReplacements();
        Searcher searcher = Finder.getInstance().searcherFor(model().getSubject());
        if (getLog().isDebugEnabled()) {
            getLog().debug("ReplaceAll toFind: " + model().getToFind() +
                    ", replaceWith: " + model().getReplaceWith() +
                    ", matchCase: " + model().isMatchCase() +
                    ", wholeWord: " + model().isWholeWord() +
                    ", regex: " + model().isRegex());
        }
        int matches = searcher.replaceAll(model().getSubject(), model().getToFind(), model().getReplaceWith(), model().isMatchCase(), model().isWholeWord(), model().isRegex());
        if (getLog().isDebugEnabled()) {
            getLog().debug("ReplaceAll " + matches + " matches");
        }
        if (matches == 0) {
            model().setOutput(msg(Finder.class.getName() + KEY_NOTHING, MSG_NOTHING));
        } else {
            model().setOutput(msg(Finder.class.getName() + KEY_MATCHES, new Object[]{matches}, matches + MSG_REPLACED));
        }
    }

    protected JDialog createDialog(Window window) {
        JDialog dialog = super.createDialog(window);
        dialog.setName(FinderModel.DIALOG_NAME);
        JButton findNextButton = (JButton) getBuilder().getVariable("findNextButton");
        dialog.getRootPane().setDefaultButton(findNextButton);
        return dialog;
    }

    protected void displayDialog(final JDialog dialog) {
        JButton findNextButton = (JButton) getBuilder().getVariable("findNextButton");
        findNextButton.getModel().setArmed(true);
        super.displayDialog(dialog);
    }

    private FinderModel model() {
        return (FinderModel) getModel();
    }

    private String msg(String key, String defaultMessage) {
        return MessageSourceHolder.getMessageSource().getMessage(key, defaultMessage);
    }

    private String msg(String key, Object[] args, String defaultMessage) {
        return MessageSourceHolder.getMessageSource().getMessage(key, args, defaultMessage);
    }
}
