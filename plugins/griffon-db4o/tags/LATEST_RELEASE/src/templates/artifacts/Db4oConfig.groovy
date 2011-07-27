import com.db4o.config.EmbeddedConfiguration

dataSource {
    delete = true
}
environments {
    development {
        dataSource {
            name = "devDb.yarv"
        }
    }
    test {
        dataSource {
            name = "testDb.yarv"
        }
    }
    production {
        dataSource {
            name = "prodDb.yarv"
            delete = false
        }
    }
}

def configure(EmbeddedConfiguration configuration) {
    // empty
}
