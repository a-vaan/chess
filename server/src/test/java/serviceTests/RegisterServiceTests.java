package serviceTests;

import dataAccess.*;
import dataAccess.DAOInterfaces.AuthDAO;
import dataAccess.DAOInterfaces.UserDAO;
import dataAccess.MemoryDAOs.AuthDAOMemory;
import dataAccess.MemoryDAOs.UserDAOMemory;
import model.request.RegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.UserService;

public class RegisterServiceTests {

    @Test
    void registerServiceSuccess() throws DataAccessException {
        // create new databases and initialize UserService
        UserDAO userDAO = new UserDAOMemory();
        AuthDAO authDAO = new AuthDAOMemory();
        UserService userService = new UserService(userDAO, authDAO);

        // make sure the correct info was put into the database and that the response item is correct.
        RegisterRequest reg = new RegisterRequest("TestUsernameReg", "TestPasswordReg", "Test@EmailReg");
        var res = userService.register(reg);
        Assertions.assertEquals("TestUsernameReg", userDAO.getUser("TestUsernameReg").username());
        Assertions.assertEquals("TestPasswordReg", userDAO.getUser("TestUsernameReg").password());
        Assertions.assertEquals("Test@EmailReg", userDAO.getUser("TestUsernameReg").email());
        Assertions.assertEquals("TestUsernameReg", res.username());
        Assertions.assertNotEquals("", res.authToken());


    }

    @Test
    void registerServiceErrors() throws DataAccessException {
        // create new databases and initialize UserService
        UserDAO userDAO = new UserDAOMemory();
        AuthDAO authDAO = new AuthDAOMemory();
        UserService userService = new UserService(userDAO, authDAO);

        RegisterRequest reg = new RegisterRequest("TestUsername1", "TestPassword1", "Test@Email1");
        userService.register(reg);

        // submit the same request again
        RegisterRequest sameReg = new RegisterRequest("TestUsername1", "TestPassword1", "Test@Email1");
        Assertions.assertThrows(DataAccessException.class, () -> userService.register(sameReg));

        // submit an unacceptable RegisterRequest object
        RegisterRequest newReg = new RegisterRequest("", "TestPassword", "Test@Email");
        Assertions.assertThrows(DataAccessException.class, () -> userService.register(newReg));    }

}
