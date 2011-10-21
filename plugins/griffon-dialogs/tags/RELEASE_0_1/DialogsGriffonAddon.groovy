/*
 * griffon-dialogs: Dialog provider Griffon plugin
 * Copyright 2010 and beyond, Andres Almiray
 *
 * griffon-dialogs is free software; you can redistribute it and/or modify it
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
class DialogsGriffonAddon {
    def mvcGroups = [
        finder: [
            model     : 'griffon.plugins.dialogs.FinderModel',
            view      : 'griffon.plugins.dialogs.FinderView',
            controller: 'griffon.plugins.dialogs.FinderController'
        ]
    ]
}
