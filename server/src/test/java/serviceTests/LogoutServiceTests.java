package serviceTests;

import dataAccess.*;
import dataAccess.DAOInterfaces.AuthDAO;
import dataAccess.DAOInterfaces.UserDAO;
import dataAccess.MemoryDAOs.AuthDAOMemory;
import dataAccess.MemoryDAOs.UserDAOMemory;
import model.request.LogoutRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.UserService;

import java.util.UUID;

public class LogoutServiceTests {

    @Test
    void registerServiceSuccess() throws DataAccessException {
        // create new databases and initialize UserService
        UserDAO userDAO = new UserDAOMemory();
        AuthDAO authDAO = new AuthDAOMemory();
        UserService userService = new UserService(userDAO, authDAO);

        // create an authToken and save the username to the database
        String authToken = authDAO.createAuth("TestUsername");

        // make sure the AuthData object is deleted in the database
        LogoutRequest reg = new LogoutRequest(authToken);
        userService.logout(reg);
        Assertions.assertNull(authDAO.getAuth(authToken));
    }

    @Test
    void registerServiceError() throws DataAccessException {
        // create new databases and initialize UserService
        UserDAO userDAO = new UserDAOMemory();
        AuthDAO authDAO = new AuthDAOMemory();
        UserService userService = new UserService(userDAO, authDAO);

        // save the username to the database
        authDAO.createAuth("TestUsername");

        // if the AuthData object is not in the database, an error should be thrown
        LogoutRequest reg = new LogoutRequest(UUID.randomUUID().toString());
        Assertions.assertThrows(DataAccessException.class, () -> userService.logout(reg));
    }

}
