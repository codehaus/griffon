raw {
    timeout = 2000
    maxConnections = 50
}
environments {
    development {
        raw {
            url = 'http://localhost:8098/rawdev'
        }
    }
    test {
        raw {
            url = 'http://localhost:8098/rawtest'
        }
    }
    production {
        raw {
            url = 'http://localhost:8098/rawprod'
        }
    }
}
