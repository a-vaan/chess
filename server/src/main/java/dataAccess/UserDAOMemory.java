package dataAccess;

import model.UserData;

import java.util.HashMap;

public class UserDAOMemory implements UserDAO {

    static private final HashMap<String, UserData> users = new HashMap<>();

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public void createUser(String username, String password, String email) {
        UserData user = new UserData(username, password, email);
        users.put(username, user);
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
    }
}
