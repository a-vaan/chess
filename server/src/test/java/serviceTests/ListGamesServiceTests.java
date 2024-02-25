package serviceTests;

import dataAccess.*;
import dataAccess.DAOInterfaces.AuthDAO;
import dataAccess.DAOInterfaces.GameDAO;
import dataAccess.MemoryDAOs.AuthDAOMemory;
import dataAccess.MemoryDAOs.GameDAOMemory;
import model.request.ListGamesRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GameService;

import java.util.UUID;

public class ListGamesServiceTests {

    @Test
    void createGameServiceSuccess() throws DataAccessException {
        // create new databases and initialize GameService
        GameDAO gameDAO = new GameDAOMemory();
        AuthDAO authDAO = new AuthDAOMemory();
        GameService gameService = new GameService(gameDAO, authDAO);

        // create an authToken, save the username to the database, and add 4 games to the database
        String authToken = authDAO.createAuth("TestUsername");
        gameDAO.createGame("TestGame1");
        gameDAO.createGame("TestGame2");
        gameDAO.createGame("TestGame3");
        gameDAO.createGame("TestGame4");

        // make sure the ListGamesResult object is returning correctly
        ListGamesRequest req = new ListGamesRequest(authToken);
        var res = gameService.listGames(req);
        Assertions.assertEquals(4, res.games().size());
    }

    @Test
    void createGameServiceErrors() {
        // create new databases and initialize GameService
        GameDAO gameDAO = new GameDAOMemory();
        AuthDAO authDAO = new AuthDAOMemory();
        GameService gameService = new GameService(gameDAO, authDAO);

        // create an authToken and save the username to the database
        authDAO.createAuth("TestUsername");

        // make sure an incorrect auth does not get through
        ListGamesRequest req = new ListGamesRequest(UUID.randomUUID().toString());
        Assertions.assertThrows(DataAccessException.class, () -> gameService.listGames(req));
    }
}
