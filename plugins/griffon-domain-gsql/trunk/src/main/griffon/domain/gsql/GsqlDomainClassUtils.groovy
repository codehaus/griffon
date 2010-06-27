/*
 * Copyright 2010 the original author or authors.
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

package griffon.domain.gsql

import griffon.core.GriffonApplication
import griffon.core.ArtifactInfo
import griffon.domain.DomainClassUtils
import griffon.domain.orm.*

import java.beans.Introspector

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

import griffon.domain.gsql.metaclass.*

/**
 * @author Andres Almiray
 */
@Singleton
final class GsqlDomainClassUtils {
    private static final Log LOG = LogFactory.getLog(GsqlDomainClassUtils)

    private final Map DOMAIN_LOOKUP = [:]
    private final Map QUERIES = [:]

    void init(GriffonApplication app) {
        fetchAndSetQueries()
        app.artifactManager.domainArtifacts.each { domain ->
            DOMAIN_LOOKUP[domain.klass.name] = domain
        }
    }
    
    def fetchQuery(String queryName, Class klass) {
        ArtifactInfo artifactInfo = DOMAIN_LOOKUP[klass.name]
        def query = QUERIES[queryName +'_'+ artifactInfo.simpleName]  
        if(!query) query = QUERIES[queryName]  
        return query(artifactInfo, DomainClassUtils.instance.getEntityNameFor(klass))
    }

    def makeAndPopulateInstance(Class klass, row) {
        def props = DOMAIN_LOOKUP[klass.name].declaredProperties
        Map values = [:]
        props.each { p -> values[p] = row[p] }
        return klass.make(values)
    }

    def toSql(Criterion criterion) {
        List criteria = []
        Map values = [:]
        buildSql(criterion, criteria, values)
        return [criteria.join(' '), values]
    }

    // ----------------------------------------------

    private void fetchAndSetQueries() {
        QUERIES.putAll(fetchQueryProperties(new DefaultGsqlQueries()))
        try {
            def gsqlQueriesClass = this.class.classLoader.loadClass('GsqlQueries')
            QUERIES.putAll(fetchQueryProperties(gsqlQueriesClass.newInstance()))
        } catch(x) {
            LOG.info('No custom GSQL queries found.') 
        }
    }

    private Map fetchQueryProperties(Object source) {
        return (Introspector.getBeanInfo(source.class).propertyDescriptors.name - ['class', 'metaClass']).inject([:]) { queries, prop ->
           queries[prop] = source[prop]
           return queries
        }
    }

    private void buildSql(UnaryExpression criterion, List criteria, Map values) {
        criteria << criterion.propertyName +' '+ criterion.operator
    }

    private void buildSql(PropertyExpression criterion, List criteria, Map values) {
        criteria << criterion.propertyName +' '+
                    criterion.operator +' '+
                    criterion.otherPropertyName
    }

    private void buildSql(BinaryExpression criterion, List criteria, Map values) {
        criteria << criterion.propertyName +' '+ criterion.operator +' ?'
        values[criterion.propertyName] = criterion.value
    }

    private void buildSql(CompositeCriterion criterion, List criteria, Map values) {
        boolean first = criteria.empty
        def length = criterion.criteria.length
        if(!first) criteria << '('
        criterion.criteria.eachWithIndex { c, i ->
            buildSql(c, criteria, values)
            if(i < length - 1) criteria << criterion.operator
        }
        if(!first) criteria << ')'
    }
}
