import org.apache.chemistry.opencmis.commons.SessionParameter
import org.apache.chemistry.opencmis.commons.enums.BindingType

// default
session = [
    (SessionParameter.ATOMPUB_URL): 'http://opencmis.cloudapp.net/inmemory/atom/',
    (SessionParameter.BINDING_TYPE): BindingType.ATOMPUB.value(),
    (SessionParameter.REPOSITORY_ID) : 'A1'
]

sessions {
    /*
    someName = [
        (SessionParameter.ATOMPUB_URL): 'http://opencmis.cloudapp.net/inmemory/atom/',
        (SessionParameter.BINDING_TYPE): BindingType.ATOMPUB.value(),
        (SessionParameter.REPOSITORY_ID) : 'A1'
    ]
    */
}

/*
// change me as needed
environments {
    development {
        session = [
            param: value
        ]
    }
    test {
    }
    production {
    }
}
*/
