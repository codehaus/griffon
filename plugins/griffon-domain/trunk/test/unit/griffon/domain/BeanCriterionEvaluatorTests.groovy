package griffon.domain

import griffon.test.*

class BeanCriterionEvaluatorTests extends GriffonUnitTestCase {
    private BeanCriterionEvaluator evaluator = new BeanCriterionEvaluator()

    void testUnaryNull() {
        Bean bean = new Bean()
        assert evaluator.eval(bean) { prop1 == null }
    }

    void testUnaryNotNull() {
        Bean bean = new Bean(prop1: 'prop1')
        assert evaluator.eval(bean) { prop1 != null }
    }

    void testBinaryEqual() {
        Bean bean = new Bean(prop1: 'prop1')
        assert evaluator.eval(bean) { prop1 == 'prop1' }
    }

    void testBinaryNotEqual() {
        Bean bean = new Bean(prop1: 'prop2')
        assert evaluator.eval(bean) { prop1 != 'prop1' }
    }

    void testBinaryGreaterThan() {
        Bean bean = new Bean(prop1: 'bbbb')
        assert evaluator.eval(bean) { prop1 > 'aaaa' }
    }

    void testBinaryGreaterThanOrEqual() {
        Bean bean = new Bean(prop1: 'bbbb')
        assert evaluator.eval(bean) { prop1 >= 'aaaa' }
        bean.prop1 = 'aaaa'
        assert evaluator.eval(bean) { prop1 >= 'aaaa' }
    }

    void testBinaryLessThan() {
        Bean bean = new Bean(prop1: 'aaaa')
        assert evaluator.eval(bean) { prop1 < 'bbbb' }
    }

    void testBinaryLessThanOrEqual() {
        Bean bean = new Bean(prop1: 'aaaa')
        assert evaluator.eval(bean) { prop1 <= 'bbbb' }
        bean.prop1 = 'bbbb'
        assert evaluator.eval(bean) { prop1 <= 'bbbb' }
    }

    void testPropertyEqual() {
        Bean bean = new Bean(prop1: 'prop1', prop2: 'prop1')
        assert evaluator.eval(bean) { prop1 == prop2 }
    }

    void testPropertyNotEqual() {
        Bean bean = new Bean(prop1: 'prop1', prop2: 'prop2')
        assert evaluator.eval(bean) { prop1 != prop2 }
    }

    void testPropertyGreaterThan() {
        Bean bean = new Bean(prop1: 'bbbb', prop2: 'aaaa')
        assert evaluator.eval(bean) { prop1 > prop2 }
    }

    void testPropertyGreaterThanOrEqual() {
        Bean bean = new Bean(prop1: 'bbbb', prop2: 'aaaa')
        assert evaluator.eval(bean) { prop1 >= prop2 }
        bean.prop1 = 'aaaa'
        assert evaluator.eval(bean) { prop1 >= prop2 }
    }

    void testPropertyLessThan() {
        Bean bean = new Bean(prop1: 'aaaa', prop2: 'bbbb')
        assert evaluator.eval(bean) { prop1 < prop2 }
    }

    void testPropertyLessThanOrEqual() {
        Bean bean = new Bean(prop1: 'aaaa', prop2: 'bbbb')
        assert evaluator.eval(bean) { prop1 <= prop2 }
        bean.prop1 = 'bbbb'
        assert evaluator.eval(bean) { prop1 <= prop2 }
    }

    void testComposite() {
        Bean bean = new Bean(prop1: 'aaaa', prop2: 'bbbb')
        assert evaluator.eval(bean) { prop1 == 'aaaa' && prop2 == 'bbbb'}
        assert evaluator.eval(bean) {
            and {
                prop1 'aaaa'
                prop2 'bbbb'
            }
        }
    }
}

class Bean {
    String prop1
    String prop2
}