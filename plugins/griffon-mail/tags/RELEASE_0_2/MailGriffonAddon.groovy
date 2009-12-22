import griffon.util.IGriffonApplication

class MailGriffonAddon {
	private IGriffonApplication application
	
	// add ourselves as a listener so we can respond to events
	def addonInit = { app ->
		application = app
		app.addApplicationEventListener(this)
	}
	
	// inject the 'sendMail' into our 
	def onNewInstance = { klass, type, instance ->
		def types = application.config.griffon?.mail?.injectInto ?: ["controller"]
		if (!types.contains(type)) return
		instance.metaClass.sendMail = { Map args -> MailService.sendMail(args) }
	}
}