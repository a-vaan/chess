package dataAccess.DatabaseDAOs;

import dataAccess.DAOInterfaces.UserDAO;
import dataAccess.DataAccessException;
import model.UserData;

public class UserDAODatabase implements UserDAO {
    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) {

    }

    @Override
    public void deleteAllUsers() {

    }
}
