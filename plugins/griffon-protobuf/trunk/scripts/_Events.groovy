includeTargets << griffonScript("Init")
includePluginScript("protobuf", "Protoc")

eventCompileStart = { type ->
    if(compilingProtobufPlugin()) return
    if(type != "source") return
    protoc()
}

private boolean compilingProtobufPlugin() { getPluginDirForName("protobuf") == null }
