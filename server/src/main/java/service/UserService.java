package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.UserData;
import model.request.RegisterRequest;
import model.result.RegisterResult;

public class UserService {

    private UserDAO userDAO;
    private AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest reg) throws DataAccessException {
        UserData user = userDAO.getUser(reg.username());

        if(user != null) {
            throw new DataAccessException("User already exists");
        }

        userDAO.createUser(reg.username(), reg.password(), reg.email());
        String auth = authDAO.createAuth(reg.username());
        return new RegisterResult(reg.username(), auth, "");
    }

}
