package serviceTests;

import dataAccess.*;
import dataAccess.DAOInterfaces.AuthDAO;
import dataAccess.DAOInterfaces.UserDAO;
import model.request.LoginRequest;
import model.result.LoginResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import service.UserService;

public class LoginServiceTests {

    @Test
    void loginServiceSuccess() throws DataAccessException {
        // create new databases and initialize UserService
        UserDAO userDAO = new UserDAODatabase();
        AuthDAO authDAO = new AuthDAODatabase();
        UserService userService = new UserService(userDAO, authDAO);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode("TestPassword");

        // create the user information
        userDAO.createUser("TestUsername", hashedPassword, "Test@Email");

        // make sure the function does not throw an exception and returns the correct response object.
        LoginRequest reg = new LoginRequest("TestUsername", "TestPassword");
        var res = userService.login(reg);
        Assertions.assertEquals("TestUsername", res.username());
        Assertions.assertEquals(LoginResult.class, res.getClass());
        Assertions.assertNotEquals("", res.authToken());
        Assertions.assertNotEquals(null, res.authToken());

        userDAO.deleteAllUsers();
        authDAO.deleteAllAuths();
    }

    @Test
    void loginServiceErrors() throws DataAccessException {
        // create new databases and initialize UserService
        UserDAO userDAO = new UserDAODatabase();
        AuthDAO authDAO = new AuthDAODatabase();
        UserService userService = new UserService(userDAO, authDAO);

        // create the user information
        userDAO.createUser("TestUsername", "TestPassword", "Test@Email");

        // should throw an error when password is incorrect
        LoginRequest reg = new LoginRequest("TestUsername", "TestWrongPassword");
        Assertions.assertThrows(DataAccessException.class, () -> userService.login(reg));

        // should throw an error when user does not exist
        LoginRequest newReg = new LoginRequest("TestWrongUsername", "TestPassword");
        Assertions.assertThrows(DataAccessException.class, () -> userService.login(newReg));

        userDAO.deleteAllUsers();
        authDAO.deleteAllAuths();
    }
}
