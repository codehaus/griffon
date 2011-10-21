import org.apache.ibatis.session.SqlSession

class BootstrapMybatis {
    def init = { String dataSourceName, SqlSession session ->
    }

    def destroy = { String dataSourceName, SqlSession session ->
    }
} 
