import java.sql.SQLException;
import java.util.List;

public interface TypeRepository {
    List<Types> getAllTypes() throws SQLException;

    void addType(String typeName) throws SQLException;

    void deleteType(int id) throws SQLException;
}
