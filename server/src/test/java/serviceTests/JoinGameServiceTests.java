package serviceTests;

import dataAccess.*;
import dataAccess.DAOInterfaces.AuthDAO;
import dataAccess.DAOInterfaces.GameDAO;
import model.GameData;
import model.request.JoinGameRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GameService;

import java.util.Random;

public class JoinGameServiceTests {

    @Test
    void joinGameServiceSuccess() throws DataAccessException {
        // create new databases and initialize GameService
        GameDAO joinGameDAO = new GameDAODatabase();
        AuthDAO joinAuthDAO = new AuthDAODatabase();
        GameService gameService = new GameService(joinGameDAO, joinAuthDAO);

        // create an authToken, save the username to the database, and add the game to the database
        String authToken = joinAuthDAO.createAuth("TestUsername");
        int gameID = joinGameDAO.createGame("TestGame");

        // make sure the database is being updated with the new information
        JoinGameRequest req = new JoinGameRequest("WHITE", gameID);
        gameService.joinGame(req, authToken);
        Assertions.assertEquals(new GameData(gameID, "TestUsername", null,
                "TestGame", joinGameDAO.getGame(gameID).game()), joinGameDAO.getGame(gameID));

        // delete everything to keep the database clear for other tests
        joinGameDAO.deleteAllGames();
        joinAuthDAO.deleteAllAuths();
    }

    @Test
    void joinGameServiceErrors() throws DataAccessException {
        // create new databases and initialize GameService
        GameDAO joinGameErrorDAO = new GameDAODatabase();
        AuthDAO joinAuthErrorDAO = new AuthDAODatabase();
        GameService gameService = new GameService(joinGameErrorDAO, joinAuthErrorDAO);

        // create an authToken, save the username to the database, and add the game to the database
        String authToken = joinAuthErrorDAO.createAuth("TestUsername");
        int gameID = joinGameErrorDAO.createGame("TestGame");

        // make sure when someone tries to add themselves to a color that is already taken an error is thrown
        JoinGameRequest req = new JoinGameRequest("WHITE", gameID);
        gameService.joinGame(req, authToken);
        Assertions.assertThrows(DataAccessException.class, () -> gameService.joinGame(req, authToken));

        // make sure when someone tries to add themselves to a game that doesn't exist an error is thrown
        Random random = new Random();
        JoinGameRequest newReq = new JoinGameRequest("WHITE", random.nextInt(10000));
        Assertions.assertThrows(DataAccessException.class, () -> gameService.joinGame(newReq, authToken));

        // delete everything to keep the database clear for other tests
        joinGameErrorDAO.deleteAllGames();
        joinAuthErrorDAO.deleteAllAuths();
    }

}
