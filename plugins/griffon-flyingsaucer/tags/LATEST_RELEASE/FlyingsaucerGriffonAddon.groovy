/*
 * Copyright 2010 the original author or authors.
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

import griffon.flyingsaucer.factory.XhtmlPanelFactory
import groovy.swing.factory.ScrollPaneFactory

import org.xhtmlrenderer.simple.XHTMLPanel
import org.xhtmlrenderer.simple.FSScrollPane
import org.xhtmlrenderer.swing.ScalableXHTMLPanel

/**
 * @author Andres Almiray
 */
class FlyingsaucerGriffonAddon {
    def factories = [
        xhtmlPanel: new XhtmlPanelFactory(XHTMLPanel),
        scalableXhtmlPanel: new XhtmlPanelFactory(ScalableXHTMLPanel),
        fsScrollPane: new ScrollPaneFactory(FSScrollPane)
    ]
}
