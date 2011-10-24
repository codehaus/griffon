database {
    params = [:]
}
environments {
    development {
        database {
            storeDir = "neo4j/@griffon.project.key@-dev"
        }
    }
    test {
        database {
            storeDir = "neo4j/@griffon.project.key@-test"
        }
    }
    production {
        database {
            storeDir = "neo4j/@griffon.project.key@-prod"
        }
    }
}
