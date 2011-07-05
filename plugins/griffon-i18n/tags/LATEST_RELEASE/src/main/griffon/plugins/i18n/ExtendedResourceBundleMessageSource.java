/*
 * Copyright 2010-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.plugins.i18n;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Andres Almiray
 */
public class ExtendedResourceBundleMessageSource extends ResourceBundleMessageSource {
    private static final Logger LOG = LoggerFactory.getLogger(ExtendedResourceBundleMessageSource.class);
    private boolean resolved;
    private PathMatchingResourcePatternResolver resolver;
    private final Object[] lock = new Object[0];

    public void resolver() {
        synchronized (lock) {
            if (resolved) return;
            createResolver();
            resolved = true;
        }
    }

    private void createResolver() {
        resolver = new PathMatchingResourcePatternResolver(getBundleClassLoader());
    }

    @Override
    protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
        resolver();
        List<ResourceBundle> bundles = new ArrayList<ResourceBundle>();
        bundles.addAll(loadBundleForFilename(basename + "_" + locale.getLanguage() + "_" + locale.getCountry()));
        bundles.addAll(loadBundleForFilename(basename + "_" + locale.getLanguage()));
        bundles.addAll(loadBundleForFilename(basename));
        return new CompositeResourceBundle(bundles.toArray(new ResourceBundle[bundles.size()]));
    }

    private List<ResourceBundle> loadBundleForFilename(String filename) {
        List<ResourceBundle> bundles = new ArrayList<ResourceBundle>();
        try {
            Resource[] resources = resolver.getResources("classpath*:/" + filename + ".properties");
            LOG.trace("Found " + resources.length + " matches for classpath *:/" + filename + ".properties");
            for (Resource resource : resources) {
                LOG.trace("Initializing bundle with " + resource.getURI());
                bundles.add(new PropertyResourceBundle(resource.getInputStream()));
            }
        } catch (IOException e) {
            // ignore
        }
        return bundles;
    }
}
