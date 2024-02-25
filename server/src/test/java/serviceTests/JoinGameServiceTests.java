package serviceTests;

import dataAccess.*;
import model.GameData;
import model.request.JoinGameRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GameService;

import java.util.Random;

public class JoinGameServiceTests {

    @Test
    void createGameServiceSuccess() throws DataAccessException {
        // create new databases and initialize GameService
        GameDAO gameDAO = new GameDAOMemory();
        AuthDAO authDAO = new AuthDAOMemory();
        GameService gameService = new GameService(gameDAO, authDAO);

        // create an authToken, save the username to the database, and add the game to the database
        String authToken = authDAO.createAuth("TestUsername");
        int gameID = gameDAO.createGame("TestGame");

        // make sure the database is being updated with the new information
        JoinGameRequest req = new JoinGameRequest("WHITE", gameID);
        gameService.joinGame(req, authToken);
        Assertions.assertEquals(new GameData(gameID, "TestUsername", null,
                "TestGame", gameDAO.getGame(gameID).game()), gameDAO.getGame(gameID));
    }

    @Test
    void createGameServiceErrors() throws DataAccessException {
        // create new databases and initialize GameService
        GameDAO gameDAO = new GameDAOMemory();
        AuthDAO authDAO = new AuthDAOMemory();
        GameService gameService = new GameService(gameDAO, authDAO);

        // create an authToken, save the username to the database, and add the game to the database
        String authToken = authDAO.createAuth("TestUsername");
        int gameID = gameDAO.createGame("TestGame");

        // make sure when someone tries to add themselves to a color that is already taken an error is thrown
        JoinGameRequest req = new JoinGameRequest("WHITE", gameID);
        gameService.joinGame(req, authToken);
        Assertions.assertThrows(DataAccessException.class, () -> gameService.joinGame(req, authToken));

        // make sure when someone tries to add themselves to a game that doesn't exist an error is thrown
        Random random = new Random();
        JoinGameRequest newReq = new JoinGameRequest("WHITE", random.nextInt(10000));
        Assertions.assertThrows(DataAccessException.class, () -> gameService.joinGame(newReq, authToken));
    }

}
