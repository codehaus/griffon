client {
    credentials {
        accessKey = 'change me'
        secretKey = 'change me'
    }
    config {
        connectionTimeout = 5000i
    }
}
environments {
    development {
        client {
        }
    }
    test {
        client {
        }
    }
    production {
        client {
            // credentialsProvider = full.qualified.class.name
        }
    }
}
