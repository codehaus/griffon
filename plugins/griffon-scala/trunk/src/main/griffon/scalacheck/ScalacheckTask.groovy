/*
 * Copyright 2009 the original author or authors.
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

package griffon.scalacheck

import org.apache.tools.ant.Task
import org.apache.tools.ant.types.Path
import org.apache.tools.ant.types.Reference
import org.apache.tools.ant.taskdefs.Java

/**
 * @author Andres.Almiray
 */
public class ScalacheckTask extends Task {
    boolean fork
	String maxMemory
	String destdir
    Path classpath
	private List specifications = []

    public Path createClasspath() {
        if(!classpath) {
            classpath = new Path(getProject())
        }
        classpath.createPath()
    }

    public void setClasspathRef(final Reference ref) {
        createClasspath().setRefid(ref)
    }

	public void addConfiguredSpecification(Specification spec) {
		specifications << spec.name
	}

	public void execute() {
		def java = new Java()
		java.bindToOwner(this)
		java.init()
		java.fork = fork
		java.setClassname("griffon.scalacheck.ScalacheckRunner")
		Path path = java.createClasspath()
		path.setPath(classpath.toString())

		if(maxMemory) java.createJvmarg.setValue("-Xmx" + maxMemory)
		java.createArg().value = "--o=$destdir"
		specifications.each{ java.createArg().value = it }

		java.executeJava()
	}
}

/**
 * @author Andres.Almiray
 */
class Specification {
	String name
}
