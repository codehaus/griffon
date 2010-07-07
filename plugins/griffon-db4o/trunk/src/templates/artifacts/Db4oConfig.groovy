import com.db4o.config.EmbeddedConfiguration

dataSource {
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
        }
    }
}

def configure(EmbeddedConfiguration configuration) {
    // empty
}
