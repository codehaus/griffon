import org.codehaus.griffon.commons.GriffonClassUtils as GCU

includeTargets << griffonScript("Init")
includeTargets << griffonScript("CreateIntegrationTest")

target(default: "Creates a new Document Group") {
    createDocumentGroup()
}

target(createDocumentGroup: "Creates a new Document Group") {
	depends(checkVersion, parseArguments)
	promptForName(type: "Document Group")
	def (pkg, name) = extractArtifactName(argsMap["params"][0])
	def fqn = "${pkg?pkg:''}${pkg?'.':''}${GCU.getClassNameRepresentation(name)}"

	// create our artifacts
	createArtifact(
		name: fqn,
		suffix: "Model",
		type: "DocumentModel",
		path: "griffon-app/models")
		
	createArtifact(
		name: fqn,
		suffix: "State",
		type: "DocumentState",
		path: "griffon-app/models")
	ant.replace(file: "${basedir}/griffon-app/models/${fqn.replace('.' as char, '/' as char)}State.groovy") {
		replacefilter(token: "@document.group@", value: name)
	}
	
	createArtifact(
		name: fqn,
		suffix: "View",
		type: "DocumentView",
		path: "griffon-app/views")

	createArtifact(
		name: fqn,
		suffix: "Actions",
		type: "DocumentActions",
		path: "griffon-app/views")
		
	createArtifact(
		name: fqn,
		suffix: "Controller",
		type: "DocumentController",
		path: "griffon-app/controllers")

	// append some helper content
	append(griffonAppName, 'Model', """
// def state = new ${fqn}State()
""")
	append(griffonAppName, 'View', """
/*
// STEP 1: add this line before the application node to include the standard 
//     document-manipulation actions, e.g. new, open, save, close
build(${fqn}Actions)

// STEP 2: add this block inside the application node to wire up the standard 
//     File menu structure
menuBar(id: 'menuBar') {
	menu(text: 'File', mnemonic: 'F') {
		menuItem(newDocumentAction)
		menuItem(openDocumentAction)
		separator()
		menuItem(closeDocumentAction)
		separator()
		menuItem(saveDocumentAction)
	}
}
			
// STEP 3: add this line inside the application node to provide a place 
//     for the open documents to reside
tabbedPane(id: 'documents', stateChanged: { evt -> model.state.activeDocumentChanged() })

// STEP 4: add this line outside the application node
model.state.tabs = documents
model.state.app = app

// STEP 5: update your model
Update ${griffonAppName}Model.groovy to create the state object
*/
""")

	// register our document MVC group in Application.groovy
	def applicationConfigFile = new File("${basedir}/griffon-app/conf/Application.groovy")
	def configText = applicationConfigFile.text
	if (!(configText =~ /\s*mvcGroups\s*\{/)) {
		configText += """
mvcGroups {
}
"""
	}
	        
	applicationConfigFile.withWriter { it.write configText.replaceAll(/\s*mvcGroups\s*\{/, """
mvcGroups {
	// MVC Group for "$name"
	'$name' {
		model = '${fqn}Model'
		controller = '${fqn}Controller'
		view = '${fqn}View'
	}""") }
	
	print '\n\n'
	printFramed("Please review your ${griffonAppName}Model.groovy and ${griffonAppName}View.groovy files\nfor additional steps needed to complete multi-document support") 
}

private append(name, type, content) {
	new File("${basedir}/griffon-app/${type.toLowerCase()}s").eachFile { file ->
		if (file.name == "${name}${type}.groovy") {
			file.append(content)
		}
	}
}

private static void printFramed(s, c = '*', padded = false) {
	def pieces = s.split('\n').collect { it.replace('\t',' ') }
	def length = pieces*.size().max() + 4
	def frame = c * length
	def result = pieces.collect {
		def blank = ' ' * (length - 4 - it.size())
		"${c} ${it}${blank} ${c}\n"
	}.join()
	result = "${frame}\n${result}${frame}\n"
	if (padded) result = "\n${result}\n"
	print result
} 