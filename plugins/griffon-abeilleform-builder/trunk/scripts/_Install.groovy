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

// check to see if we already have a AbeilleForm Builder
boolean builderIsSet
builderConfig.each() { prefix, v ->
    v.each { builder, views ->
        builderIsSet = builderIsSet || 'griffon.builder.abeilleform.AbeilleFormBuilder' == builder
    }
}

if (!builderIsSet) {
    println 'Adding AbeilleFormBuilder to Builder.groovy'
    builderConfigFile.append('''
root.'griffon.builder.abeilleform.AbeilleFormBuilder'.view = '*'
''')
}
