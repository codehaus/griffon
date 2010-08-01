package org.codehaus.griffon.runtime.domain

import griffon.test.*
import griffon.core.GriffonApplication
import griffon.test.mock.MockGriffonApplication

class EntityInjectionTests extends GriffonUnitTestCase {
    private GriffonApplication app

    void setUp() {
        app = new MockGriffonApplication()
        app.builderClass = SampleBuilderConfig
        SampleDomainHandlerHolder.domainHandler = new SampleDomainHandler(app)
        app.initialize()
    }

    void testInjectedProperties() {
        Book book = new Book(title: 'Griffon in Action', id: 1)
        assert book.griffonDomainMapping == 'sample'
        assert book.title == 'Griffon in Action'
        assert book.id == 1
        assert book.toString() == 'org.codehaus.griffon.runtime.domain.Book : 1'
    }

    void testDynamicMethodInjection() {
        assert 1 == Book.fetch(1)
        assert [1] == Book.findAll()
        assert [[a:1, b: 2]] == Book.findAll(a: 1, b: 2)
    }
}
