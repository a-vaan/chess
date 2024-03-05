package dataAccess.DatabaseDAOs;

import dataAccess.DAOInterfaces.GameDAO;
import model.GameData;

import java.util.Collection;

public class GameDAODatabase implements GameDAO {
    @Override
    public Integer createGame(String gameName) {
        return null;
    }

    @Override
    public void updateGame(GameData game) {

    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return null;
    }

    @Override
    public void deleteAllGames() {

    }
}
