package dataAccess;

import model.GameData;

public interface GameDAO {

    Integer createGame(String gameName);

    GameData getGame(int gameID);

    void deleteAllGames();

}
