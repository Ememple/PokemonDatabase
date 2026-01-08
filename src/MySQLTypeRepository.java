import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLTypeRepository implements TypeRepository {

    @Override
    public List<Types> getAllTypes() throws SQLException {
        List<Types> types = new ArrayList<>();
        String sql = "SELECT id, type_name FROM types ORDER BY type_name ASC";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                types.add(new Types(
                        rs.getInt("id"),
                        rs.getString("type_name")
                ));
            }
        }
        return types;
    }

    @Override
    public void addType(String typeName) throws SQLException {
        String sql = "INSERT INTO types (type_name) VALUES (?)";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, typeName);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deleteType(int id) throws SQLException {
        String sql = "DELETE FROM types WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}