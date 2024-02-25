package serviceTests;

import dataAccess.*;
import dataAccess.DAOInterfaces.AuthDAO;
import dataAccess.DAOInterfaces.GameDAO;
import dataAccess.DAOInterfaces.UserDAO;
import dataAccess.MemoryDAOs.AuthDAOMemory;
import dataAccess.MemoryDAOs.GameDAOMemory;
import dataAccess.MemoryDAOs.UserDAOMemory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.DeleteService;

public class DeleteAllServiceTests {

    @Test
    void createGameServiceSuccess() throws DataAccessException {
        // create new databases and initialize GameService
        GameDAO gameDAO = new GameDAOMemory();
        UserDAO userDAO = new UserDAOMemory();
        AuthDAO authDAO = new AuthDAOMemory();
        DeleteService deleteService = new DeleteService(userDAO, authDAO, gameDAO);

        // add 4 items to each database
        userDAO.createUser("TestUsername1", "TestPassword1","Test@Email1");
        userDAO.createUser("TestUsername2", "TestPassword2","Test@Email2");
        userDAO.createUser("TestUsername3", "TestPassword3","Test@Email3");
        userDAO.createUser("TestUsername4", "TestPassword4","Test@Email4");
        String authToken1 = authDAO.createAuth("TestUsername1");
        String authToken2 =authDAO.createAuth("TestUsername2");
        String authToken3 =authDAO.createAuth("TestUsername3");
        String authToken4 =authDAO.createAuth("TestUsername4");
        int gameID1 = gameDAO.createGame("TestGame1");
        int gameID2 = gameDAO.createGame("TestGame2");
        int gameID3 = gameDAO.createGame("TestGame3");
        int gameID4 = gameDAO.createGame("TestGame4");

        // make sure all databases are empty after running deleteAll
        deleteService.deleteAll();
        Assertions.assertNull(userDAO.getUser("TestUsername1"));
        Assertions.assertNull(userDAO.getUser("TestUsername2"));
        Assertions.assertNull(userDAO.getUser("TestUsername3"));
        Assertions.assertNull(userDAO.getUser("TestUsername4"));
        Assertions.assertNull(authDAO.getAuth(authToken1));
        Assertions.assertNull(authDAO.getAuth(authToken2));
        Assertions.assertNull(authDAO.getAuth(authToken3));
        Assertions.assertNull(authDAO.getAuth(authToken4));
        Assertions.assertNull(gameDAO.getGame(gameID1));
        Assertions.assertNull(gameDAO.getGame(gameID2));
        Assertions.assertNull(gameDAO.getGame(gameID3));
        Assertions.assertNull(gameDAO.getGame(gameID4));
    }

}
