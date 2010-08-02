package org.codehaus.griffon.runtime.domain

import griffon.persistence.Entity

@Entity('sample')
class Author {
    String name

    static belongsTo = Book
}
