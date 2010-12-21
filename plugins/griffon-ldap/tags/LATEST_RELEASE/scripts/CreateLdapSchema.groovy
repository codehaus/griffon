
includeTargets << griffonScript("_GriffonInit")
includeTargets << griffonScript("_GriffonCreateArtifacts")

target(createLdapSchema: "Creates a new LdapSchema script") {
    depends(checkVersion, parseArguments)

    promptForName(type: "LdapSchema")

    def name = argsMap["params"][0]
    createArtifact(name: name,
        suffix: "LdapSchema",
        type: "LdapSchema",
        path: "griffon-app/ldap")
}
setDefaultTarget('createLdapSchema')
