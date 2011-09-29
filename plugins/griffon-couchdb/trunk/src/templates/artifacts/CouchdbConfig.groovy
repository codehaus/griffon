database {
    host = "localhost"
    port = 5984
    username = ""
    password = ""
}
environments {
    development {
        database {
            datastore = "@griffon.project.key@-dev"
        }
    }
    test {
        database {
            datastore = "@griffon.project.key@-test"
        }
    }
    production {
        database {
            datastore = "@griffon.project.key@-prod"
        }
    }
}
