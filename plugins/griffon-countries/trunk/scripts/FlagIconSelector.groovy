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
import java.util.concurrent.CountDownLatch
import griffon.countries.Country

includeTargets << griffonScript("Init")

target(flagIconSelector: "Displays all Flag icons") {
    def countries = Country.COUNTRIES
    // hack to avoid script from terminating before time
    CountDownLatch latch = new CountDownLatch(1i)
    def swing = new SwingBuilder()
    swing.edt {
        swing.frame(title: 'Flags of the World', pack: true, visible: true, resizable: true,
                    defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE,
                    windowClosed: { latch.countDown() }) {
            scrollPane(preferredSize: [400, 420]) {
                panel {
                    gridLayout(cols: 1, rows: countries.size())
                    countries.each { country ->
                        try {
                            label(icon: imageIcon("/com/famfamfam/flags/icons/${country.code.toLowerCase()}.png"),
                                  "[$country.code] $country.name")
                        } catch(x) {
                            // missing icon
                            println "No icon found for $country"
                        }
                    }
                } 
            }
        }
    }

    latch.await()
}

setDefaultTarget('flagIconSelector')
