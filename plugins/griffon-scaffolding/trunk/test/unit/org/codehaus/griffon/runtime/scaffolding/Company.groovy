package org.codehaus.griffon.runtime.scaffolding

import griffon.plugins.scaffolding.Scaffold

@Scaffold(excludes=['id'])
class Company {
    int id
    String name
}
