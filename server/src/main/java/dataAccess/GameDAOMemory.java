package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class GameDAOMemory implements GameDAO {

    static private final HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public Integer createGame(String gameName) {
        Random random = new Random();
        Integer gameID = random.nextInt(10000);
        games.put(gameID, new GameData(gameID, null, null, gameName, new ChessGame()));
        return gameID;
    }

    @Override
    public void updateGame(GameData game) {
        games.put(game.gameID(), game);
    }

    @Override
    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        return games.values();
    }

    @Override
    public void deleteAllGames() {
        games.clear();
    }

}
