package dataAccess;

import model.AuthData;

public interface AuthDAO {

    String createAuth(String username);

    void deleteAllAuths();

}
