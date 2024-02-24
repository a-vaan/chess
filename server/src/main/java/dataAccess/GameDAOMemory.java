package dataAccess;

import model.AuthData;

import java.util.HashMap;

public class GameDAOMemory implements GameDAO {

    static private final HashMap<String, AuthData> games = new HashMap<>();

    @Override
    public void deleteAllGames() {
        games.clear();
    }

}
