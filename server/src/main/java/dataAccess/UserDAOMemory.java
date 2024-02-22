package dataAccess;

import model.UserData;

import java.util.HashMap;

public class UserDAOMemory implements UserDAO {

    static private HashMap<Integer, UserData> users = new HashMap<>();

    @Override
    public UserData getUser(String username) {
        return new UserData("","", "");
    }

    @Override
    public void createUser(String username, String password) {

    }
}
