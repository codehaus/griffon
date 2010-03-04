import com.mongodb.*
import com.google.inject.*
import com.google.inject.name.*
import com.mongodb.griffon.*

public interface DBCollectionFactory {
	public DBCollection getCollection(String name)
}

public class MongoDBCollectionFactory implements DBCollectionFactory {
	private final Provider<DB> dbProvider

	@Inject
	public MongoDBCollectionFactory(Provider<DB> dbProvider) {
		this.dbProvider = dbProvider
	}

	public DBCollection getCollection(String name) {
        def collection = dbProvider.get().getCollection(name)
        decorateCollection(collection)
		return collection
	}
    
    public DBCollection getCollection(String name, String username, char [] password) {
        def db = dbProvider.get()
        if (db.authenticate(username, password)) {
            def collection = db.getCollection(name)
            decorateCollection(collection)
            return collection
        } else return null
    }
    
    static {
		//meta programming convenience methods courtesy of jareed
        DBCollection.metaClass {
	        findAll << { LinkedHashMap query -> delegate.find(query as BasicDBObject) }
			findAll << { LinkedHashMap query, LinkedHashMap filter -> delegate.find(query as BasicDBObject, filter as BasicDBObject) }
			find << { LinkedHashMap query -> delegate.find(query as BasicDBObject).find {true} }
			find << { LinkedHashMap query, LinkedHashMap filter -> delegate.find(query as BasicDBObject, filter as BasicDBObject).find {true} }
			insert << { LinkedHashMap doc -> delegate.insert(doc as BasicDBObject) }
			update << { BasicDBObject doc, LinkedHashMap op -> delegate.update(doc, op as BasicDBObject) }
		}
	}
	
	void decorateCollection(DBCollection collection) {
		println "decorateCollection"
        collection.metaClass.methodMissing = {String methodName, args ->
	            def operators = [
	                "LessThan":'$lt',
	                "LessThanEquals":'$lte',
	                "GreaterThan":'$gt',
	                "GreaterThanEquals":'$gte',
	                "InList":'$in',
	                "NotInList":'$nin',
	                "Exists":'$exists',
	                "AllInList":'$all',
	                "NotEqual":'$ne',
	                "ArraySize":'$size',
	                //modifiers
	                "WithLimit":'$limit',
	                "WithSkip":'$skip'  
	            ]  
	
	            def matcher = (methodName =~ /^(find)(\w+)$/)
	            if (matcher.matches()) {
	                def temp = matcher.group(2) - "By"
	                def list = temp.split("And")
	                def map = [:]
	                
	                for (item in list) {
	                    def property
	                    def operator
	                    def hasOperator = false
	
	                    for (op in operators.keySet()) {
	                        if (item.endsWith(op)) {
	                            operator = operators[op]
	                            property = item.replace(op,"").replace("_",".").toLowerCase()
	                            if (operator.equals('$limit') || operator.equals('$skip')) {
	                                if (!property.equals(""))
	                                    map.put(property, '$eq')
	                                map.put(operator, "")
	                            } else map.put(property.toLowerCase(), operator)
	                            hasOperator = true
	                        }
	                    }
	                    if (!hasOperator) {
	                        def name = item.replace('_','.')
	                        map.put(name.toLowerCase(), '$eq')
	                    }
	                }
	                
	                // create function
	                def function = { arguments ->
	                    def attrs = map
	                    def query
	                    def builder = BasicDBObjectBuilder.start()
	                    builder.start()
	                    map.entrySet().eachWithIndex { entry, i ->
	                        if (!entry.value.equals("")) {
	                            if (entry.value.equals('$eq'))
	                                builder.append(entry.key, arguments[i])
	                            else builder.append(entry.key, [entry.value, arguments[i]] as BasicDBObject)
	                        }
	                    }
	
	                    query = builder.get()
	                    def cursor = collection.find(query)
	                    
	                    // skip and limit
	                    //TODO
	                    return cursor
	                }
	                
	                collection.metaClass.(methodName.toString()) = function
	                function(args)
	            }
	        }
        }
}
