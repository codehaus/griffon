package org.codehaus.griffon.runtime.domain

import griffon.transform.Domain

@Domain('sample')
class Book {
    String title

    static hasMany = [author: Author]
}
