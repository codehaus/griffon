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

/**
 * @author Andres Almiray
 */

package griffon.plugins.dialogs;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import ca.odell.glazedlists.swing.EventComboBoxModel;
import griffon.plugins.actions.ActionManager;
import griffon.plugins.i18n.MessageSourceHolder;
import net.miginfocom.swing.MigLayout;

import griffon.util.RunnableWithArgs;
import org.codehaus.griffon.runtime.core.AbstractGriffonView;

import static griffon.util.GriffonNameUtils.isBlank;

public class FinderView extends AbstractGriffonView {
    private FinderController controller;
    private FinderModel model;
    private JComponent content;
    private JButton findNextButton;

    public void setController(FinderController controller) {
        this.controller = controller;
    }

    public void setModel(FinderModel model) {
        this.model = model;
    }

    public FinderController getController() {
        return controller;
    }

    public FinderModel getModel() {
        return model;
    }

    @Override
    public void mvcGroupInit(Map<String, Object> args) {
        execSync(new Runnable() {
            public void run() {
                getBuilder().setVariable("content", buildContent());
            }
        });
    }

    private JComponent buildContent() {
        JPanel panel = new JPanel(new MigLayout("fillx"));
        buildFindRow(panel);
        buildReplaceRow(panel);
        buildOptionsRow(panel);
        buildButtonsRow(panel);

        String actionKey = "HideAction";
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), actionKey);
        panel.getActionMap().put(actionKey, ActionManager.getInstance().actionFor(controller, "hide"));

        return panel;
    }

    private void buildFindRow(JPanel container) {
        container.add(
                new JLabel(msg("application.dialog.Finder.find", "Find:")),
                "right, gapx 5"
        );
        final JComboBox findComboBox = new JComboBox(new EventComboBoxModel<String>(model.getRecentFinds()));
        findComboBox.setEditable(true);
        ComboBoxUpdater findComboBoxUpdater = new ComboBoxUpdater(findComboBox, new RunnableWithArgs() {
            public void run(Object[] args) {
                model.setToFind((String) args[0]);
            }
        });
        findComboBox.getEditor().getEditorComponent().addKeyListener(findComboBoxUpdater);
        container.add(findComboBox, "wmin 500, left, grow, push, wrap");
    }

    private void buildReplaceRow(JPanel container) {
        container.add(
                new JLabel(msg("application.dialog.Finder.replace", "Replace:")),
                "right, gapx 5"
        );
        final JComboBox replaceComboBox = new JComboBox(new EventComboBoxModel<String>(model.getRecentReplacements()));
        replaceComboBox.setEditable(true);
        ComboBoxUpdater replaceComboBoxComboBoxUpdater = new ComboBoxUpdater(replaceComboBox, new RunnableWithArgs() {
            public void run(Object[] args) {
                model.setReplaceWith((String) args[0]);
            }
        });
        replaceComboBox.getEditor().getEditorComponent().addKeyListener(replaceComboBoxComboBoxUpdater);
        container.add(replaceComboBox, "wmin 500, left, grow, push, wrap");
    }

    private void buildOptionsRow(JPanel container) {
        JPanel options = new JPanel(new MigLayout("insets 0"));

        final JLabel output = new JLabel();
        options.add(output, "left, width 120:120:");
        model.addPropertyChangeListener(FinderModel.KEY_OUTPUT, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                output.setText(model.getOutput());
            }
        });

        final JCheckBox regex = new JCheckBox(msg("'application.dialog.Finder.regex", "Regular expression"));
        options.add(regex, "right");
        regex.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                model.setRegex(regex.isSelected());
            }
        });
        final JCheckBox matchcase = new JCheckBox(msg("'application.dialog.Finder.matchcase", "Match case"));
        options.add(matchcase, "right");
        matchcase.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                model.setMatchCase(matchcase.isSelected());
            }
        });
        final JCheckBox wholeWord = new JCheckBox(msg("'application.dialog.Finder.wholeWord", "Whole word"));
        options.add(wholeWord, "right, gap");
        wholeWord.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                model.setWholeWord(wholeWord.isSelected());
            }
        });

        container.add(options, "span 2, right, grow, wrap");
    }

    private void buildButtonsRow(JPanel container) {
        final Action findPreviousAction = actionFor("findPrevious");
        final Action findNextAction = actionFor("findNext");
        final Action replaceAction = actionFor("replace");
        final Action replaceAllAction = actionFor("replaceAll");

        findPreviousAction.setEnabled(false);
        findNextAction.setEnabled(false);
        replaceAction.setEnabled(false);
        replaceAllAction.setEnabled(false);

        model.addPropertyChangeListener(FinderModel.KEY_TO_FIND, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                findPreviousAction.setEnabled(!isBlank(model.getToFind()));
                findNextAction.setEnabled(!isBlank(model.getToFind()));
            }
        });
        model.addPropertyChangeListener(FinderModel.KEY_REPLACE_WITH, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                replaceAction.setEnabled(!isBlank(model.getToFind()) && !isBlank(model.getReplaceWith()));
                replaceAllAction.setEnabled(!isBlank(model.getToFind()) && !isBlank(model.getReplaceWith()));
            }
        });

        JPanel buttons = new JPanel(new MigLayout("insets 0"));
        buttons.add(new JButton(replaceAllAction), "left");
        buttons.add(new JButton(replaceAction), "left, push, gap");
        buttons.add(new JButton(findPreviousAction), "right");
        findNextButton = new JButton(findNextAction);
        buttons.add(findNextButton, "right");
        container.add(buttons, "span2, grow");
        getBuilder().setVariable("findNextButton", findNextButton);
    }

    private class ComboBoxUpdater extends KeyAdapter implements ItemListener {
        private final JComboBox comboBox;
        private final RunnableWithArgs block;

        public ComboBoxUpdater(JComboBox comboBox, RunnableWithArgs block) {
            this.comboBox = comboBox;
            this.block = block;
            this.comboBox.addItemListener(this);
        }

        public void keyTyped(KeyEvent keyEvent) {
            update(keyEvent);
        }

        public void keyPressed(KeyEvent keyEvent) {
            update(keyEvent);
        }

        public void keyReleased(KeyEvent keyEvent) {
            update(keyEvent);
        }

        private void update(KeyEvent keyEvent) {
            String text = ((JTextComponent) comboBox.getEditor().getEditorComponent()).getText();
            block.run(new Object[]{text});
            model.setEnterTyped(keyEvent.getKeyCode() == KeyEvent.VK_ENTER);
        }

        public void itemStateChanged(ItemEvent itemEvent) {
            if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                String text = (String) itemEvent.getItem();
                block.run(new Object[]{text});
                model.setEnterTyped(false);
            }
        }
    }

    private Action actionFor(String actionName) {
        return ActionManager.getInstance().actionFor(controller, actionName);
    }

    private String msg(String key, String defaultMessage) {
        return MessageSourceHolder.getMessageSource().getMessage(key, defaultMessage);
    }

    private String msg(String key, Object[] args, String defaultMessage) {
        return MessageSourceHolder.getMessageSource().getMessage(key, args, defaultMessage);
    }
}