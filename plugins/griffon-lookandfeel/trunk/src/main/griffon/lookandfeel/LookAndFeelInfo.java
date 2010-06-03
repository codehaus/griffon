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

/**
 * @author Andres Almiray
 */
public abstract class LookAndFeelInfo implements Comparable {
    private final String identifier;
    private final String displayName;

    public LookAndFeelInfo(String identifier, String displayName) {
        this.identifier = identifier;
        this.displayName = displayName;
    }

    public final String getIdentifier() {
        return identifier;
    }

    public final String getDisplayName() {
        return displayName;
    }

    public String toString() {
        return displayName;
    }

    public final int hashCode() {
        return identifier.hashCode();
    }

    public final boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(!(obj instanceof LookAndFeelInfo)) return false;
        
        LookAndFeelInfo other = (LookAndFeelInfo) obj;
        return identifier.equals(other.identifier);
    }

    public final int compareTo(Object obj) {
        if(obj == null) return -1;
        if(obj == this) return 0;
        if(!(obj instanceof LookAndFeelInfo)) return -1;

        LookAndFeelInfo other = (LookAndFeelInfo) obj;
        return identifier.compareTo(other.identifier);
    }

    public abstract boolean isCurrentLookAndFeel();

    public abstract void install();
}
