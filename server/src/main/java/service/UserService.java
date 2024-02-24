package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.UserData;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.LoginResult;
import model.result.RegisterResult;

import java.util.Objects;

public class UserService {

    private UserDAO userDAO;
    private AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest reg) throws DataAccessException {
        if(reg.username() == null || reg.username().isEmpty() ||
                reg.password() == null || reg.password().isEmpty() ||
                reg.email() == null || reg.email().isEmpty()) {
            throw new DataAccessException("Bad request");
        }

        UserData user = userDAO.getUser(reg.username());

        if(user != null) {
            throw new DataAccessException("User already exists");
        }

        userDAO.createUser(reg.username(), reg.password(), reg.email());
        String auth = authDAO.createAuth(reg.username());
        return new RegisterResult(reg.username(), auth);
    }

    public LoginResult login(LoginRequest reg) throws DataAccessException {
        UserData user = userDAO.getUser(reg.username());

        if(user == null) {
            throw new DataAccessException("Unauthorized");
        }

        if(!Objects.equals(user.password(), reg.password())) {
            throw new DataAccessException("Unauthorized");
        }

        String auth = authDAO.createAuth(reg.username());
        return new LoginResult(reg.username(), auth);
    }

}
