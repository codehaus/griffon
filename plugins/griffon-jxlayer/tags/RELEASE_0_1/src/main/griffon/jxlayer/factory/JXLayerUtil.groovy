/*
 * Copyright 2009-2010 the original author or authors.
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
 */

package griffon.jxlayer.factory

// import org.jdesktop.jxlayer.JXLayer
// import org.jdesktop.jxlayer.plaf.LayerUI

/**
 * @author Andres Almiray
 */
final class JXLayerUtil {
    private JXLayerUtil() {}

    static Class findJXLayerClass() {
        findClass(['javax.swing.JLayer',
                   'org.jdesktop.jxlayer.JXLayer'])
    }

    static Class findLayerUIClass() {
        findClass(['javax.swing.plaf.LayerUI',
                   'org.jdesktop.jxlayer.plaf.LayerUI'])
    }

    static boolean isLayerUI(obj) {
        findLayerUIClass().isAssignableFrom(obj?.getClass())
    }

    private static Class findClass(classes = []) {
        for (klass in classes) {
            try {
                return Class.forName(klass)
            } catch (Throwable t) {
                // ignore it, try the next on the list
            }
        }
        return null
    }
}
