couchdb {
    host = "localhost"
    port = 5984
    username = ""
    password = ""
}
environments {
    development {
        couchdb {
            database = "devdb"
        }
    }
    test {
        couchdb {
            database = "testdb"
        }
    }
    production {
        couchdb {
            database = "proddb"
        }
    }
}
