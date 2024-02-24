package serviceTests;

import dataAccess.*;
import model.request.RegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.DeleteService;
import service.UserService;

import java.net.HttpURLConnection;
import java.util.Objects;

public class RegisterServiceTests {

    @Test
    void registerServiceSuccess() throws DataAccessException {
        // create new databases and initialize UserService
        UserDAO userDAO = new UserDAOMemory();
        AuthDAO authDAO = new AuthDAOMemory();
        UserService userService = new UserService(userDAO, authDAO);

        // make sure the correct info was put into the database and that the response item is correct.
        RegisterRequest reg = new RegisterRequest("TestUsername", "TestPassword", "Test@Email");
        var res = userService.register(reg);
        Assertions.assertEquals("TestUsername", userDAO.getUser("TestUsername").username());
        Assertions.assertEquals("TestPassword", userDAO.getUser("TestUsername").password());
        Assertions.assertEquals("Test@Email", userDAO.getUser("TestUsername").email());
        Assertions.assertEquals("TestUsername", res.username());
        Assertions.assertNotEquals("", res.authToken());


    }

    @Test
    void registerServiceError403() throws DataAccessException {
        // create new databases and initialize UserService
        UserDAO userDAO = new UserDAOMemory();
        AuthDAO authDAO = new AuthDAOMemory();
        UserService userService = new UserService(userDAO, authDAO);

        RegisterRequest reg = new RegisterRequest("TestUsername1", "TestPassword1", "Test@Email1");
        userService.register(reg);

        // submit the same request again
        RegisterRequest sameReg = new RegisterRequest("TestUsername1", "TestPassword1", "Test@Email1");
        Assertions.assertThrows(DataAccessException.class, () -> userService.register(sameReg));

    }

    @Test
    void registerServiceError400() throws DataAccessException {
        // create new databases and initialize UserService
        UserDAO userDAO = new UserDAOMemory();
        AuthDAO authDAO = new AuthDAOMemory();
        UserService userService = new UserService(userDAO, authDAO);

        // submit an unacceptable RegisterRequest object
        RegisterRequest reg = new RegisterRequest("", "TestPassword", "Test@Email");
        Assertions.assertThrows(DataAccessException.class, () -> userService.register(reg));

    }

}
