dataSource {
    pooled = false
    driverClassName = 'org.h2.Driver'
    username = 'sa'
    password = ''
    tokenizeddl = false // set this to true if using MySQL or any other
                        // RDBMS that requires execution of DDL statements
                        // on separate calls
}
pool {
    maxWait = 60000
    maxIdle = 5
    maxActive = 8
}

berkeleydb {
    environmentHomeFile = new File('.', 'carbonado-@griffon.project.key@')
    transactionWriteNoSync = true
}

map {
    name = '@griffon.project.key@'
}

environments {
    development {
        dataSource {
            name = '@griffon.project.key@-dev'
            dbCreate = "create" // one of ['create', 'skip']
            url = "jdbc:h2:mem:@griffon.project.key@-dev"
        }
        berkeleydb {
            name = '@griffon.project.key@-dev'
        }
    }
    test {
        dataSource {
            name = '@griffon.project.key@-test'
            dbCreate = "create"
            url = "jdbc:h2:mem:@griffon.project.key@-test"
        }
        berkeleydb {
            name = '@griffon.project.key@-test'
        }
    }
    production {
        dataSource {
            name = '@griffon.project.key@-prod'
            dbCreate = "skip"
            url = "jdbc:h2:file:@griffon.project.key@-prod;shutdown=true"
        }
        berkeleydb {
            name = '@griffon.project.key@-prod'
        }
    }
}
