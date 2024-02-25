package serviceTests;

import dataAccess.*;
import model.request.CreateGameRequest;
import model.result.CreateGameResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GameService;

import java.util.UUID;

public class CreateGameServiceTests {

    @Test
    void createGameServiceSuccess() throws DataAccessException {
        // create new databases and initialize GameService
        GameDAO gameDAO = new GameDAOMemory();
        AuthDAO authDAO = new AuthDAOMemory();
        GameService gameService = new GameService(gameDAO, authDAO);

        // create an authToken and save the username to the database
        String authToken = authDAO.createAuth("TestUsername");

        // make sure a new GameData object was created in the database
        CreateGameRequest req = new CreateGameRequest("TestGameName");
        CreateGameResult res = gameService.createGame(req, authToken);
        Assertions.assertNotNull(gameDAO.getGame(res.gameID()));
    }

    @Test
    void createGameServiceErrors() {
        // create new databases and initialize UserService
        GameDAO gameDAO = new GameDAOMemory();
        AuthDAO authDAO = new AuthDAOMemory();
        GameService gameService = new GameService(gameDAO, authDAO);

        // create an authToken and save the username to the database
        String authToken = authDAO.createAuth("TestUsername");

        // make sure an unauthorized authToken does not work
        String badAuthToken = UUID.randomUUID().toString();
        CreateGameRequest req = new CreateGameRequest("TestGameName");
        Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame(req, badAuthToken));

        // an empty gameName should throw an error
        CreateGameRequest newReq = new CreateGameRequest(null);
        Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame(newReq, authToken));
    }

}
