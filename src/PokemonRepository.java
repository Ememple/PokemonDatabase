import java.sql.SQLException;

/**
 * Interface defining the data access layer for Pokémon entities
 */
public interface PokemonRepository {
    int catchNewPokemon(Pokemon p, int hp, int attack, int defense) throws SQLException;

    void addTypeToPokemon(int pokemonId, int typeId) throws SQLException;

    void deletePokemon(int id) throws SQLException;

    void importPokemonsFromCSV(String filePath) throws SQLException;

    void updatePokemon(int id, String nickname, String rarity, int hp, int attack, int defense) throws SQLException;

    void updatePokemonTypes(int pokemonId, Integer type1Id, Integer type2Id) throws SQLException;
}
