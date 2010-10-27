/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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
package griffon.couchdb

import org.jcouchdb.document.DesignDocument
import org.jcouchdb.util.AbstractCouchDBUpdater
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

/**
 * @author Andres Almiray
 */
class ClasspathCouchDBUpdater extends AbstractCouchDBUpdater {
    String parentPath = 'couchdb/views/'

    protected List<DesignDocument> readDesignDocuments() throws IOException {
        Map<String,DesignDocument> designDocuments = new HashMap<String, DesignDocument>()

        new PathMatchingResourcePatternResolver(getClass().classLoader).getResources(parentPath + '**/*.js').each { view ->
            URL viewURL = view.getURL()
            String path = viewURL.toString()
            path = path.substring(path.indexOf(parentPath) + parentPath.length())

            boolean isMapFunction = path.endsWith(MAP_SUFFIX)
            boolean isReduceFunction = path.endsWith(REDUCE_SUFFIX)
            if(isMapFunction || isReduceFunction)
            {
                String content = viewURL.text
                if(content) createViewFor(path, content, designDocuments, File.separator)
            }
        }

        return new ArrayList<DesignDocument>(designDocuments.values())
    }
}
