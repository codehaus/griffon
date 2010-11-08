database {
   client = false
   config { }
}
environments {
    development {
        database {
            alias = 'neodatis/dev.odb'
        }
    }
    test {
        database {
            alias = 'neodatis/test.odb'
        }
    }
    production {
        database {
            alias = 'neodatis/prod.odb'
        }
    }
}
