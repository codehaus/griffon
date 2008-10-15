eventCleanEnd = {
    Ant.delete(dir: "${projectWorkDir}/fest-classes", failonerror: false)
    Ant.delete(dir: "${basedir}/test/fest-reports", failonerror: false)
}
