/*
 * griffon-nuvola: Nuvola icons Grifofn plugin
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
import groovy.swing.SwingBuilder
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.util.concurrent.CountDownLatch

includeTargets << griffonScript("Init")

target(nuvolaIconSelector: "Displays all Nuvola icons") {
    def categories = [:]
    def pathResolver = new PathMatchingResourcePatternResolver(this.class.classLoader)
    pathResolver.getResources('classpath*:/nuvola/icons/16x16/**/*.png').each { r ->
        def (m, category, icon) = (r.getURL().path =~ /.*\/(\w+)\/([0-9a-zA-Z_\-\+]+)\.png/)[0]
        categories.get(category, []) << icon
    }
    
    // hack to avoid script from terminating before time
    CountDownLatch latch = new CountDownLatch(1i)
    def swing = new SwingBuilder()
    swing.edt {
        swing.frame(title: 'Nuvola Icons', pack: true, visible: true, resizable: false,
                    defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE,
                    windowClosed: { latch.countDown() }) {
            borderLayout()
            tabbedPane(constraints: context.NORTH, preferredSize: [320, 420]) {
                categories.each { category, icons ->
                    scrollPane(title: category) {
                        panel {
                            gridLayout(cols: 4, rows: icons.size())
                            icons.each { icon ->
                                label(icon: imageIcon("/nuvola/icons/16x16/${category}/${icon}.png"), toolTipText: icon)
                                try {
                                    label(icon: imageIcon("/nuvola/icons/22x22/${category}/${icon}.png"), toolTipText: icon)
                                } catch(x) { label '' }
                                try {
                                    label(icon: imageIcon("/nuvola/icons/32x32/${category}/${icon}.png"), toolTipText: icon)
                                } catch(x) { label '' }
                                try {
                                    label(icon: imageIcon("/nuvola/icons/48x48/${category}/${icon}.png"), toolTipText: icon)
                                } catch(x) { label '' }
                            }
                        } 
                    }
                }
            }
        }
    }

    latch.await()
}
setDefaultTarget(nuvolaIconSelector)
