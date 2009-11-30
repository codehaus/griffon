package com.mongodb.griffon

import com.mongodb.*
import com.google.inject.*
import com.google.inject.name.*

public class MongoProvider implements Provider<Mongo> {
    String host
    Integer port
    

    @Inject
    public MongoProvider(@Named("mongo.host") String host, @Named("mongo.port") Integer port) {
        this.host = host
        this.port = port
    }
    
    public Mongo get() {
        Mongo mongo
        if (host == null)
            mongo = new Mongo()
        else mongo = (port == null) ? new Mongo(host) : new Mongo(host, port)
        return mongo
    }
}
