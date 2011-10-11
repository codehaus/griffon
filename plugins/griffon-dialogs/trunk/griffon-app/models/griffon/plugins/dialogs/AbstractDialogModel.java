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
import org.codehaus.griffon.runtime.core.AbstractGriffonModel;

import java.util.Map;

import static griffon.util.GriffonNameUtils.capitalize;

/**
 * @author Andres Almiray
 */
public abstract class AbstractDialogModel extends AbstractGriffonModel {
    private String title;
    private int width = 0;
    private int height = 0;
    private boolean resizable = true;
    private boolean modal = true;
    private boolean center = true;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        firePropertyChange("title", this.title, this.title = title);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        firePropertyChange("width", this.width, this.width = width);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        firePropertyChange("height", this.height, this.height = height);
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        firePropertyChange("resizable", this.resizable, this.resizable = resizable);
    }

    public boolean isModal() {
        return modal;
    }

    public void setModal(boolean modal) {
        firePropertyChange("modal", this.modal, this.modal = modal);
    }

    public boolean isCenter() {
        return center;
    }

    public void setCenter(boolean center) {
        firePropertyChange("center", this.center, this.center = center);
    }

    protected abstract String getDialogKey();

    protected abstract String getDialogTitle();

    public void mvcGroupInit(Map<String, Object> args) {
        title = capitalize(MessageSourceHolder.getMessageSource().getMessage("application.dialog." + getDialogKey() + ".title", getDialogTitle()));
    }
}
