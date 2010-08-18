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

package griffon.lookandfeel

import java.awt.Component
import java.awt.Window
import javax.swing.UIManager
import javax.swing.LookAndFeel
import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import griffon.core.GriffonApplication

/**
 * @author Andres Almiray
 */
@Singleton
final class LookAndFeelManager {
    private final List<LookAndFeelProvider> providers = []

    void loadLookAndFeelProviders() {
        // do it once
        if(providers) return

        ClassLoader cl = Thread.currentThread().contextClassLoader
        Enumeration urls = cl.getResources('META-INF/griffon-lookandfeel.properties')
        urls.each { url ->
            url.eachLine { text ->
                String providerClassName = text.trim()
                try {
                    Class providerClass = cl.loadClass(providerClassName)
                    providers << providerClass.newInstance()
                } catch(NoClassDefFoundError ncdfe) {
                    // skip
                    ncdfe.printStackTrace()
                } catch(ClassNotFoundException cnfe) {
                    // skip
                    cnfe.printStackTrace()
                }
            }
        }
    }

    LookAndFeelProvider[] getLookAndFeelProviders() {
        if(!providers) loadLookAndFeelProviders()
        providers.toArray(new LookAndFeelProvider[providers.size()])
    }

    LookAndFeelProvider getLookAndFeelProvider(String name) {
        for(provider in getLookAndFeelProviders()) {
            if(provider.name == name) {
                return provider
            } 
        }
        return null
    }

    griffon.lookandfeel.LookAndFeelInfo getLookAndFeelInfo(LookAndFeelProvider provider, String displayName) {
        if(!provider) return null
        for(lookAndFeelInfo in provider.supportedLookAndFeels) {
            if(lookAndFeelInfo.displayName == displayName) {
                return lookAndFeelInfo
            } 
        }
        return null
    }

    LookAndFeelProvider getCurrentLookAndFeelProvider() {
        for(provider in getLookAndFeelProviders()) {
            if(provider.handles(UIManager.lookAndFeel)) {
                return provider
            } 
        }
        return null
    }

    griffon.lookandfeel.LookAndFeelInfo getCurrentLookAndFeelInfo() {
        LookAndFeelProvider provider = getCurrentLookAndFeelProvider()
        if(!provider) return null
        for(lookAndFeelInfo in provider.supportedLookAndFeels) {
            if(lookAndFeelInfo.isCurrentLookAndFeel()) {
                return lookAndFeelInfo
            }
        }
        return null
    }

    void preview(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo, Component component, GriffonApplication application) {
        for(provider in getLookAndFeelProviders()) {
            if(provider.handles(lookAndFeelInfo)) {
                application.event('LookAndFeelPreview', [getCurrentLookAndFeelProvider(), getCurrentLookAndFeelInfo(), lookAndFeelInfo]) 
                provider.preview(lookAndFeelInfo, component)
                break
            }
        }
    }

    void apply(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo, GriffonApplication application) {
        for(provider in getLookAndFeelProviders()) {
            if(provider.handles(lookAndFeelInfo)) {
                application.event('LookAndFeelApply', [getCurrentLookAndFeelProvider(), getCurrentLookAndFeelInfo(), lookAndFeelInfo]) 
                provider.apply(lookAndFeelInfo, application)
                break
            }
        }
    }

    void installLookAndFeel(LookAndFeel lookAndFeel, GriffonApplication application) {
        SwingUtilities.invokeLater {
            UIManager.setLookAndFeel(lookAndFeel)
            for(Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window)
            }
            application.event('LookAndFeelChanged', [getCurrentLookAndFeelProvider(), getCurrentLookAndFeelInfo(), lookAndFeel]) 
        }
    }

    void showLafDialog(GriffonApplication application) {
        // already showing?
        if(application.groups['LookAndFeelSelector']) return

        def currentLaf = getCurrentLookAndFeelInfo()
        def systemProvider = getLookAndFeelProvider('System')
        def systemLaf = getLookAndFeelInfo(systemProvider, 'System')

        def (m, v, c) = application.createMVCGroup('LookAndFeelSelector')
        c.setCurrentLookAndFeel(getCurrentLookAndFeelProvider())

        int option = JOptionPane.showConfirmDialog(null, v.box,
                         'Look & Feel Selection',
                         JOptionPane.OK_CANCEL_OPTION,
                         JOptionPane.PLAIN_MESSAGE)
        if(option == JOptionPane.OK_OPTION) {
            apply(m.lafSelection, application)
        } else {
            if(m.reset) {
                apply(systemLaf, application)
            } else {
                apply(currentLaf, application)
            }
        }

        application.destroyMVCGroup('LookAndFeelSelector')
    }
}
