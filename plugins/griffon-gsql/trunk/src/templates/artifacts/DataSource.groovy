dataSource {
    pooled = false
    driverClassName = "org.hsqldb.jdbcDriver"
    username = "sa"
    password = ""
}
pool {
  maxWait = 60000
  maxIdle = 5
  maxActive = 8
}
environments {
    development {
        dataSource {
            dbCreate = "create" // one of 'create', 'create-drop'
            url = "jdbc:hsqldb:mem:devDB"
        }
    }
    test {
        dataSource {
            dbCreate = "create"
            url = "jdbc:hsqldb:mem:testDb"
        }
    }
    production {
        dataSource {
            dbCreate = "create-drop"
            url = "jdbc:hsqldb:file:prodDb;shutdown=true"
        }
    }
}
