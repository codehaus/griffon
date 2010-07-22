griffon {}

griffon.jars.destDir='dist/addon'

/*
 * Copyright 2010 the original author or authors.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

//griffon.jars.jarName='ValidationGriffonAddon.jar'

// The following properties have been added by the Upgrade process...
griffon.jars.pack=false // jars were not automatically packed in Griffon 0.0
griffon.jars.sign=true // jars were automatically signed in Griffon 0.0
griffon.extensions.jarUrls = [] // remote jars were not possible in Griffon 0.1
griffon.extensions.jnlpUrls = [] // remote jars were not possible in Griffon 0.1
// may safely be removed, but calling upgrade will restore it
def env = griffon.util.Environment.current.name
signingkey.params.sigfile='GRIFFON' + env
signingkey.params.keystore = "${basedir}/griffon-app/conf/keys/${env}Keystore"
signingkey.params.alias = env
// signingkey.params.storepass = 'BadStorePassword'
// signingkey.params.keyPass = 'BadKeyPassword'
signingkey.params.lazy = true // only sign when unsigned
// you may now tweak memory parameters
//griffon.memory.min='16m'
//griffon.memory.max='64m'
//griffon.memory.maxPermSize='64m'
griffon.project.dependency.resolution = {
    // inherit Griffon' default dependencies
    inherits("global") {
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        mavenLocal()
        mavenCentral()
        mavenRepo "http://snapshots.repository.codehaus.org"
        mavenRepo "http://repository.codehaus.org"
        mavenRepo "http://download.java.net/maven/2/"
        mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        compile 'common-lang:commons-lang:2.5'
        compile 'commons-validator:commons-validator:1.3.1'
    }
}

griffon {
    doc {
        logo = '<a href="http://griffon.codehaus.org" target="_blank"><img alt="The Griffon Framework" src="../img/griffon.png" border="0"/></a>'
        sponsorLogo = "<br/>"
        footer = "<br/><br/>Made with Griffon (0.9)"
    }
}
