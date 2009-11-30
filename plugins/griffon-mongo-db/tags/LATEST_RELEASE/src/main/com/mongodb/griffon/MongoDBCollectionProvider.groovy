package com.mongodb.griffon

import com.mongodb.*
import com.google.inject.*
import com.google.inject.name.*

public class MongoDBCollectionProvider implements Provider<DBCollection> {
    String collectionName
    @Inject
    private Provider<DB> dbProvider
    
    
    @Inject
    public MongoDBCollectionProvider(@Named("mongo.collectionName") String collectionName) {
        this.collectionName = collectionName;
    }
    
    public DBCollection get() {
        DBCollection collection
        if (collectionName != null)
            collection = dbProvider.get().getCollection(collectionName)
        else collection = dbProvider.get().getCollection("testCollection")
        return collection
    }
}
