/*
 * Copyright 2010 the original author or authors.
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

/**
 * @author Andres.Almiray
 */

import javax.swing.JFrame
import groovy.swing.SwingBuilder
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.util.concurrent.CountDownLatch

includeTargets << griffonScript("Init")

target(tangoIconSelector: "Displays all Tango! icons") {
    def categories = [:]
    def pathResolver = new PathMatchingResourcePatternResolver(this.class.classLoader)
    pathResolver.getResources('classpath*:/org/freedesktop/tango/16x16/**/*.png').each { r ->
        def (m, category, icon) = (r.getURL().path =~ /.*\/(\w+)\/([a-zA-Z_\-]+)\.png/)[0]
        categories.get(category, []) << icon
    }
    
    // hack to avoid script from terminating before time
    CountDownLatch latch = new CountDownLatch(1i)
    def swing = new SwingBuilder()
    swing.edt {
        swing.frame(title: 'Tango! Icons', pack: true, visible: true, resizable: false,
                    defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE,
                    windowClosed: { latch.countDown() }) {
            borderLayout()
            tabbedPane(constraints: context.NORTH, preferredSize: [320, 420]) {
                categories.each { category, icons ->
                    scrollPane(title: category) {
                        panel {
                            gridLayout(cols: 3, rows: icons.size())
                            icons.each { icon ->
                                label(icon: imageIcon("/org/freedesktop/tango/16x16/${category}/${icon}.png"), toolTipText: icon)
                                label(icon: imageIcon("/org/freedesktop/tango/22x22/${category}/${icon}.png"), toolTipText: icon)
                                label(icon: imageIcon("/org/freedesktop/tango/32x32/${category}/${icon}.png"), toolTipText: icon)
                            }
                        } 
                    }
                }
            }
        }
    }

    latch.await()
}
setDefaultTarget(tangoIconSelector)
