package dataAccess.DAOInterfaces;

import dataAccess.DataAccessException;
import model.UserData;

public interface UserDAO {

    /**
     * Retrieves the UserData object from the database specified by the username passed in.
     *
     * @param username: the username of the desired UserData object
     * return: the Userdata object associated with the username
     * @throws DataAccessException: if it is not possible to get this user, throw this error
     */
    UserData getUser(String username) throws DataAccessException;

    /**
     * Creates a user and save their data to the database
     *
     * @param username: desired username
     * @param password: desired password
     * @param email: desired email
     */
    void createUser(String username, String password, String email);

    /**
     * Deletes all users in the database
     */
    void deleteAllUsers();

}
