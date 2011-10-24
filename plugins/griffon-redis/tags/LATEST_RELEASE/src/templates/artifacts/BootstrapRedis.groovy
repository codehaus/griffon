import redis.clients.jedis.Jedis

class BootstrapRedis {
    def init = { String datasourceName, Jedis jedis -> 
    }

    def destroy = { String datasourceName, Jedis jedis ->
    }
} 
