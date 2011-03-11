/*
 * griffon-mouseguestures: MouseGestures Griffon plugin
 * Copyright 2010 and beyond, Andres Almiray
 *
 * MouseGestures is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

/**
 * @author Andres Almiray
 */

// check to see if we already have a MousegesturesGriffonAddon
boolean addonIsSet1
builderConfig.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'MousegesturesGriffonAddon' == builder
    }
}

if (addonIsSet1) {
    println 'Removing MousegesturesGriffonAddon from Builder.groovy'
    builderConfigFile.text = builderConfigFile.text - "root.'MousegesturesGriffonAddon'.addon=true\n"
}
