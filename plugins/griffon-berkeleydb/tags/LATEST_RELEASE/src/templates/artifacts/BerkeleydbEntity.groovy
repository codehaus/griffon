@artifact.package@import com.sleepycat.persist.model.Entity
import com.sleepycat.persist.model.PrimaryKey
import com.sleepycat.je.DatabaseException
import com.sleepycat.persist.EntityCursor
import com.sleepycat.persist.EntityStore
import com.sleepycat.persist.PrimaryIndex

@Entity
class @artifact.name@ {
    @PrimaryKey
    Integer id
    String name

    static class Accessor {
        private final PrimaryIndex<Integer, @artifact.name@> @type.property.name@ById

        Accessor(EntityStore store) throws DatabaseException {
            @type.property.name@ById = store.getPrimaryIndex(Integer, @artifact.name@)
        }
        
        void save(@artifact.name@ @type.property.name@) throws DatabaseException {
            @type.property.name@ById.put(@type.property.name@)
        }
        
        EntityCursor<@artifact.name@> list() throws DatabaseException {
            @type.property.name@ById.entities()
        }
    }
}
