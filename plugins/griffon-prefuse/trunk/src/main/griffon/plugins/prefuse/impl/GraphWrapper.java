/*
 * Copyright 2011 the original author or authors.
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

package griffon.plugins.prefuse.impl;

import prefuse.data.Graph;
import prefuse.data.Table;

/**
 * @author Andres Almiray
 */
public class GraphWrapper extends Graph implements Named {
    private String name;

    public GraphWrapper() {
    }

    public GraphWrapper(boolean b) {
        super(b);
    }

    public GraphWrapper(Table table, boolean b) {
        super(table, b);
    }

    public GraphWrapper(Table table, boolean b, String s, String s1, String s2) {
        super(table, b, s, s1, s2);
    }

    public GraphWrapper(Table table, Table table1, boolean b) {
        super(table, table1, b);
    }

    public GraphWrapper(Table table, Table table1, boolean b, String s, String s1) {
        super(table, table1, b, s, s1);
    }

    public GraphWrapper(Table table, Table table1, boolean b, String s, String s1, String s2) {
        super(table, table1, b, s, s1, s2);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
