import java.io.BufferedReader;
import java.io.File;
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
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            throw new SQLException("The trainer file is empty or does not exist.");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String header = br.readLine();
            if (header == null) throw new SQLException("Trainer file has no header.");

            int count = 0;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 3) {
                    String name = data[0].trim();
                    float xp = Float.parseFloat(data[1].trim());
                    boolean isGymLeader = Boolean.parseBoolean(data[2].trim());

                    addTrainer(name, xp, isGymLeader);
                    count++;
                }
            }
            if (count == 0) throw new SQLException("No valid trainer records were processed.");
        } catch (IOException e) {
            throw new SQLException("File access error: " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new SQLException("XP format error: Please ensure experience is a number.");
        }
    }

}
