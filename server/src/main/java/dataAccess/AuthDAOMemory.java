package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class AuthDAOMemory implements AuthDAO {

    static private final HashMap<String, AuthData> auths = new HashMap<>();

    @Override
    public String createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, username);
        auths.put(authToken, auth);
        return authToken;
    }

    @Override
    public void deleteAllAuths() {
        auths.clear();
    }

}
