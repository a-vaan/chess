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
    void listGamesServiceSuccess() throws DataAccessException {
        // create new databases and initialize GameService
        GameDAO listGameDAO = new GameDAOMemory();
        AuthDAO listAuthDAO = new AuthDAOMemory();
        GameService listGameService = new GameService(listGameDAO, listAuthDAO);

        // create an authToken, save the username to the database, and add 4 games to the database
        String authToken = listAuthDAO.createAuth("TestUsername");
        listGameDAO.createGame("TestGame1");
        listGameDAO.createGame("TestGame2");
        listGameDAO.createGame("TestGame3");
        listGameDAO.createGame("TestGame4");

        // make sure the ListGamesResult object is returning correctly
        ListGamesRequest req = new ListGamesRequest(authToken);
        var res = listGameService.listGames(req);
        Assertions.assertEquals(4, res.games().size());

        // delete all games to keep the database clear for other tests
        listGameDAO.deleteAllGames();
    }

    @Test
    void listGamesServiceErrors() {
        // create new databases and initialize GameService
        GameDAO listGameErrorDAO = new GameDAOMemory();
        AuthDAO listAuthErrorDAO = new AuthDAOMemory();
        GameService listGameService = new GameService(listGameErrorDAO, listAuthErrorDAO);

        // create an authToken and save the username to the database
        listAuthErrorDAO.createAuth("TestUsername");

        // make sure an incorrect auth does not get through
        ListGamesRequest req = new ListGamesRequest(UUID.randomUUID().toString());
        Assertions.assertThrows(DataAccessException.class, () -> listGameService.listGames(req));

        // delete all games to keep the database clear for other tests
        listGameErrorDAO.deleteAllGames();
    }
}
