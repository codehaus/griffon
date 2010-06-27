package org.codehaus.griffon.persistence

import griffon.core.GriffonApplication
import griffon.test.mock.MockGriffonApplication
import sample.*

class EntityInjectionTests extends GroovyTestCase {
    private static GriffonApplication app

    static {
        app = new MockGriffonApplication()
        SampleDomainHandlerHolder.domainHandler = new SampleDomainHandler(app)
    }

    void testInjectedProperties() {
        Book book = new Book(title: 'Griffon in Action', id: 1)
        assert book.griffonDomainMapping == 'sample'
        assert book.title == 'Griffon in Action'
        assert book.id == 1
        assert book.toString() == 'sample.Book : 1'
    }

    void testDynamicMethodInjection() {
        assert 1 == Book.fetch(1)
        assert [1] == Book.findAll()
        assert [[a:1, b: 2]] == Book.findAll(a: 1, b: 2)
    }
}
