database {
   client = false
   config { }
}
environments {
    development {
        database {
            alias = 'neodatis/@griffon.project.key@-dev.odb'
        }
    }
    test {
        database {
            alias = 'neodatis/@griffon.project.key@-test.odb'
        }
    }
    production {
        database {
            alias = 'neodatis/@griffon.project.key@-prod.odb'
        }
    }
}
