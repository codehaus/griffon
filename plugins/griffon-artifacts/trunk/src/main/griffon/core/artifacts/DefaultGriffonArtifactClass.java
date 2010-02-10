/* 
 * Copyright 2004-2010 Graeme Rocher
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
package griffon.core.artifacts;

import griffon.core.ArtifactInfo;

/**
 * A default implementation for Griffon classes that need to be registered and managed by a GriffonApplication,
 * but don't need any special handling.
 *
 * @author Graeme Rocher
 * @since 0.1
 * 
 */
public class DefaultGriffonArtifactClass extends AbstractGriffonArtifactClass {
    /**
     * <p>Contructor to be used by all child classes to create a
     * new instance and get the name right.
     *
     * @param artifactInfo the Griffon artifact
     * @param trailingName the trailing part of the name for this class type
     */
    public DefaultGriffonArtifactClass(ArtifactInfo artifactInfo, String trailingName) {
        super(artifactInfo, trailingName);
    }

    public DefaultGriffonArtifactClass(ArtifactInfo artifactInfo) {
        super(artifactInfo, "");
    }
}
