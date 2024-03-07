package dataAccessTests;

import dataAccess.AuthDAODatabase;
import dataAccess.DAOInterfaces.AuthDAO;
import dataAccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AuthDAODatabaseTests {

    @Test
    void createAuthSuccess() throws DataAccessException {
        AuthDAO createAuthDAO = new AuthDAODatabase();

        String authToken = createAuthDAO.createAuth("TestUsername");
        String authToken1 = createAuthDAO.createAuth("TestUsername1");

        AuthData retrievedData = createAuthDAO.getAuth(authToken);
        AuthData retrievedData1 = createAuthDAO.getAuth(authToken1);


        Assertions.assertEquals(new AuthData(authToken, "TestUsername"), retrievedData);
        Assertions.assertEquals(new AuthData(authToken1, "TestUsername1"), retrievedData1);
    }

    @Test
    void createAuthFail() throws DataAccessException {
        AuthDAO createAuthDAO = new AuthDAODatabase();

        createAuthDAO.createAuth("TestUsername");

        Assertions.assertThrows(DataAccessException.class, () -> createAuthDAO.createAuth("TestUsername"));
    }

    @Test
    void getAuthSuccess() throws DataAccessException {
        AuthDAO getAuthDAO = new AuthDAODatabase();

        String authToken = getAuthDAO.createAuth("TestUsername");
        String authToken1 = getAuthDAO.createAuth("TestUsername1");

        AuthData retrievedData = getAuthDAO.getAuth(authToken);
        AuthData retrievedData1 = getAuthDAO.getAuth(authToken1);


        Assertions.assertEquals(new AuthData(authToken, "TestUsername"), retrievedData);
        Assertions.assertEquals(new AuthData(authToken1, "TestUsername1"), retrievedData1);
    }

    @Test
    void deleteAuthSuccess() throws DataAccessException {
        AuthDAO deleteAuthDAO = new AuthDAODatabase();

        String authToken = deleteAuthDAO.createAuth("TestUsername");
        String authToken1 = deleteAuthDAO.createAuth("TestUsername1");

        AuthData retrievedData = deleteAuthDAO.getAuth(authToken);
        AuthData retrievedData1 = deleteAuthDAO.getAuth(authToken1);


        Assertions.assertEquals(new AuthData(authToken, "TestUsername"), retrievedData);
        Assertions.assertEquals(new AuthData(authToken1, "TestUsername1"), retrievedData1);

        deleteAuthDAO.deleteAuth(authToken);

        Assertions.assertNull(deleteAuthDAO.getAuth(authToken));
        Assertions.assertEquals(new AuthData(authToken1, "TestUsername1"), retrievedData1);
    }

    @Test
    void deleteAllAuthSuccess() throws DataAccessException {
        AuthDAO deleteAllAuthDAO = new AuthDAODatabase();

        String authToken = deleteAllAuthDAO.createAuth("TestUsername");
        String authToken1 = deleteAllAuthDAO.createAuth("TestUsername1");

        AuthData retrievedData = deleteAllAuthDAO.getAuth(authToken);
        AuthData retrievedData1 = deleteAllAuthDAO.getAuth(authToken1);


        Assertions.assertEquals(new AuthData(authToken, "TestUsername"), retrievedData);
        Assertions.assertEquals(new AuthData(authToken1, "TestUsername1"), retrievedData1);

        deleteAllAuthDAO.deleteAllAuths();

        Assertions.assertNull(deleteAllAuthDAO.getAuth(authToken));
        Assertions.assertNull(deleteAllAuthDAO.getAuth(authToken1));
    }

}
