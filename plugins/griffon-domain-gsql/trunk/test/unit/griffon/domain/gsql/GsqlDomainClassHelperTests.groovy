package griffon.domain.gsql

import griffon.domain.orm.*

class GsqlDomainClassHelperTests extends GroovyTestCase {
    private CriteriaBuilder builder = new CriteriaBuilder()

    void testSmoke() {
        Criterion criterion = builder.name('foo')
        def (sql, values) = GsqlDomainClassHelper.instance.toSql(criterion)

        assert sql == 'name = ?'
        assert values == [name: 'foo']

        criterion = builder.and {
            name('foo')
            lastname('bar')
        }
        (sql, values) = GsqlDomainClassHelper.instance.toSql(criterion)
        assert sql == 'name = ? AND lastname = ?'
        assert values == [name: 'foo', lastname: 'bar']
     
        criterion = builder.and {
            name('foo')
            lastname('bar')
            or {
               eq(propertyName: 'id', value: 1)
               address()
            }
        }
        (sql, values) = GsqlDomainClassHelper.instance.toSql(criterion)
        assert sql == 'name = ? AND lastname = ? AND ( id = ? OR address IS NULL )'
        assert values == [name: 'foo', lastname: 'bar', id: 1]
    }
}
