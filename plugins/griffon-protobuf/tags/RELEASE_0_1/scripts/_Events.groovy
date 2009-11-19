includeTargets << griffonScript("Init")
includePluginScript("protobuf", "Protoc")

eventCompileStart = { type ->
    if(compilingProtobufPlugin()) return
    if(type != "source") return
    protoc()
}

eventCleanEnd = {
    ant.delete(dir: "${projectWorkDir}/gensrc", quiet: false)
}

eventCopyLibsEnd = { jardir ->
    ant.fileset(dir: "${getPluginDirForName('protobuf').file}/lib", includes: "*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}

eventStatsStart = { pathToInfo ->
    if(!pathToInfo.find{it.path == "src.protobuf"} ) {
        pathToInfo << [name: "Protobuf Sources", path: "src.protobuf", filetype: [".proto"]]
    }
}

private boolean compilingProtobufPlugin() { getPluginDirForName("protobuf") == null }
