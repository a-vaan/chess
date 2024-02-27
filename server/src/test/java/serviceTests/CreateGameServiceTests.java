package serviceTests;

import dataAccess.*;
import dataAccess.DAOInterfaces.AuthDAO;
import dataAccess.DAOInterfaces.GameDAO;
import dataAccess.MemoryDAOs.AuthDAOMemory;
import dataAccess.MemoryDAOs.GameDAOMemory;
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
        GameDAO createGameDAO = new GameDAOMemory();
        AuthDAO createAuthDAO = new AuthDAOMemory();
        GameService gameService = new GameService(createGameDAO, createAuthDAO);

        // create an authToken and save the username to the database
        String authToken = createAuthDAO.createAuth("TestUsername");

        // make sure a new GameData object was created in the database
        CreateGameRequest req = new CreateGameRequest("TestGameName");
        CreateGameResult res = gameService.createGame(req, authToken);
        Assertions.assertNotNull(createGameDAO.getGame(res.gameID()));

        // delete all games to keep the database clear for other tests
        createGameDAO.deleteAllGames();
    }

    @Test
    void createGameServiceErrors() {
        // create new databases and initialize UserService
        GameDAO createGameErrorDAO = new GameDAOMemory();
        AuthDAO createAuthErrorDAO = new AuthDAOMemory();
        GameService gameService = new GameService(createGameErrorDAO, createAuthErrorDAO);

        // create an authToken and save the username to the database
        String authToken = createAuthErrorDAO.createAuth("TestUsername");

        // make sure an unauthorized authToken does not work
        String badAuthToken = UUID.randomUUID().toString();
        CreateGameRequest req = new CreateGameRequest("TestGameName");
        Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame(req, badAuthToken));

        // an empty gameName should throw an error
        CreateGameRequest newReq = new CreateGameRequest(null);
        Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame(newReq, authToken));

        // delete all games to keep the database clear for other tests
        createGameErrorDAO.deleteAllGames();
    }

}
