/*
 * Copyright 2010 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sourceforge.gvalidation.util

import griffon.spock.UnitSpec
import javax.swing.JDialog
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.JFrame

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class GriffonWindowManagerSpec extends UnitSpec {

    def 'Window manager should be able retrieve top level JDialog'(){
        given:
        def windowManager = new GriffonWindowManager()
        def frame = new JFrame()
        def jdialog = new JDialog(frame)
        def panel = new JPanel()
        jdialog.getRootPane().add(panel)
        def component = new JLabel()
        panel.add(component)

        when:
        def window = windowManager.getWindow(component)

        then:
        window == jdialog
    }

    def 'Window manager should be able to retrieve top level JFrame'(){
        given:
        def windowManager = new GriffonWindowManager()
        def frame = new JFrame()
        def panel = new JPanel()
        frame.rootPane.add(panel)
        def component = new JLabel()
        panel.add(component)

        when:
        def window = windowManager.getWindow(component)

        then:
        window == frame
    }

    def 'Window manager should return null for non-component'(){
        given:
        def windowManager = new GriffonWindowManager()

        when:
        def window = windowManager.getWindow("")

        then:
        window == null
    }

    def 'Window manager should return null if component is a orphan'(){
        given:
        def windowManager = new GriffonWindowManager()
        def panel = new JPanel()
        def component = new JLabel()
        panel.add(component)

        when:
        def window = windowManager.getWindow(component)

        then:
        window == null
    }

}
