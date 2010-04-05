import net.sourceforge.gvalidation.ValidationEnhancer

class ValidationGriffonAddon {
    
    def addonInit = {app ->
        app.addApplicationEventListener(this)
    }

    def onNewInstance = {klass, type, instance ->
        if (type == "model")
            ValidationEnhancer.enhance(instance)
    }
    
}
