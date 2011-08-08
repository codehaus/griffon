import org.apache.ibatis.session.SqlSession

class BootstrapMybatis {
    def init = { String dataSourceName = 'default', SqlSession session ->
    }

    def destroy = { String dataSourceName = 'default', SqlSession session ->
    }
} 
