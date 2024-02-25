package dataAccess.DAOInterfaces;

import model.GameData;

import java.util.Collection;

public interface GameDAO {

    Integer createGame(String gameName);

    void updateGame(GameData game);

    GameData getGame(int gameID);

    Collection<GameData> listGames();

    void deleteAllGames();

}
