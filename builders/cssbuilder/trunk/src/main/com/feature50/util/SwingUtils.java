/*
 * Copyright 2007 Ben Galbraith.
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

package com.feature50.util;

import com.feature50.clarity.ClarityConstants;

import javax.swing.JComponent;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.awt.Component;
import java.awt.Container;

import java.util.logging.Logger;

public class SwingUtils {
    private static final Logger logger = Logger.getLogger(SwingUtils.class.getName());

    public static List<JComponent> removeUnnamedComponents(Collection components) {
        List<JComponent> c = new ArrayList<JComponent>();
        for (Iterator iterator = components.iterator(); iterator.hasNext();) {
            JComponent component = (JComponent) iterator.next();
            if (StringUtils.notNullOrEmpty(component.getName())) c.add(component);
        }
        return c;
    }

    public static List<JComponent> getAllJComponents(Container container) {
        List<JComponent> components = new ArrayList<JComponent>();
        getAllJComponents(container, components);
        return components;
    }

    private static void getAllJComponents(Container container, Collection<JComponent> collection) {
        if (container instanceof JComponent) {
            JComponent c = (JComponent) container;
            collection.add(c);
        }

        Component[] children = container.getComponents();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                Component c = children[i];
                if (c instanceof Container) {
                    getAllJComponents((Container) c, collection);
                }
            }
        }
    }

    public static JComponent getComponentByName(Container parent, String name) {
        Collection components = getAllJComponents(parent);
        for (Iterator iterator = components.iterator(); iterator.hasNext();) {
            JComponent component = (JComponent) iterator.next();
            if (name.equals(component.getName())) return component;
        }
        return null;
    }

    public static JComponent[] getComponentsByPattern(Container parent, Pattern pattern) {
        java.util.List<JComponent> results = new ArrayList<JComponent>();

        Collection components = SwingUtils.getAllJComponents(parent);
        for (Iterator iterator = components.iterator(); iterator.hasNext();) {
            JComponent component = (JComponent) iterator.next();
            String name = component.getName();
            if (StringUtils.isNullOrEmpty(name)) continue;
            if (pattern.matcher(name).matches()) results.add(component);
        }

        return results.toArray(new JComponent[0]);
    }

    public static JComponent[] parseSelector(String selector, List<JComponent> allComponents) {
        List<JComponent> comps = new ArrayList<JComponent>();

        // split the potential selector group into individual selectors (they can be grouped together with commas)
        String[] group = selector.split(",");
        for (int i = 0; i < group.length; i++) {
            // trim extraneous whitespace
            String s = group[i].trim();
            String normalizedSelector = normalizeSelector(s);

            // build an individual SelectorElement for each component in the selector
            List<SelectorElement> elements = new ArrayList<SelectorElement>();
            StringBuffer element = new StringBuffer();
            boolean add = false;
            boolean child = false;
            boolean sibling = false;
            for (int z = normalizedSelector.length() - 1; z >= -1; z--) {
                char c;
                if (z == -1) {
                    c = ' ';
                } else {
                    c = normalizedSelector.charAt(z);
                }

                if (c == ' ') {
                    add = true;
                } else if (c == '+') {
                    add = true;
                    sibling = true;
                } else if (c == '>') {
                    add = true;
                    child = true;
                } else {
                    element.insert(0, c);
                }

                if (add) {
                    String e = element.toString();
                    String pseudoClass = null;
                    String type = null;
                    String id = null;
                    String clazz = null;

                    // pseudo-class support
                    if ((e != null) && (e.indexOf(":") != -1)) {
                        logger.warning(String.format("Pseudo classes not supported ('%1$s' uses one)", element));
                        String[] temp = e.split(":");
                        e = temp[0];
                        pseudoClass = temp[1];
                    }

                    // attribute selector
                    if ((e != null) && (e.indexOf("[") != -1)) {
                        logger.warning(String.format("Attribute selectors not supported ('%1$s' uses one)", element));
                        e = e.replaceAll("\\[.*\\]", "");
                    }

                    // id selector
                    if ((e != null) && (e.indexOf("#") != -1)) {
                        String[] temp = e.split("#");
                        if (StringUtils.notNullOrEmpty(temp[0])) {
                            e = temp[0];
                        } else {
                            e = null;
                        }
                        id = temp[1];
                    }

                    // class
                    if ((e != null) && (e.indexOf(".") != -1)) {
                        String[] temp = e.split("\\.");
                        if (StringUtils.notNullOrEmpty(temp[0])) {
                            e = temp[0];
                        } else {
                            e = null;
                        }
                        clazz = temp[1];
                    }

                    type = e;

                    elements.add(new SelectorElement(child, sibling, false, id, clazz, type));

                    element = new StringBuffer();
                    sibling = false;
                    child = false;
                    add = false;
                }
            }

            // match the components selected by the selector
            List<JComponent> selected = new ArrayList<JComponent>();
            for (int j = 0; j < allComponents.size(); j++) {
                JComponent component = allComponents.get(j);
                if (matches(component, elements.get(0))) selected.add(component);
            }

            // make sure each component has the appropriate hierarchy
            if (elements.size() > 1) {
                for (int j = 0; j < selected.size(); ) {
                    JComponent component = selected.get(j);

                    boolean matched = false;
                    JComponent leftOff = component;
                    for (int q = 1; q < elements.size(); q++) {
                        SelectorElement se = elements.get(q);
                        matched = false;

                        // check for a special rule
                        if (elements.get(q - 1).sibling) {
                            if (leftOff.getParent() == null) break;
                            Component[] siblings = leftOff.getParent().getComponents();

                            boolean foundSibling = false;
                            for (int k = 0; k < siblings.length; k++) {
                                if (!(siblings[k] instanceof JComponent)) continue;
                                if (matches((JComponent) siblings[k], se)) {
                                    leftOff = (JComponent) siblings[k];
                                    foundSibling = true;
                                    break;
                                }
                            }
                            if (!foundSibling) break;
                        } else if (elements.get(q - 1).child) {
                            if (leftOff.getParent() == null) break;
                            Component parent = leftOff.getParent();
                            if (!(parent instanceof JComponent)) break;
                            if (!matches((JComponent) parent, se)) break;
                            leftOff = (JComponent) parent;
                        } else {
                            Component parent = leftOff.getParent();
                            boolean foundAncestor = false;
                            while (parent != null) {
                                if (!(parent instanceof JComponent)) break;
                                JComponent c = (JComponent) parent;
                                if (matches(c, se)) {
                                    foundAncestor = true;
                                    leftOff = c;
                                    break;
                                }
                                parent = c.getParent();
                            }
                            if (!foundAncestor) break;
                        }
                        matched = true;
                    }

                    if (matched) {
                        j++;
                    } else {
                        selected.remove(j);
                    }
                }
            }

            comps.addAll(selected);
        }

        return comps.toArray(new JComponent[0]);
    }

    private static boolean matches(JComponent component, SelectorElement element) {
        if (element.id != null) {
            if (!element.id.equals(component.getName())) return false;
        }

        if (element.clazz != null) {
            if (!element.clazz.equals(component.getClientProperty(ClarityConstants.CLIENT_PROPERTY_CLASS_KEY))) return false;
        }

        if ((element.type != null) && (!element.type.equals("*"))) {
            boolean found = false;
            Class c = component.getClass();
            while (c != null) {
                if (c.getSimpleName().equalsIgnoreCase(element.type)) {
                    found = true;
                    break;
                }
                c = c.getSuperclass();
            }
            if (!found) return false;
        }

        return true;
    }

    private static String normalizeSelector(String selector) {
        while (selector.indexOf("  ") != -1) selector = selector.replaceAll("  ", " ");
        selector = selector.replaceAll(" \\+ ", "\\+");
        selector = selector.replaceAll(" \\> ", "\\>");
        return selector;
    }

    private static class SelectorElement {
        private boolean child;
        private boolean sibling;
        private boolean exactType;

        private String id;
        private String clazz;
        private String type;

        public SelectorElement(boolean child, boolean sibling, boolean exactType, String id, String clazz, String type) {
            this.child = child;
            this.sibling = sibling;
            this.exactType = exactType;
            this.id = id;
            this.clazz = clazz;
            this.type = type;
        }
    }
}