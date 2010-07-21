//
// This script is executed by Griffon after plugin was installed to project.
// This script is a Gant script so you can use all special variables provided
// by Gant (such as 'baseDir' which points on project base dir). You can
// use 'ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
//
//    ant.mkdir(dir:"${basedir}/griffon-app/jobs")
//

import org.apache.commons.lang.StringUtils


// default driver
String realDriver = 'org.hsqldb.jdbcDriver'

//     add p6spy driver as a commented line to DataSource.groovy
File p6file = new File("${basedir}/griffon-app/conf/spy.properties")
File dsfile = new File("${basedir}/griffon-app/conf/DataSource.groovy")
if(dsfile.getText().indexOf('p6spy')==-1 && !p6file.exists()) {
    println "Adding p6spy driver to DataSource.groovy"
    StringBuffer sb = new StringBuffer()
    dsfile.eachLine { line ->
        sb << line << '\n'
        if(line.indexOf('driverClassName')>-1) {
            // record the real driver so we can put it in spy.properties
            realDriver = StringUtils.substringBetween(line,'"')
            // add the p6spy driver so it can easily be enabled
            sb << '//    driverClassName = "com.p6spy.engine.spy.P6SpyDriver" // use this driver to enable p6spy logging\n'
        }
    }
    dsfile.write(sb.toString())

    println "Configuring spy.properties with realdriver=${realDriver}"
    // set real driver in spy.properties
    File spyfile = new File("${pluginBasedir}/griffon-app/conf/spy.template")
    sb = new StringBuffer()
    spyfile.eachLine { line ->
        if(line.startsWith('realdriver=')) {
            sb << "realdriver=${realDriver}\n"
        } else {
            sb << line << '\n'
        }
    }
    p6file.write(sb.toString())
} else {
    println "It appears configuration for p6spy already exists. Not modifying DataSource.groovy or spy.properties"
}

