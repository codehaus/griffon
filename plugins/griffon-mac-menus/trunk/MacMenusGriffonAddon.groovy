import org.codehaus.griffon.macmenus.other.AboutMenuItemFactory
import org.codehaus.griffon.macmenus.other.PreferencesMenuItemFactory

import griffon.util.GriffonApplicationUtils

class MacMenusGriffonAddon {


    // lifecycle methods

    // called once, after the addon is created
    def addonInit(app) {
        if (GriffonApplicationUtils.isMacOSX) {
            // invoke via reflection so we don't have verifier errors
            factories.aboutMenuItem = Class.forName('org.codehaus.griffon.macmenus.mac.MacAboutMenuItemFactory').newInstance()
            factories.preferencesMenuItem = Class.forName('org.codehaus.griffon.macmenus.mac.MacPreferencesMenuItemFactory').newInstance()
        }
    }

    // called once, after all addons have been inited
    //def addonPostInit(app) {
    //}

    // called many times, after creating a builder
    //def addonInit(app) {
    //}

    // called many times, after creating a builder and after
    // all addons have been inited
    //def addonPostInit(app) {
    //}


    // to add MVC Groups use create-mvc


    // builder fields, these are added to all builders.
    // closures can either be literal { it -> println it}
    // or they can be method closures: this.&method

    // adds methods to all builders
    //def methods = [
    //    methodName: { /*Closure*/ }
    //]

    // adds properties to all builders
    //def props = [
    //    propertyName: [
    //        get: { /* optional getter closuer},
    //        set: {val-> /* optional setter closuer},
    //  ]
    //]

    // adds new factories to all builders
    def factories = [
        aboutMenuItem : new AboutMenuItemFactory(),
        preferencesMenuItem : new PreferencesMenuItemFactory()
    ]


    //TODO enumerate FactoryBuilderSupporte delegate closures
    def mvcGroups = [
        // MVC Group for "MacAboutDialog"
        'MacAboutDialog' : [
            model : 'org.codehaus.griffon.macmenus.MacAboutDialogModel',
            view : 'org.codehaus.griffon.macmenus.MacAboutDialogView',
            controller : 'org.codehaus.griffon.macmenus.MacAboutDialogController'
        ]
    
    ]
}
