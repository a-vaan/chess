package service;

import dataAccess.DAOInterfaces.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.DAOInterfaces.UserDAO;
import model.AuthData;
import model.UserData;
import model.request.LoginRequest;
import model.request.LogoutRequest;
import model.request.RegisterRequest;
import model.result.LoginResult;
import model.result.RegisterResult;

import java.util.Objects;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

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

    public LoginResult login(LoginRequest req) throws DataAccessException {
        UserData user = userDAO.getUser(req.username());

        if(user == null) {
            throw new DataAccessException("Unauthorized");
        }

        if(!Objects.equals(user.password(), req.password())) {
            throw new DataAccessException("Unauthorized");
        }

        String auth = authDAO.createAuth(req.username());
        return new LoginResult(req.username(), auth);
    }

    public void logout(LogoutRequest req) throws DataAccessException {
        AuthData authData = authDAO.getAuth(req.authToken());

        if(authData == null) {
            throw new DataAccessException("Unauthorized");
        }

        authDAO.deleteAuth(req.authToken());
    }

}
