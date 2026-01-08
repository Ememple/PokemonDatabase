/**
 * Represents a Types entity in the database
 */
public class Types {
    private int id;
    private String typeName;

    public Types(int id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public int getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }
}
