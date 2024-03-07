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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    /**
     * Registers the user and saves their data to the database.
     *
     * @param reg: RegisterRequest containing the username, password, and email of the specified user
     * return: RegisterResult object containing the username and authToken
     * @throws DataAccessException: to be thrown if the user already exists or submits a bad request
     */
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

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(reg.password());

        userDAO.createUser(reg.username(), hashedPassword, reg.email());
        String auth = authDAO.createAuth(reg.username());
        return new RegisterResult(reg.username(), auth);
    }

    /**
     * Logs in the user by checking that the submitted password matches the password in the database.
     *
     * @param req: LoginRequest object containing the username and password of the user
     * return: LoginResult object containing the username and authToken
     * @throws DataAccessException: to be thrown if the user is not authorized
     */
    public LoginResult login(LoginRequest req) throws DataAccessException {
        UserData user = userDAO.getUser(req.username());

        if(user == null) {
            throw new DataAccessException("Unauthorized");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if(!encoder.matches(req.password(), user.password())) {
            throw new DataAccessException("Unauthorized");
        }

        String auth = authDAO.createAuth(req.username());
        return new LoginResult(req.username(), auth);
    }

    /**
     * Logs out the user and deletes their authToken
     *
     * @param req: LogoutRequest object containing the authToken of the user
     * @throws DataAccessException: to be thrown if the user is not authorized
     */
    public void logout(LogoutRequest req) throws DataAccessException {
        AuthData authData = authDAO.getAuth(req.authToken());

        if(authData == null) {
            throw new DataAccessException("Unauthorized");
        }

        authDAO.deleteAuth(req.authToken());
    }

}
