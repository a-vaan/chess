package dataAccess;

import model.AuthData;

public interface AuthDAO {

    String createAuth(String username);

    AuthData getAuth(String authToken);

    void deleteAuth(String authToken);

    void deleteAllAuths();

}
