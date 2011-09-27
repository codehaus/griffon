/*
 * Copyright 2008-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import griffon.util.ApplicationHolder;
import griffon.swing.factory.ApplicationFactory;
import griffon.swing.factory.RootFactory;
import org.codehaus.griffon.runtime.core.AbstractGriffonAddon;

/**
 * @author Andres Almiray
 */
public class SwingGriffonAddon extends AbstractGriffonAddon {
    public SwingGriffonAddon() {
        super(ApplicationHolder.getApplication());
        factories.put("application", new ApplicationFactory());
        factories.put("root", new RootFactory());
    }
}
