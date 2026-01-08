import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLTrainerRepository implements TrainerRepository {

    @Override
    public List<Trainer> getAllTrainers() throws SQLException {
        List<Trainer> trainers = new ArrayList<>();
        String sql = "SELECT * FROM trainers";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                trainers.add(new Trainer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getFloat("experience_points"),
                        rs.getBoolean("is_gym_leader")
                ));
            }
        }
        return trainers;
    }

    @Override
    public void addTrainer(String name, float xp, boolean isLeader) throws SQLException {
        String sql = "INSERT INTO trainers (name, experience_points, is_gym_leader) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setFloat(2, xp);
            pstmt.setBoolean(3, isLeader);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deleteTrainer(int id) throws SQLException {
        String sql = "DELETE FROM trainers WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void updateTrainer(int id, String name, float xp, boolean isGymLeader) throws SQLException {
        String sql = "UPDATE trainers SET name = ?, xp = ?, is_gym_leader = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setFloat(2, xp);
            pstmt.setBoolean(3, isGymLeader);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void importTrainersFromCSV(String filePath) throws SQLException {
        String sql = "INSERT INTO trainers (name, experience_points, is_gym_leader) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String line;
            boolean firstLine = true;

            conn.setAutoCommit(false);

            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; }

                String[] data = line.split(";");
                if (data.length >= 3) {
                    pstmt.setString(1, data[0].trim());
                    pstmt.setFloat(2, Float.parseFloat(data[1].trim()));
                    pstmt.setBoolean(3, data[2].trim().equalsIgnoreCase("true") || data[2].trim().equals("1"));
                    pstmt.addBatch();
                }
            }
            pstmt.executeBatch();
            conn.commit();
            System.out.println("Import trenérů dokončen.");

        } catch (IOException e) {
            System.err.println("Chyba při čtení CSV: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Chyba při importu do DB: " + e.getMessage());
        }
    }

}
