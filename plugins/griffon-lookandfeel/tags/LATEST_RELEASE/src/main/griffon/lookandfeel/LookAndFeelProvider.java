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
import javax.swing.LookAndFeel;
import griffon.core.GriffonApplication;

/**
 * @author Andres Almiray
 */
public abstract class LookAndFeelProvider implements Comparable {
    private final String name;

    public LookAndFeelProvider(String name) {
        this.name = name;
    }

    public final String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public final int hashCode() {
        return name.hashCode();
    }

    public final boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(!(obj instanceof LookAndFeelProvider)) return false;

        LookAndFeelProvider other = (LookAndFeelProvider) obj;
        return name.equals(other.name);
    }

    public final int compareTo(Object obj) {
        if(obj == null) return -1;
        if(obj == this) return 0;
        if(!(obj instanceof LookAndFeelProvider)) return -1;

        LookAndFeelProvider other = (LookAndFeelProvider) obj;
        return name.compareTo(other.name);
    }

    public abstract boolean handles(LookAndFeel lookAndFeel);

    public abstract boolean handles(LookAndFeelInfo lookAndFeelInfo);

    public abstract LookAndFeelInfo[] getSupportedLookAndFeels();

    public abstract void preview(LookAndFeelInfo lookAndFeelInfo, Component target);

    public abstract void apply(LookAndFeelInfo lookAndFeelInfo, GriffonApplication application);
}
