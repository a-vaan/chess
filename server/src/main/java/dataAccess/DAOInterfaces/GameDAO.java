package dataAccess.DAOInterfaces;

import dataAccess.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {

    /**
     * Creates a game and saves it to the database.
     *
     * @param gameName: desired name of the game to be created
     * return: the gameID in the form of an integer
     */
    Integer createGame(String gameName);

    /**
     * Replaces the GameData at the current gameID with the provided GameData
     *
     * @param game: a GameData object containing the new information to be saved
     */
    void updateGame(GameData game);

    /**
     * Returns the GameData object from the database based on the gameID provided.
     *
     * @param gameID: gameID of the desired game
     * return: the GameData object saved with the specified gameID
     */
    GameData getGame(int gameID) throws DataAccessException;

    /**
     * Lists all the games currently contained in the database.
     * return: a list of all GameData objects in the database
     */
    Collection<GameData> listGames() throws DataAccessException;

    /**
     * Deletes all the GameData objects in the database.
     */
    void deleteAllGames();

}
