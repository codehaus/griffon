import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.Transaction
import org.neo4j.graphdb.Node

class BootstrapNeo4j {
    def init = { String databaseName, GraphDatabaseService db, Transaction tx -> 
    }

    def destroy = { String databaseName, GraphDatabaseService db, Transaction tx ->
    }
} 
