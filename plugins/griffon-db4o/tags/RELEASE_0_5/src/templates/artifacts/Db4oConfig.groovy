import com.db4o.config.EmbeddedConfiguration

dataSource {
    delete = true
}
environments {
    development {
        dataSource {
            name = "@griffon.project.key@-dev.yarv"
        }
    }
    test {
        dataSource {
            name = "@griffon.project.key@-test.yarv"
        }
    }
    production {
        dataSource {
            name = "@griffon.project.key@-prod.yarv"
            delete = false
        }
    }
}

def configure(EmbeddedConfiguration configuration) {
    // empty
}
