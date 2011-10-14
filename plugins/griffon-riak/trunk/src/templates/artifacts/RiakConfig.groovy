client {
    timeout = 2000
    maxConnections = 50
}
environments {
    development {
        client {
            url = 'http://localhost:8098/@griffon.project.key@-dev'
        }
    }
    test {
        client {
            url = 'http://localhost:8098/@griffon.project.key@-test'
        }
    }
    production {
        client {
            url = 'http://localhost:8098/@griffon.project.key@-prod'
        }
    }
}
