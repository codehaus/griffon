@artifact.package@import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;

@Entity
public class @artifact.name@ {
    @PrimaryKey
    private Integer id;
    private String name;

    public Integer getId() { return id; }
    public String getName() { return name; }

    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    
    public static class Accessor {
        private final PrimaryIndex<Integer, @artifact.name@> @type.property.name@ById;

        public Accessor(EntityStore store) throws DatabaseException {
            @type.property.name@ById = store.getPrimaryIndex(Integer.class, @artifact.name@.class);
        }
        
        public void save(@artifact.name@ @type.property.name@) throws DatabaseException {
            @type.property.name@ById.put(@type.property.name@);
        }
        
        public EntityCursor<@artifact.name@> list() throws DatabaseException {
            return @type.property.name@ById.entities();
        }
    }
}
