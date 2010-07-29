/*
 * griffon-lookandfeel-quaqua: Quaqua Look&Feel for Griffon
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

/**
 * @author Andres Almiray
 */

// check to see if we already have a LookandfeelQuaquaGriffonAddon
boolean addonIsSet1
builderConfig.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'LookandfeelQuaquaGriffonAddon' == builder
    }
}

if (!addonIsSet1) {
    println 'Adding LookandfeelQuaquaGriffonAddon to Builder.groovy'
    builderConfigFile.append('''
root.'LookandfeelQuaquaGriffonAddon'.addon=true
''')
}
