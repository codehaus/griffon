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
import griffon.util.Environment
import griffon.util.Metadata
import griffon.util.CallableWithArgs

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * @author Andres Almiray
 */
@Singleton
final class RedisConnector {
    private bootstrap

    private static final Log LOG = LogFactory.getLog(RedisConnector)

    static void enhance(MetaClass mc) {
        mc.withRedis = {Closure closure ->
            RedisPoolHolder.instance.withRedis('default', closure)
        }
        mc.withRedis << {String datasourceName, Closure closure ->
            RedisPoolHolder.instance.withRedis(datasourceName, closure)
        }
        mc.withRedis << {CallableWithArgs callable ->
            RedisPoolHolder.instance.withRedis('default', callable)
        }
        mc.withRedis << {String datasourceName, CallableWithArgs callable ->
            RedisPoolHolder.instance.withRedis(datasourceName, callable)
        }
    }

    Object withRedis(String datasourceName = 'default', Closure closure) {
        RedisPoolHolder.instance.withRedis(datasourceName, closure)
    }

    public <T> T withRedis(String datasourceName = 'default', CallableWithArgs<T> callable) {
        return RedisPoolHolder.instance.withRedis(datasourceName, callable)
    }

    // ======================================================

    ConfigObject createConfig(GriffonApplication app) {
        def datasourceClass = app.class.classLoader.loadClass('RedisConfig')
        new ConfigSlurper(Environment.current.name).parse(datasourceClass)
    }

    private ConfigObject narrowConfig(ConfigObject config, String datasourceName) {
        return datasourceName == 'default' ? config.datasource : config.datasources[datasourceName]
    }

    JedisPool connect(GriffonApplication app, ConfigObject config, String datasourceName = 'default') {
        if (RedisPoolHolder.instance.isJedisPoolConnected(datasourceName)) {
            return RedisPoolHolder.instance.getJedisPool(datasourceName)
        }

        config = narrowConfig(config, datasourceName)
        app.event('RedisConnectStart', [config, datasourceName])
        JedisPool pool = startRedis(config)
        RedisPoolHolder.instance.setJedisPool(datasourceName, pool)
        bootstrap = app.class.classLoader.loadClass('BootstrapRedis').newInstance()
        bootstrap.metaClass.app = app
        withRedis(datasourceName) { dsName, jedis -> bootstrap.init(dsName, jedis) }
        app.event('RedisConnectEnd', [datasourceName, pool])
        pool
    }

    void disconnect(GriffonApplication app, ConfigObject config, String datasourceName = 'default') {
        if (RedisPoolHolder.instance.isJedisPoolConnected(datasourceName)) {
            config = narrowConfig(config, datasourceName)
            JedisPool pool = RedisPoolHolder.instance.getJedisPool(datasourceName)
            app.event('RedisDisconnectStart', [config, datasourceName, pool])
            withRedis(datasourceName) { dsName, jedis -> bootstrap.destroy(dsName, jedis) }
            stopRedis(config, pool)
            app.event('RedisDisconnectEnd', [config, datasourceName])
            RedisPoolHolder.instance.disconnectJedisPool(datasourceName)
        }
    }

    private JedisPool startRedis(ConfigObject config) {
        JedisPoolConfig poolConfig = new JedisPoolConfig()
        config.pool.each { key, value -> poolConfig[key] = value }
        new JedisPool(poolConfig,
            (config.host ?: 'localhost') as String)
            /*
            (config.port ?: 6379i) as int,
            (config.timeout ?: 2000i) as int,
            (config.password ?: null) as String,
            (config.database ?: 0i) as int)
            */
    }

    private void stopRedis(ConfigObject config, JedisPool pool) {
        // empty ??
    }
}
