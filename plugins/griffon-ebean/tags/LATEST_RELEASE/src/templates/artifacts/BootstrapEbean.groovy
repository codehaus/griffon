import com.avaje.ebean.EbeanServer

class BootstrapEbean {
    def init = { String dataSourceName, EbeanServer ebeanServer ->
    }

    def destroy = { String dataSourceName, EbeanServer ebeanServer ->
    }
} 
