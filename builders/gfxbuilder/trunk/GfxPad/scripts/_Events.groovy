eventPrepareIzpackInstallerEnd = { ->
    Ant.copy( todir: "${basedir}/installer/izpack/resources", overwrite: true ) { 
        fileset( dir: "${basedir}/src/installer/izpack/resources", includes: "**" ) 
    }
}
