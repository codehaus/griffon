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
            home = '@griffon.project.key@-dev'
        }
    }
    test {
        environment {
            home = '@griffon.project.key@-test'
        }
    }
    production {
        environment {
            home = '@griffon.project.key@-prod'
        }
    }
}
