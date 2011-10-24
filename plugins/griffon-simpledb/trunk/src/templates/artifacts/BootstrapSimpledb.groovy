import com.amazonaws.services.simpledb.AmazonSimpleDB

class BootstrapSimpledb {
    def init = { String clientName, AmazonSimpleDB client -> 
    }

    def destroy = { String clientName, AmazonSimpleDB client ->
    }
} 
