eventCollectArtifacts = { artifactsInfo ->
    if(!artifactsInfo.find{ it.type == "domain" }) {
        artifactsInfo << [type: "domain", path: "domain", suffix: ""]
    }
}

def eventClosure1 = binding.variables.containsKey('eventCopyLibsEnd') ? eventCopyLibsEnd : {jardir->}
eventCopyLibsEnd = { jardir ->
    eventClosure1(jardir)
    if (!isPluginProject) {
        ant.fileset(dir:"${getPluginDirForName('gorm').file}/lib/", includes:"*.jar").each {
            griffonCopyDist(it.toString(), jardir)
        }
    }
}

