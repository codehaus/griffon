import org.codehaus.griffon.cli.GriffonScriptRunner as GSR
import org.codehaus.griffon.plugins.GriffonPluginUtils

includeTargets << griffonScript("_GriffonInit")

eventCopyLibsEnd = { jardir ->
    def gfxlibs = "${getPluginDirForName('gfx').file}/lib"
    ant.fileset(dir: gfxlibs, includes: "*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
    ["swingx","svg"].each { ext ->
        if(config?.griffon?.gfx[(ext)]?.enabled ?: false) {
            ant.fileset(dir: "${gfxlibs}/$ext", includes: "*.jar").each {
                griffonCopyDist(it.toString(), jardir)
            }
        }
    }
}