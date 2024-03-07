package dataAccess.DAOInterfaces;

import dataAccess.DataAccessException;
import model.AuthData;

public interface AuthDAO {

    /**
     * Creates an authToken string and saves it to the database along with the user's username.
     *
     * @param username: desired username
     * return: the authToken in the form of a string
     */
    String createAuth(String username) throws DataAccessException;

    /**
     * Gets the authToken and associated username from the database.
     *
     * @param authToken: desired username
     * return: an AuthData object containing the authToken and username.
     */
    AuthData getAuth(String authToken) throws DataAccessException;

    /**
     * Deletes the specified authToken and username from the database.
     *
     * @param authToken: the authToken to be deleted
     */
    void deleteAuth(String authToken);

    /**
     * Deletes all authTokens and associated usernames in the database.
     */
    void deleteAllAuths();

}
