/* --------------------------------------------------------------------
   griffon-domain-db4o plugin
   Copyright (C) 2010 Andres Almiray

   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License as
   published by the Free Software Foundation; either version 2 of the
   License, or (at your option) any later version.

   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this library; if not, see <http://www.gnu.org/licenses/>.
   ---------------------------------------------------------------------
*/
package griffon.domain.db4o.query

import com.db4o.query.Predicate

/**
 * @author Andres Almiray
 */
class PropertiesPredicate<O> extends Predicate<O> {
    private final Map props = [:]

    PropertiesPredicate(Map props) {
       if(props) this.props.putall(props)
    }

    public boolean match(O o) {
        MetaClass mc = o.metaClass
        props.each { propertyName, propertyValue ->
            if(!mc.hasProperty(propertyName, o)) return false
            if(o[propertyName] != propertyValue) return false
        }
        return true
    }
}
