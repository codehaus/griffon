import groovy.sql.Sql

class BootstrapCassandra {
    def init = { String dataSourceName = 'default', Sql sql ->
    }

    def destroy = { String dataSourceName = 'default', Sql sql ->
    }
} 
