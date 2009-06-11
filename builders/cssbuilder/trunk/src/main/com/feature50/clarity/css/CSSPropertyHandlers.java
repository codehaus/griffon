package com.feature50.clarity.css;

import javax.swing.JComponent;
import java.util.List;
import java.util.ArrayList;

public class CSSPropertyHandlers {
    private static CSSPropertyHandlers instance;

    private List<CSSPropertyHandler> handlers = new ArrayList<CSSPropertyHandler>();

    private CSSPropertyHandlers() {
        addHandler(new DefaultCSSPropertyHandler());
    }

    public boolean handle(JComponent[] components, String property, String value) {
        boolean processed = false;
        for (int i = 0; i < handlers.size(); i++) {
            CSSPropertyHandler h = handlers.get(i);
            for (int j = 0; j < h.getPropertyNames().length; j++) {
                String name = h.getPropertyNames()[j];
                if (property.equals(name)) {
                    processed = true;
                    h.processStyles(components, property, value);
                }
            }
        }
        return processed;
    }

    public void addHandler(CSSPropertyHandler handler) {
        handlers.add(handler);
    }

    public void removeHandler(CSSPropertyHandler handler) {
        handlers.remove(handler);
    }

    public static synchronized CSSPropertyHandlers getInstance() {
        if (instance == null) {
            instance = new CSSPropertyHandlers();
        }

        return instance;
    }
}
