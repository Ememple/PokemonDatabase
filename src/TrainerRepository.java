import java.sql.SQLException;
import java.util.List;

/**
 * Interface defining the data access layer for Trainer entities
 */
public interface TrainerRepository {
    List<Trainer> getAllTrainers() throws SQLException;

    void addTrainer(String name, float xp, boolean isLeader) throws SQLException;

    void updateTrainer(int id, String name, float xp, boolean isGymLeader) throws SQLException;

    void deleteTrainer(int id) throws SQLException;

    void importTrainersFromCSV(String filePath) throws SQLException;
}
