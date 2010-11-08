environment {
    transactional = true
    allowCreate = true
}
entityStores {
    first {
        transactional = true
        allowCreate = true
    }
}
environments {
    development {
        environment {
            home = 'dev'
        }
    }
    test {
        environment {
            home = 'test'
        }
    }
    production {
        environment {
            home = 'prod'
        }
    }
}
