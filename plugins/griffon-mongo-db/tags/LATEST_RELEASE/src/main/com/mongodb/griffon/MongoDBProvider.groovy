package com.mongodb.griffon

import com.mongodb.*
import com.google.inject.*
import com.google.inject.name.*

public class MongoDBProvider implements Provider<DB> {
    @Inject
    private Provider<Mongo> mongoProvider
    String dbName
    
    @Inject
    public MongoDBProvider(@Named("mongo.dbName") String dbName) {
        this.dbName = dbName
    }
    
    public DB get() {
        DB database
        if (dbName != null)
            database = mongoProvider.get().getDB(dbName)
        return database
    }
}
