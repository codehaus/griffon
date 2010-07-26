/*
 * griffon-nuvola: Nuvola icons Grifofn plugin
 * Copyright 2010 and beyond, Andres Almiray
 *
 * SmartGWT is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  SmartGWT is also
 * available under typical commercial license terms - see
 * smartclient.com/license.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package griffon.nuvola

import groovy.swing.factory.ImageIconFactory

/**
 * @author Andres.Almiray
 */
class NuvolaIconFactory extends ImageIconFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        String size = attributes.remove('size') ?: '16'
        String category = attributes.remove('category') ?: 'actions'
        String icon = attributes.remove('icon') ?: value 
  
        if(!icon) throw new IllegalArgumentException("In $name you must define a node value or icon:")

        size = parseSize(size)
        if(icon.endsWith('.png')) icon -= '.png'

        value = "/nuvola/icons/${size}/${category}/${icon}.png"
        super.newInstance(builder, name, value, [:])
    }

    private String parseSize(String size) {
        switch(size) {
            case '16':
            case '22':
            case '32':
            case '48':
            case '64':
            case '128':
                return size +'x'+ size
        }
        return '16x16'
    }
}
