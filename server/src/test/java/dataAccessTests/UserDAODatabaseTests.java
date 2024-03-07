package dataAccessTests;

import dataAccess.DAOInterfaces.UserDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAODatabase;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserDAODatabaseTests {

    @Test
    void createUserSuccess() throws DataAccessException {
        UserDAO createUserDAO = new UserDAODatabase();

        createUserDAO.createUser("TestUsernameCreate", "TestPasswordCreate", "TestEmailCreate");
        createUserDAO.createUser("TestUsernameCreate1", "TestPasswordCreate1", "TestEmailCreate1");

        UserData retrievedData = createUserDAO.getUser("TestUsernameCreate");
        UserData retrievedData1 = createUserDAO.getUser("TestUsernameCreate1");


        Assertions.assertEquals(new UserData("TestUsernameCreate", "TestPasswordCreate", "TestEmailCreate"), retrievedData);
        Assertions.assertEquals(new UserData("TestUsernameCreate1", "TestPasswordCreate1", "TestEmailCreate1"), retrievedData1);

        createUserDAO.deleteAllUsers();
    }

    @Test
    void createUserFail() throws DataAccessException {
        UserDAO createUserDAO = new UserDAODatabase();

        Assertions.assertThrows(DataAccessException.class, () -> createUserDAO.createUser(null, null, null));
    }

    @Test
    void getUserSuccess() throws DataAccessException {
        UserDAO getUserDAO = new UserDAODatabase();

        getUserDAO.createUser("TestUsernameGet", "TestPasswordGet", "TestEmailGet");
        getUserDAO.createUser("TestUsernameGet1", "TestPasswordGet1", "TestEmailGet1");

        UserData retrievedData = getUserDAO.getUser("TestUsernameGet");
        UserData retrievedData1 = getUserDAO.getUser("TestUsernameGet1");


        Assertions.assertEquals(new UserData("TestUsernameGet", "TestPasswordGet", "TestEmailGet"), retrievedData);
        Assertions.assertEquals(new UserData("TestUsernameGet1", "TestPasswordGet1", "TestEmailGet1"), retrievedData1);

        getUserDAO.deleteAllUsers();
    }

    @Test
    void getUserFail() throws DataAccessException {
        UserDAO getUserDAO = new UserDAODatabase();

        Assertions.assertNull(getUserDAO.getUser("test"));
    }

    @Test
    void deleteAllUsersSuccess() throws DataAccessException {
        UserDAO deleteAllUsersDAO = new UserDAODatabase();

        deleteAllUsersDAO.createUser("TestUsernameDelete", "TestPasswordDelete", "TestEmailDelete");
        deleteAllUsersDAO.createUser("TestUsernameDelete1", "TestPasswordDelete1", "TestEmailDelete1");

        UserData retrievedData = deleteAllUsersDAO.getUser("TestUsernameDelete");
        UserData retrievedData1 = deleteAllUsersDAO.getUser("TestUsernameDelete1");


        Assertions.assertEquals(new UserData("TestUsernameDelete", "TestPasswordDelete", "TestEmailDelete"), retrievedData);
        Assertions.assertEquals(new UserData("TestUsernameDelete1", "TestPasswordDelete1", "TestEmailDelete1"), retrievedData1);

        deleteAllUsersDAO.deleteAllUsers();

        Assertions.assertNull(deleteAllUsersDAO.getUser("TestUsernameDelete"));
        Assertions.assertNull(deleteAllUsersDAO.getUser("TestUsernameDelete1"));
    }

}
