dataSource {
}
environments {
    development {
        dataSource {
            name = "devDb.db4o"
        }
    }
    test {
        dataSource {
            name = "testDb.db4o"
        }
    }
    production {
        dataSource {
            name = "prodDb.db4o"
        }
    }
}
