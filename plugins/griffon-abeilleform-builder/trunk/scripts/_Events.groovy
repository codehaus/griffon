eventCopyLibsEnd = { jardir ->
	//println  "COPY FROM: ${getPluginDirForName('abeilleform-builder').file}/lib/"
    //ant.fileset(dir:"${getPluginDirForName('abeilleform-builder').file}/lib/", includes:"*.jar").each {
	ant.fileset(dir:"${getPluginDirForName('abeilleform-builder').file}/lib/", includes:"*.jar").each {
	    println "griffonCopyDist(${it.toString()}, ${jardir})"
        griffonCopyDist(it.toString(), jardir)
    }	
}