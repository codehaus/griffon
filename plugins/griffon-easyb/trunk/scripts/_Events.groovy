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

eventCleanEnd = {
    ant.delete(dir: "${projectWorkDir}/easyb-classes", failonerror: false)
    ant.delete(dir: "${basedir}/test/easyb-reports", failonerror: false)
}

eventJarFilesStart = {
   // make sure EasybGriffonPlugin.class is not added to app jar
   ant.delete(file: "${projectWorkDir}/classes/EasybGriffonPlugin.class", failonerror: false)
}

eventCopyLibsEnd = { jardir ->
   ant.delete(dir:jardir, includes: "**/easyb.*")
}
