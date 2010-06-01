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

import groovy.beans.Bindable
import java.beans.PropertyChangeListener
import javax.swing.DefaultComboBoxModel
import griffon.lookandfeel.LookAndFeelInfo
import griffon.lookandfeel.LookAndFeelProvider
import griffon.lookandfeel.LookAndFeelManager

/**
 * @author Andres Almiray
 */
class LookAndFeelSelectorModel {
    final DefaultComboBoxModel providerModel = new DefaultComboBoxModel()
    final DefaultComboBoxModel lafInfoModel = new DefaultComboBoxModel()

    @Bindable LookAndFeelProvider providerSelection
    @Bindable LookAndFeelInfo lafSelection

    LookAndFeelSelectorModel() {
        LookAndFeelManager.instance.lookAndFeelProviders.sort().each { provider ->
            providerModel << provider
        }

        addPropertyChangeListener('providerSelection', updateLafInfoModel as PropertyChangeListener)
    }

    private updateLafInfoModel = { e = null ->
        if(!providerSelection) return
        lafInfoModel.removeAllElements()
        providerSelection.supportedLookAndFeels.sort().each { laf ->
            lafInfoModel << laf
        }
    }
}
