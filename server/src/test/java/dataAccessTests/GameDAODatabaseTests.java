package dataAccessTests;

import chess.ChessBoard;
import chess.ChessGame;
import dataAccess.DAOInterfaces.GameDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAODatabase;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class GameDAODatabaseTests {

    @Test
    void createGameSuccess() throws DataAccessException {
        GameDAO createGameDAO = new GameDAODatabase();

        int gameID = createGameDAO.createGame("TestGameCreate");
        int gameID1 = createGameDAO.createGame("TestGameCreate1");

        GameData retrievedData = createGameDAO.getGame(gameID);
        GameData retrievedData1 = createGameDAO.getGame(gameID1);

        GameData newGameData = new GameData(gameID, null, null, "TestGameCreate", new ChessGame());
        GameData newGameData1 = new GameData(gameID1, null, null, "TestGameCreate1", new ChessGame());

        Assertions.assertEquals(newGameData, retrievedData);
        Assertions.assertEquals(newGameData1, retrievedData1);
    }

    @Test
    void getGameSuccess() throws DataAccessException {
        GameDAO getGameDAO = new GameDAODatabase();

        int gameID = getGameDAO.createGame("TestGameGet");
        int gameID1 = getGameDAO.createGame("TestGameGet1");

        GameData retrievedData = getGameDAO.getGame(gameID);
        GameData retrievedData1 = getGameDAO.getGame(gameID1);

        GameData newGameData = new GameData(gameID, null, null, "TestGameGet", new ChessGame());
        GameData newGameData1 = new GameData(gameID1, null, null, "TestGameGet1", new ChessGame());

        Assertions.assertEquals(newGameData, retrievedData);
        Assertions.assertEquals(newGameData1, retrievedData1);
    }

    @Test
    void updateGameSuccess() throws DataAccessException {
        GameDAO updateGameDAO = new GameDAODatabase();

        int gameID = updateGameDAO.createGame("TestGameUpdate");
        int gameID1 = updateGameDAO.createGame("TestGameUpdate1");

        ChessGame newChessGame = new ChessGame();
        newChessGame.setBoard(new ChessBoard());

        updateGameDAO.updateGame(new GameData(gameID, null, null, "TestGameUpdate", newChessGame));

        GameData newGameData = new GameData(gameID, null, null, "TestGameUpdate", newChessGame);
        GameData newGameData1 = new GameData(gameID1, null, null, "TestGameUpdate1", new ChessGame());

        GameData retrievedData = updateGameDAO.getGame(gameID);
        GameData retrievedData1 = updateGameDAO.getGame(gameID1);

        Assertions.assertEquals(newGameData, retrievedData);
        Assertions.assertEquals(newGameData1, retrievedData1);
    }

    @Test
    void listGamesSuccess() throws DataAccessException {
        GameDAO listGamesDAO = new GameDAODatabase();
        listGamesDAO.deleteAllGames();

        listGamesDAO.createGame("TestGameList");
        listGamesDAO.createGame("TestGameList1");
        listGamesDAO.createGame("TestGameList2");

        Collection<GameData> gameList = listGamesDAO.listGames();

        Assertions.assertEquals(3, gameList.size());
    }

    @Test
    void deleteAllGamesSuccess() throws DataAccessException {
        GameDAO deleteAllGamesDAO = new GameDAODatabase();

        int gameID = deleteAllGamesDAO.createGame("TestGameDeleteAll");
        int gameID1 = deleteAllGamesDAO.createGame("TestGameDeleteAll1");

        deleteAllGamesDAO.deleteAllGames();

        Assertions.assertNull(deleteAllGamesDAO.getGame(gameID));
        Assertions.assertNull(deleteAllGamesDAO.getGame(gameID1));
    }
}
