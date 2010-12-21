
includeTargets << griffonScript("_GriffonInit")
includeTargets << griffonScript("_GriffonCreateArtifacts")

target(createChart3D: "Creates a new Chart3D script") {
    depends(checkVersion, parseArguments)

    promptForName(type: "Chart3D")

    def name = argsMap["params"][0]
    createArtifact(name: name,
        suffix: "Chart3D",
        type: "Chart3D",
        path: "griffon-app/charts")
}
setDefaultTarget('createChart3D')
