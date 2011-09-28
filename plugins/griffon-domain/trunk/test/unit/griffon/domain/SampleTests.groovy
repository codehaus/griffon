package griffon.domain

import griffon.test.*
import griffon.core.GriffonApplication
import griffon.test.mock.MockGriffonApplication

class SampleTests extends GriffonUnitTestCase {
    private GriffonApplication app

    void setUp() {
        ExpandoMetaClassCreationHandle.enable()
        app = new MockGriffonApplication()
        app.builderClass = SampleBuilderConfig
        app.initialize()
    }

    void testDynamicMethodInjection() {
        Sample.make(name: 'Andres', lastName: 'Almiray', num: 30).save()
        assert Sample.findBy('Name', ['Andres']).lastName == 'Almiray'
        assert Sample.findBy('NameAndLastName', ['Andres', 'Almiray']).lastName == 'Almiray'
        assert !Sample.findBy('NameAndLastName', 'Dierk', 'Koenig')
        Sample.make(name: 'Dierk', lastName: 'Koenig', num: 40).save()
        assert Sample.findBy('NameAndLastName', 'Dierk', 'Koenig').lastName == 'Koenig'
        assert Sample.list().name == ['Andres', 'Dierk']
        assert Sample.count() == 2
        assert Sample.list(max: 1, offset: 0).name == ['Andres']
        assert Sample.list(max: 1, offset: 1).name == ['Dierk']
        assert Sample.list(sort: 'num', order: 'asc').num == [30, 40]
        assert Sample.list(sort: 'num', order: 'desc').num == [40, 30]
        Sample.fetch(2).delete()
        assert Sample.count() == 1
        assert Sample.list().name == ['Andres']

        assert Sample.findByName('Andres').lastName == 'Almiray'
        Sample.make(name: 'Dierk', lastName: 'Koenig', num: 40).save()
        Sample.make(name: 'Guillaume', lastName: 'Laforge', num: 30).save()
        assert Sample.countByNum(30) == 2
        assert Sample.findAllByNum(30).name == ['Andres', 'Guillaume']

        assert Sample.mapping() == 'memory'
        assert Sample.datasource() == 'default'
    }
}