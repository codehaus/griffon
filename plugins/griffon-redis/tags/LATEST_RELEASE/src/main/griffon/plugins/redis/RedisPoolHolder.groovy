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
package griffon.plugins.redis

import redis.clients.jedis.*

import griffon.core.GriffonApplication
import griffon.util.ApplicationHolder
import griffon.util.CallableWithArgs
import static griffon.util.GriffonNameUtils.isBlank

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Andres Almiray
 */
@Singleton
class RedisPoolHolder {
    private static final Logger LOG = LoggerFactory.getLogger(RedisPoolHolder)
    private static final Object[] LOCK = new Object[0]
    private final Map<String, JedisPool> pools = [:]
  
    String[] getJedisPoolNames() {
        List<String> poolNames = new ArrayList().addAll(pools.keySet())
        poolNames.toArray(new String[poolNames.size()])
    }

    JedisPool getJedisPool(String datasourceName = 'default') {
        if(isBlank(datasourceName)) datasourceName = 'default'
        retrieveJedisPool(datasourceName)
    }

    void setJedisPool(String datasourceName = 'default', JedisPool pool) {
        if(isBlank(datasourceName)) datasourceName = 'default'
        storeJedisPool(datasourceName, pool)       
    }

    Object withRedis(String datasourceName = 'default', Closure closure) {
        JedisPool pool = fetchJedisPool(datasourceName)
        if(LOG.debugEnabled) LOG.debug("Executing statement on datasource '$datasourceName'")
        Jedis jedis = pool.resource
        try {
            return closure(datasourceName, jedis)
        } finally {
            pool.returnResource(jedis)
        }
    }

    public <T> T withRedis(String datasourceName = 'default', CallableWithArgs<T> callable) {
        JedisPool pool = fetchJedisPool(datasourceName)
        if(LOG.debugEnabled) LOG.debug("Executing statement on datasource '$datasourceName'")
        Jedis jedis = pool.resource
        try {
            callable.args = [datasourceName, jedis] as Object[]
            return callable.call()
        } finally {
            pool.returnResource(jedis)
        }        
    }
    
    boolean isJedisPoolConnected(String datasourceName) {
        if(isBlank(datasourceName)) datasourceName = 'default'
        retrieveJedisPool(datasourceName) != null
    }
    
    void disconnectJedisPool(String datasourceName) {
        if(isBlank(datasourceName)) datasourceName = 'default'
        retrieveJedisPool(datasourceName)?.destroy()
        storeJedisPool(datasourceName, null)        
    }

    private JedisPool fetchJedisPool(String datasourceName) {
        if(isBlank(datasourceName)) datasourceName = 'default'
        JedisPool pool = retrieveJedisPool(datasourceName)
        if(pool == null) {
            GriffonApplication app = ApplicationHolder.application
            ConfigObject config = RedisConnector.instance.createConfig(app)
            pool = RedisConnector.instance.connect(app, config, datasourceName)
        }
        
        if(pool == null) {
            throw new IllegalArgumentException("No such JedisPool configuration for name $datasourceName")
        }
        pool
    }

    private JedisPool retrieveJedisPool(String datasourceName) {
        synchronized(LOCK) {
            pools[datasourceName]
        }
    }

    private void storeJedisPool(String datasourceName, JedisPool pool) {
        synchronized(LOCK) {
            pools[datasourceName] = pool
        }
    }
}
