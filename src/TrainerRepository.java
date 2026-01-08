import java.sql.SQLException;
import java.util.List;

public interface TrainerRepository {
    List<Trainer> getAllTrainers() throws SQLException;

    void addTrainer(String name, float xp, boolean isLeader) throws SQLException;

    void updateTrainer(int id, String name, float xp, boolean isGymLeader) throws SQLException;

    void deleteTrainer(int id) throws SQLException;

    void importTrainersFromCSV(String filePath) throws SQLException;
}
