datasource {
    password = null
    timeout = 2000i
    port = 6379i
    database = 0i
    pool {
        testWhileIdle = true
        minEvictableIdleTimeMillis = 60000
        timeBetweenEvictionRunsMillis = 30000
        numTestsPerEvictionRun = -1        
    }
}
environments {
    development {
        datasource {
            host = 'localhost'
        }
    }
    test {
        datasource {
            host = 'localhost'
        }
    }
    production {
        datasource {
            host = 'localhost'
        }
    }
}
