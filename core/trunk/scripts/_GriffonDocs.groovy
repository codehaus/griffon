/*
* Copyright 2004-2005 the original author or authors.
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
 * @author Graeme Rocher
 * @since 1.0
 *
 * Created: Sep 20, 2007
 */

includeTargets << griffonScript("_GriffonCompile")

javadocDir = "${basedir}/docs/api"
groovydocDir = "${basedir}/docs/gapi"
docEncoding = "UTF-8"
docSourceLevel = "1.5"
links = [
            'http://java.sun.com/j2se/1.5.0/docs/api/'
        ]

target(docs: "Produces documentation for a Griffon project") {
    depends(compile, javadoc, groovydoc)
}

target(setupDoc:"Sets up the doc directories") {
    ant.mkdir(dir:"${basedir}/docs")
    ant.mkdir(dir:groovydocDir)
    ant.mkdir(dir:javadocDir)
}

target(groovydoc:"Produces groovydoc documentation") {
    ant.taskdef(name:"groovydoc", classname:"org.codehaus.groovy.ant.Groovydoc")
    event("DocStart", ['groovydoc'])
    ant.groovydoc(destdir:groovydocDir, sourcepath:".", use:"true", windowtitle:griffonAppName,'private':"true")
    event("DocEnd", ['groovydoc'])
}

target(javadoc:"Produces javadoc documentation") {
   depends(setupDoc)
    event("DocStart", ['javadoc'])
   if(new File("${basedir}/src/java").listFiles().find{ !it.name.startsWith(".")}) {
	   ant.javadoc( access:"protected",
	                destdir:javadocDir,
	                encoding:docEncoding,
	                classpathref:"griffon.compile.classpath",
	                use:"yes",
	                windowtitle:griffonAppName,
	                docencoding:docEncoding,
	                charset:docEncoding,
	                source:docSourceLevel,
	                useexternalfile:"yes",
	                breakiterator:"true",
	                linksource:"yes",
	                maxmemory:"128m") {
	       fileset(dir:"${basedir}/src/java")
	       for(i in links) {
	           link(href:i)
	       }
	   }
   }
    event("DocEnd", ['javadoc'])

}
