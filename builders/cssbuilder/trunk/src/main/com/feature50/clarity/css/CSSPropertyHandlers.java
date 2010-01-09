/*
 * Copyright 2007-2010 Ben Galbraith.
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
