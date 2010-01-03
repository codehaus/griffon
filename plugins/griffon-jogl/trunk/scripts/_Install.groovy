/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Andres Almiray
 */

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

// check to see if we already have a JoglGriffonAddon
ConfigSlurper configSlurper1 = new ConfigSlurper()
def slurpedBuilder1 = configSlurper1.parse(new File("$basedir/griffon-app/conf/Builder.groovy").toURL())
boolean addonIsSet1
slurpedBuilder1.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'JoglGriffonAddon' == builder
    }
}

if (!addonIsSet1) {
    println 'Adding JoglGriffonAddon to Builders.groovy'
    new File("$basedir/griffon-app/conf/Builder.groovy").append('''
root.'JoglGriffonAddon'.addon=true
''')
}

def joglJnlp = "http://download.java.net/media/jogl/builds/archive/jsr-231-1.x-webstart-next/jogl.jnlp"
def buildconf = configSlurper1.parse(new File("$basedir/griffon-app/conf/Config.groovy").toURL())
if(!(joglJnlp in buildconf.flatten().'griffon.extensions.jnlpUrls')) {
    println "Adding JOGL jnlp extension to configuration"
    new File("$basedir/griffon-app/conf/Config.groovy").append("""
griffon.extensions.jnlpUrls << "$joglJnlp"
""")
}
