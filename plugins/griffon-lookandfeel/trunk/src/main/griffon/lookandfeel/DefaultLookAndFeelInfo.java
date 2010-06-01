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

package griffon.lookandfeel;

import java.awt.Component;
import javax.swing.UIManager;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author Andres Almiray
 */
public abstract class DefaultLookAndFeelInfo extends LookAndFeelInfo {
    private final LookAndFeel lookAndFeel;

    public DefaultLookAndFeelInfo(String identifier, String displayName, LookAndFeel lookAndFeel) {
        super(identifier, displayName);
        this.lookAndFeel = lookAndFeel;
    }

    public final LookAndFeel getLookAndFeel() {
        return lookAndFeel;
    }

    public void preview(Component component) {
        try {
            UIManager.setLookAndFeel(lookAndFeel);
            SwingUtilities.updateComponentTreeUI(component);
        } catch(UnsupportedLookAndFeelException ulafe) {
            // ignore
        }
    }

    public boolean isCurrentLookAndFeel() {
        LookAndFeel currentLookAndFeel = UIManager.getLookAndFeel();
        if(currentLookAndFeel == null) return false;
        return lookAndFeel.getClass().getName().equals(currentLookAndFeel.getClass().getName());
    }
}
