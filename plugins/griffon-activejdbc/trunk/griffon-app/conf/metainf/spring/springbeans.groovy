import griffon.activejdbc.BaseHolder
import griffon.spring.factory.support.ObjectFactoryBean

'activeJdbcDataSource'(ObjectFactoryBean) {
    object = BaseHolder.instance.dataSource
}
