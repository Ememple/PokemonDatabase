import java.sql.SQLException;
import java.util.List;

/**
 * Interface defining the data access layer for Types entities
 */
public interface TypeRepository {
    List<Types> getAllTypes() throws SQLException;

    void addType(String typeName) throws SQLException;

    void deleteType(int id) throws SQLException;
}
