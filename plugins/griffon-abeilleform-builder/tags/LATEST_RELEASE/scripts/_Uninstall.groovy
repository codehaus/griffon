/*
 * griffon-abeilleformbuilder: AbeilleForms Griffon plugin
 * Copyright 2010 and beyond, Jim Shingler
 *
 * AbeilleForms is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

/**
 * @author Jim Shingler
 */

boolean addonIsSet1
builderConfig.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'griffon.builder.abeilleform.AbeilleFormBuilder' == builder
    }
}

if(addonIsSet1) {
    builderConfigFile.text = builderConfigFile.text - "root.'griffon.builder.abeilleform.AbeilleFormBuilder'.view = '*'\n"
}

