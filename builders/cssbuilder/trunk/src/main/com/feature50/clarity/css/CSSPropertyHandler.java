package com.feature50.clarity.css;

import javax.swing.JComponent;

public interface CSSPropertyHandler {
    public String[] getPropertyNames();
    public void processStyles(JComponent[] selected, String propertyName, String propertyValue);
}
