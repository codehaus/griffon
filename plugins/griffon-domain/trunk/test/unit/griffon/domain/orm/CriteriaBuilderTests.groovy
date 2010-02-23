package griffon.domain.orm

class CriteriaBuilderTests extends GroovyTestCase {
    private CriteriaBuilder builder = new CriteriaBuilder()

    void testSmoke() {
        assert builder.name("foo") == new BinaryExpression("name", Operator.EQUAL, "foo")
        assert builder.name(operator: Operator.NOT_EQUAL, "foo") == new BinaryExpression("name", Operator.NOT_EQUAL, "foo")
        assert builder.eq(propertyName: "name", value: "foo") == new BinaryExpression("name", Operator.EQUAL, "foo")
        assert builder.eq(propertyName: "name", otherPropertyName: "lastname") == new PropertyExpression("name", Operator.EQUAL, "lastname")

        CompositeCriterion and = builder.and {
            name("foo")
            lastname("bar")
        }
        assert and.operator == Operator.AND
        assert and.criteria[0] == new BinaryExpression("name", Operator.EQUAL, "foo")
        assert and.criteria[1] == new BinaryExpression("lastname", Operator.EQUAL, "bar")

        def criteria = {
            or {
                name("foo")
                lastname("bar")
            }
        }
        criteria.delegate = builder
        CompositeCriterion or = criteria()
        assert or.operator == Operator.OR
        assert or.criteria[0] == new BinaryExpression("name", Operator.EQUAL, "foo")
        assert or.criteria[1] == new BinaryExpression("lastname", Operator.EQUAL, "bar")
    }
}
