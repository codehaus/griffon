dataSource {
    driverClassName = 'org.apache.cassandra.cql.jdbc.CassandraDriver'
    tokenizeddl = false // set this to true if using MySQL or any other
                        // RDBMS that requires execution of DDL statements
                        // on separate calls
    pool {
        maxWait = 60000
        maxIdle = 5
        maxActive = 8
    }
}
environments {
    development {
        dataSource {
            dbCreate = 'create' // one of ['create', 'skip']
            url = 'jdbc:cassandra://localhost:9160/@griffon.project.key@_dev'
        }
    }
    test {
        dataSource {
            dbCreate = 'create'
            url = 'jdbc:cassandra://localhost:9160/@griffon.project.key@_test'
        }
    }
    production {
        dataSource {
            dbCreate = 'skip'
            url = 'jdbc:cassandra://localhost:9160/@griffon.project.key@_prod'
        }
    }
}
