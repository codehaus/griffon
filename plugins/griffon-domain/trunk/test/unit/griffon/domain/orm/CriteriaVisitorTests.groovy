package griffon.domain.orm

class CriteriaVisitorTests extends GroovyTestCase {
    void testSmoke() {
        Criterion c = CriteriaVisitor.visit { name == 'foo' }
        assert c == new BinaryExpression('name', Operator.EQUAL, 'foo')
        c = CriteriaVisitor.visit { 'foo' == name }
        assert c == new BinaryExpression('name', Operator.EQUAL, 'foo')

        c = CriteriaVisitor.visit { name != 'foo' }
        assert c == new BinaryExpression('name', Operator.NOT_EQUAL, 'foo')
        c = CriteriaVisitor.visit { 'foo' != name }
        assert c == new BinaryExpression('name', Operator.NOT_EQUAL, 'foo')

        c = CriteriaVisitor.visit { name == lastname }
        assert c == new PropertyExpression('name', Operator.EQUAL, 'lastname')

        c = CriteriaVisitor.visit { name == 'foo' && lastname == 'bar' }
        assert c.operator == Operator.AND
        assert c.criteria[0] == new BinaryExpression('name', Operator.EQUAL, 'foo')
        assert c.criteria[1] == new BinaryExpression('lastname', Operator.EQUAL, 'bar')

        c = CriteriaVisitor.visit { name == 'foo' || lastname == 'bar' }
        assert c.operator == Operator.OR
        assert c.criteria[0] == new BinaryExpression('name', Operator.EQUAL, 'foo')
        assert c.criteria[1] == new BinaryExpression('lastname', Operator.EQUAL, 'bar')

        c = CriteriaVisitor.visit { name == null }
        assert c == new UnaryExpression('name', Operator.IS_NULL)
        c = CriteriaVisitor.visit { null == name }
        assert c == new UnaryExpression('name', Operator.IS_NULL)

        c = CriteriaVisitor.visit { name != null }
        assert c == new UnaryExpression('name', Operator.IS_NOT_NULL)
        c = CriteriaVisitor.visit { null != name }
        assert c == new UnaryExpression('name', Operator.IS_NOT_NULL)

        c = CriteriaVisitor.visit { id > 1 }
        assert c == new BinaryExpression('id', Operator.GREATER_THAN, 1)
        c = CriteriaVisitor.visit { 1 < id }
        assert c == new BinaryExpression('id', Operator.GREATER_THAN_OR_EQUAL, 1)

        c = CriteriaVisitor.visit { name == 'foo' && lastname }
        assert c.operator == Operator.AND
        assert c.criteria[0] == new BinaryExpression('name', Operator.EQUAL, 'foo')
        assert c.criteria[1] == new UnaryExpression('lastname', Operator.IS_NOT_NULL)

        c = CriteriaVisitor.visit { name && lastname == 'bar'}
        assert c.operator == Operator.AND
        assert c.criteria[0] == new UnaryExpression('name', Operator.IS_NOT_NULL)
        assert c.criteria[1] == new BinaryExpression('lastname', Operator.EQUAL, 'bar')
    }

    void testErrors() {
        shouldFail(CriteriaVisitException) {
            CriteriaVisitor.visit { 1 == 1 }
        }
        shouldFail(CriteriaVisitException) {
            CriteriaVisitor.visit { name <=> 'foo' }
        }
    }
}
