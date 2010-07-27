/*
 * griffon-crystal: Crystal icons Grifofn plugin
 * Copyright 2010 and beyond, Andres Almiray
 *
 * SmartGWT is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  SmartGWT is also
 * available under typical commercial license terms - see
 * smartclient.com/license.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

/**
 * @author Andres Almiray
 */

import javax.swing.JFrame
import javax.swing.JTabbedPane
import groovy.swing.SwingBuilder
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.util.concurrent.CountDownLatch

includeTargets << griffonScript("Init")

target(crystalIconSelector: "Displays all Crystal icons") {
    def categories = [:]
    def pathResolver = new PathMatchingResourcePatternResolver(this.class.classLoader)
    ['16', '22', '24', '32', '48', '64', '128'].each { s ->
        pathResolver.getResources("classpath*:/com/everaldo/crystal/${s}x${s}/**/*.png").each { r ->
            def (m, category, icon) = (r.getURL().path =~ /.*\/(\w+)\/([0-9a-zA-Z_\-\+&\ ]+)\.png/)[0]
            def map = categories.get(category, [:])
            def key = s+"x"+s
            map.get(key, []) << icon
        }
    }
    
    // hack to avoid script from terminating before time
    CountDownLatch latch = new CountDownLatch(1i)
    def swing = new SwingBuilder()
    swing.edt {
        frame(title: 'Crystal Icons', pack: true, visible: true, resizable: true,
                    defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE,
                    windowClosed: { latch.countDown() }) {
            borderLayout()
            tabbedPane(preferredSize: [620, 480]) {
                categories.each { category, sizes ->
                    scrollPane(title: category) {
                        tabbedPane(tabPlacement: JTabbedPane.TOP) {
                            sizes.each { size, icons ->
                                scrollPane(title: size) {
                                    panel {
                                        gridLayout(cols: 4, rows: (icons.size()/4)+1)
                                        icons.each { icon ->
                                            label(icon: imageIcon("/com/everaldo/crystal/${size}/${category}/${icon}.png"), toolTipText: icon)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    latch.await()
}
setDefaultTarget(crystalIconSelector)
