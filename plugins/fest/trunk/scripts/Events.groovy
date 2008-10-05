eventCleanEnd = {
    Ant.delete(dir: "${projectWorkDir}/fest-classes", failonerror: false)
    Ant.delete(dir: "${basedir}/build/fest-reports", failonerror: false)
}
