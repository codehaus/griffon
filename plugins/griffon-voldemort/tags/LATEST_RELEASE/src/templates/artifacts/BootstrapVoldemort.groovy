import voldemort.client.StoreClient
import voldemort.client.StoreClientFactory

class BootstrapVoldemort {
    def init = { String clientName, StoreClientFactory clientFactory -> 
    }

    def destroy = { String clientName, StoreClientFactory clientFactory ->
    }
} 
