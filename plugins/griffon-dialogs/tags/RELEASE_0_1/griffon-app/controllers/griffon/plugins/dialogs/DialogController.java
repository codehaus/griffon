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

import griffon.swing.SwingGriffonApplication;
import groovy.util.FactoryBuilderSupport;
import org.codehaus.griffon.runtime.core.AbstractGriffonController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * @author Andres Almiray
 */
public class DialogController extends AbstractGriffonController {
    private AbstractDialogModel model;
    private FactoryBuilderSupport builder;

    protected JDialog dialog;

    public void setModel(AbstractDialogModel model) {
        this.model = model;
    }

    public void setBuilder(FactoryBuilderSupport builder) {
        this.builder = builder;
    }

    public AbstractDialogModel getModel() {
        return model;
    }

    public FactoryBuilderSupport getBuilder() {
        return builder;
    }

    public void show() {
        show((Window) null);
    }

    public void show(ActionEvent event) {
        show((Window) null);
    }

    public void show(final Window owner) {
        execAsync(new Runnable() {
            public void run() {
                boolean created = false;
                Window window = owner;
                if (window == null) {
                    List<Window> windows = ((SwingGriffonApplication) getApp()).getWindowManager().getWindows();
                    for (Window w : windows) {
                        if (w.isFocused()) {
                            window = w;
                            break;
                        }
                    }
                }

                if (dialog == null || dialog.getOwner() != window) {
                    dialog = createDialog(window);
                    created = true;
                }

                if (created || model.isCenter() && window != null) {
                    placeDialog(window, dialog);
                }
                displayDialog(dialog);
            }
        });
    }

    public void hide() {
        hide(null);
    }

    public void hide(ActionEvent event) {
        execAsync(new Runnable() {
            public void run() {
                if (dialog != null) {
                    dialog.setVisible(false);
                    dialog.dispose();
                }
                dialog = null;
            }
        });
    }

    protected JDialog createDialog(Window window) {
        JDialog dialog = new JDialog(window, model.getTitle());
        dialog.setResizable(model.isResizable());
        dialog.setModal(model.isModal());
        dialog.getContentPane().add((JComponent) builder.getVariable("content"));

        if (model.getWidth() > 0 && model.getHeight() > 0) {
            dialog.setPreferredSize(new Dimension(model.getWidth(), model.getHeight()));
        }
        dialog.pack();
        return dialog;
    }

    protected void placeDialog(Window window, JDialog dialog) {
        int x = window.getX() + (window.getWidth() - dialog.getWidth()) / 2;
        int y = window.getY() + (window.getHeight() - dialog.getHeight()) / 2;
        dialog.setLocation(x, y);
    }

    protected void displayDialog(JDialog dialog) {
        dialog.setVisible(true);
    }
}
