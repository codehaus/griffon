package org.codehaus.griffon.runtime.domain

import griffon.transform.Domain

@Domain('sample')
class Publisher extends Bean {
    String name

    void onLoad() { println 'load' }
}
