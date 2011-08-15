/*
 * Copyright 2011 the original author or authors.
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

package griffon.plugins.mybatis

import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory

import griffon.core.GriffonApplication
import griffon.util.ApplicationHolder
import static griffon.util.GriffonNameUtils.isBlank

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * @author Andres Almiray
 */
@Singleton
class SqlSessionFactoryHolder {
    private static final Log LOG = LogFactory.getLog(SqlSessionFactoryHolder)
    private static final Object[] LOCK = new Object[0]
    private final Map<String, SqlSessionFactory> sessionFactories = [:]

    String[] getSessionFactoryNames() {
        List<String> sessionFactoryNames = new ArrayList().addAll(sessionFactories.keySet())
        sessionFactoryNames.toArray(new String[sessionFactoryNames.size()])
    }

    SqlSessionFactory getSqlSessionFactory(String sessionFactoryName = 'default') {
        if(isBlank(sessionFactoryName)) sessionFactoryName = 'default'
        retrieveSqlSessionFactory(sessionFactoryName)
    }

    void setSqlSessionFactory(String sessionFactoryName = 'default', SqlSessionFactory sf) {
        if(isBlank(sessionFactoryName)) sessionFactoryName = 'default'
        storeSqlSessionFactory(sessionFactoryName, sf)       
    }

    void withSqlSession(String sessionFactoryName = 'default', Closure closure) {
        SqlSessionFactory sf = fetchSqlSessionFactory(sessionFactoryName)
        if(LOG.debugEnabled) LOG.debug("Executing SQL stament on sqlSession '$sessionFactoryName'")
        SqlSession session = sf.openSession(true)
        try {
            closure(sessionFactoryName, session)
        } finally {
            session.close()
        }
    }
    
    boolean isSqlSessionFactoryAvailable(String sessionFactoryName) {
        if(isBlank(sessionFactoryName)) sessionFactoryName = 'default'
        retrieveSqlSessionFactory(sessionFactoryName) != null
    }
    
    void disconnectSqlSessionFactory(String sessionFactoryName) {
        if(isBlank(sessionFactoryName)) sessionFactoryName = 'default'
        storeSqlSessionFactory(sessionFactoryName, null)        
    }

    private SqlSessionFactory fetchSqlSessionFactory(String sessionFactoryName) {
        if(isBlank(sessionFactoryName)) sessionFactoryName = 'default'
        SqlSessionFactory sf = retrieveSqlSessionFactory(sessionFactoryName)
        if(sf == null) {
            GriffonApplication app = ApplicationHolder.application
            ConfigObject config = SqlSessionConnector.instance.createConfig(app)
            sf = SqlSessionConnector.instance.connect(app, config, sessionFactoryName)
        }
        
        if(sf == null) {
            throw new IllegalArgumentException("No such SqlSessionFactory configuration for name $sessionFactoryName")
        }
        sf
    }

    private SqlSessionFactory retrieveSqlSessionFactory(String sessionFactoryName) {
        synchronized(LOCK) {
            sessionFactories[sessionFactoryName]
        }
    }

    private void storeSqlSessionFactory(String sessionFactoryName, SqlSessionFactory sf) {
        synchronized(LOCK) {
            sessionFactories[sessionFactoryName] = sf
        }
    }
}
