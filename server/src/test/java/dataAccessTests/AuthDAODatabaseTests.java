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
        AuthDAO AuthDAO = new AuthDAODatabase();

        String authToken = AuthDAO.createAuth("TestUsername");
        String authToken1 = AuthDAO.createAuth("TestUsername1");

        AuthData retrievedData = AuthDAO.getAuth(authToken);
        AuthData retrievedData1 = AuthDAO.getAuth(authToken1);


        Assertions.assertEquals(new AuthData(authToken, "TestUsername"), retrievedData);
        Assertions.assertEquals(new AuthData(authToken1, "TestUsername1"), retrievedData1);
    }

}
