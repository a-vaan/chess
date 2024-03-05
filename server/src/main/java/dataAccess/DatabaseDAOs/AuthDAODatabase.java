package dataAccess.DatabaseDAOs;

import dataAccess.DAOInterfaces.AuthDAO;
import model.AuthData;

public class AuthDAODatabase implements AuthDAO {
    @Override
    public String createAuth(String username) {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public void deleteAllAuths() {

    }
}
