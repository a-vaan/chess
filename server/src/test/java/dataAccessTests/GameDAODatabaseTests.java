package dataAccessTests;

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

        if(newGameData.game().equals(retrievedData.game())){
            System.out.println("potatoes");
        }

        ChessGame databaseGame = retrievedData.game();
        ChessGame newGame = newGameData.game();

        if(newGame.equals(databaseGame)){
            System.out.println("potatoes");
        }

        Assertions.assertEquals(newGameData, retrievedData);
        Assertions.assertEquals(newGameData1, retrievedData1);
    }

    @Test
    void listGamesSuccess() throws DataAccessException {
        GameDAO listGamesDAO = new GameDAODatabase();
        listGamesDAO.deleteAllGames();

        int gameID = listGamesDAO.createGame("TestGameList");
        int gameID1 = listGamesDAO.createGame("TestGameList1");
        int gameID2 = listGamesDAO.createGame("TestGameList2");

        Collection<GameData> gameList = listGamesDAO.listGames();

        Assertions.assertEquals(3, gameList.size());
    }


}
