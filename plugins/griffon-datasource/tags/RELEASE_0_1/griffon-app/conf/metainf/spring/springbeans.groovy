import griffon.plugins.datasource.DataSourceHolder
import griffon.spring.factory.support.ObjectFactoryBean

'dataSource'(ObjectFactoryBean) {
    object = DataSourceHolder.instance.getDataSource('default')
}

