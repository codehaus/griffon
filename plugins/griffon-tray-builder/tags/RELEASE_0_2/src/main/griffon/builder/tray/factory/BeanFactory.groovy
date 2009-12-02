/* --------------------------------------------------------------------
   TrayBuilder
   Copyright (C) 2008 Andres Almiray

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

package griffon.builder.tray.factory

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class BeanFactory extends AbstractFactory {
    final Class beanClass
    final protected boolean leaf

    public BeanFactory(Class beanClass) {
        this(beanClass, false)
    }

    public BeanFactory(Class beanClass, boolean leaf) {
        this.beanClass = beanClass
        this.leaf = leaf
    }

    public boolean isLeaf() {
        return leaf
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if (value instanceof GString) value = value as String
        if (FactoryBuilderSupport.checkValueIsTypeNotString(value, name, beanClass)) {
            return value
        }
        return beanClass.newInstance()
    }
}
