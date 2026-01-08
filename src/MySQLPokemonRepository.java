import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class MySQLPokemonRepository implements PokemonRepository {

    @Override
    public int catchNewPokemon(Pokemon p, int hp, int attack, int defense) throws SQLException {
        Connection conn = DatabaseConfig.getInstance().getConnection();
        int newId = 0;
        try {
            conn.setAutoCommit(false);

            String pokemonSql = "INSERT INTO pokemons (trainer_id, nickname, rarity) VALUES (?, ?, ?)";
            try (PreparedStatement pStmt = conn.prepareStatement(pokemonSql, Statement.RETURN_GENERATED_KEYS)) {
                pStmt.setInt(1, p.getTrainerId());
                pStmt.setString(2, p.getNickname());
                pStmt.setString(3, p.getRarity());
                pStmt.executeUpdate();

                ResultSet rs = pStmt.getGeneratedKeys();
                if (rs.next()) newId = rs.getInt(1);
            }

            String statsSql = "INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense) VALUES (?, ?, ?, ?)";
            try (PreparedStatement sStmt = conn.prepareStatement(statsSql)) {
                sStmt.setInt(1, newId);
                sStmt.setInt(2, hp);
                sStmt.setInt(3, attack);
                sStmt.setInt(4, defense);
                sStmt.executeUpdate();
            }

            conn.commit();
            return newId;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }


    @Override
    public void addTypeToPokemon(int pokemonId, int typeId) throws SQLException {
        String sql = "INSERT INTO pokemon_types (pokemon_id, type_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pokemonId);
            pstmt.setInt(2, typeId);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deletePokemon(int id) throws SQLException {
        String sql = "DELETE FROM pokemons WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public void importPokemonsFromCSV(String filePath) throws SQLException {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            throw new SQLException("The selected file is empty or does not exist.");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String header = br.readLine();
            if (header == null) throw new SQLException("File has no content.");

            int importedCount = 0;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 6) {
                    Pokemon p = new Pokemon(Integer.parseInt(data[0]), data[1], data[2]);
                    int hp = Integer.parseInt(data[3]);
                    int atk = Integer.parseInt(data[4]);
                    int def = Integer.parseInt(data[5]);

                    catchNewPokemon(p, hp, atk, def);
                    importedCount++;
                }
            }

            if (importedCount == 0) {
                throw new SQLException("No valid Pokémon data found in the file.");
            }
        } catch (IOException e) {
            throw new SQLException("Reading error: " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new SQLException("Data format error: Check if all numbers are valid.");
        }
    }

    private void addTypeByName(int pokemonId, String typeName) throws SQLException {
        String sql = "INSERT INTO pokemon_types (pokemon_id, type_id) " +
                "SELECT ?, id FROM types WHERE type_name = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pokemonId);
            pstmt.setString(2, typeName);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void updatePokemon(int id, String nickname, String rarity, int hp, int attack, int defense) throws SQLException {
        String sqlPoke = "UPDATE pokemons SET nickname = ?, rarity = ? WHERE id = ?";
        String sqlStats = "UPDATE pokemon_stats SET hp = ?, attack = ?, defense = ? WHERE pokemon_id = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlPoke);
                 PreparedStatement ps2 = conn.prepareStatement(sqlStats)) {

                ps1.setString(1, nickname);
                ps1.setString(2, rarity);
                ps1.setInt(3, id);
                ps1.executeUpdate();

                ps2.setInt(1, hp);
                ps2.setInt(2, attack);
                ps2.setInt(3, defense);
                ps2.setInt(4, id);
                ps2.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    @Override
    public void updatePokemonTypes(int pokemonId, Integer type1Id, Integer type2Id) throws SQLException {
        String deleteSql = "DELETE FROM pokemon_types WHERE pokemon_id = ?";
        String insertSql = "INSERT INTO pokemon_types (pokemon_id, type_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConfig.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement deletePs = conn.prepareStatement(deleteSql)) {
                deletePs.setInt(1, pokemonId);
                deletePs.executeUpdate();

                try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                    if (type1Id != null) {
                        insertPs.setInt(1, pokemonId);
                        insertPs.setInt(2, type1Id);
                        insertPs.executeUpdate();
                    }
                    if (type2Id != null && !type2Id.equals(type1Id)) {
                        insertPs.setInt(1, pokemonId);
                        insertPs.setInt(2, type2Id);
                        insertPs.executeUpdate();
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
}