package org.codehaus.griffon.runtime.domain

import griffon.transform.Domain

@Domain('sample')
class Author {
    String name

    static belongsTo = Book
}
