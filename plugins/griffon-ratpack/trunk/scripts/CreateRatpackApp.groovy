includeTargets << griffonScript("_GriffonInit")
includeTargets << griffonScript("_GriffonCreateArtifacts")

target('createRatpackApp': "Creates a new domain class") {
    depends(checkVersion, parseArguments)

    ant.mkdir(dir:"${basedir}/griffon-app/ratpack")

    def type = "RatpackApp"
    promptForName(type: type)

    def name = argsMap["params"][0]
    createArtifact(name: name, suffix: "RatpackApp", type: type, path: "griffon-app/ratpack")
}

setDefaultTarget('createRatpackApp')
