package org.codehaus.griffon.runtime.domain

import griffon.persistence.Entity

@Entity('sample')
class Publisher extends Bean {
    String name

    void onLoad() { println 'load' }
}
