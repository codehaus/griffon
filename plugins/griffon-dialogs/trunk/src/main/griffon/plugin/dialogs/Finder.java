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

import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;
import org.codehaus.griffon.runtime.search.JTextComponentSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import static griffon.util.ApplicationHolder.getApplication;
import static griffon.util.GriffonNameUtils.isBlank;

/**
 * @author Andres Almiray
 */
public final class Finder {
    private static final String FINDER_GROUP_NAME = "finder";
    private static final Finder INSTANCE;
    private static final Logger LOG = LoggerFactory.getLogger(Finder.class);
    private final Map<Class, Class> searcherClassRegistry = new LinkedHashMap<Class, Class>();
    private final Map<Class<? extends Searcher>, Searcher> searcherInstanceCache = new LinkedHashMap<Class<? extends Searcher>, Searcher>();

    static {
        INSTANCE = new Finder();
    }

    public static Finder getInstance() {
        return INSTANCE;
    }

    private Finder() {
        registerSearcher(JTextComponent.class, JTextComponentSearcher.class);
        loadSearchers();
    }

    public void registerSearcher(Class type, Class searcherClass) {
        if (type != null && searcherClass != null) {
            searcherClassRegistry.put(type, searcherClass);
            searcherInstanceCache.remove(type);
        }
    }

    public Searcher searcherFor(JComponent subject) {
        if (subject == null) {
            throw new IllegalArgumentException("The component to search over is null!");
        }

        Class type = subject.getClass();
        while (type != null) {
            Searcher searcher = searcherInstanceCache.get(type);
            if (searcher != null) return searcher;
            if (searcherClassRegistry.containsKey(type)) {
                try {
                    searcher = (Searcher) searcherClassRegistry.get(type).newInstance();
                } catch (InstantiationException e) {
                    throw new IllegalStateException(e);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
                searcherInstanceCache.put(type, searcher);
                return searcher;
            }
            type = type.getSuperclass();
        }

        throw new IllegalArgumentException("Could not find a suitable " + Searcher.class.getName() + " implementation for " + subject.getClass().getName());
    }

    public void findIt(JComponent subject) {
        initialize(subject);
        finderController().show();
    }

    public void findPrevious(JComponent subject) {
        initialize(subject);
        if (isBlank(finderModel().getToFind())) {
            finderController().show();
        } else {
            finderController().findPrevious();
        }
    }

    public void findNext(JComponent subject) {
        initialize(subject);
        if (isBlank(finderModel().getToFind())) {
            finderController().show();
        } else {
            finderController().findNext();
        }
    }

    public void replace(JComponent subject) {
        initialize(subject);
        if (isBlank(finderModel().getReplaceWith())) {
            finderController().show();
        } else {
            finderController().replace();
        }
    }

    public void replaceAll(JComponent subject) {
        initialize(subject);
        if (isBlank(finderModel().getReplaceWith())) {
            finderController().show();
        } else {
            finderController().replaceAll();
        }
    }

    private void initialize(JComponent subject) {
        if (getApplication().getGroups().get(FINDER_GROUP_NAME) == null) {
            getApplication().createMVCGroup(FINDER_GROUP_NAME);
        }
        finderModel().setSubject(subject);
    }

    private FinderController finderController() {
        return (FinderController) getApplication().getControllers().get(FINDER_GROUP_NAME);
    }

    private FinderModel finderModel() {
        return (FinderModel) getApplication().getModels().get(FINDER_GROUP_NAME);
    }

    private void loadSearchers() {
        try {
            Enumeration<URL> urls = Finder.class.getClassLoader().getResources("META-INF/services" + Searcher.class.getName());
            ConfigSlurper slurper = new ConfigSlurper();
            while (urls.hasMoreElements()) {
                processURL(urls.nextElement(), slurper);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void processURL(URL url, ConfigSlurper slurper) {
        Properties p = new Properties();
        try {
            p.load(url.openStream());
        } catch (IOException e) {
            return;
        }

        ConfigObject config = slurper.parse(p);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Loading Searcher definitions from " + url);
        }

        for (Object key : config.keySet()) {
            String type = key.toString();
            String className = (String) config.get(type);
            try {
                registerSearcher(Class.forName(type), Class.forName(className));
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
