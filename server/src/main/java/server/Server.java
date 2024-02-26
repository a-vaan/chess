package server;

import com.google.gson.Gson;
import dataAccess.*;
import dataAccess.DAOInterfaces.AuthDAO;
import dataAccess.DAOInterfaces.GameDAO;
import dataAccess.DAOInterfaces.UserDAO;
import dataAccess.MemoryDAOs.AuthDAOMemory;
import dataAccess.MemoryDAOs.GameDAOMemory;
import dataAccess.MemoryDAOs.UserDAOMemory;
import model.request.*;
import model.result.ErrorMessage;
import service.DeleteService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.Objects;

public class Server {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public int run(int desiredPort) {
        userDAO = new UserDAOMemory();
        authDAO = new AuthDAOMemory();
        gameDAO = new GameDAOMemory();

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.post("/user", this::registerHandler);
        Spark.post("/session", this::loginHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.post("/game", this::createGameHandler);
        Spark.put("/game", this::joinGameHandler);
        Spark.get("/game", this::listGamesHandler);
        Spark.delete("/db", this::deleteHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    /**
     * Server handler for register requests.
     * Calls the register service method of UserService.
     *
     * @param request: request Json object
     * @param response: response object
     * return: response Json object
     */
    private Object registerHandler(Request request, Response response) {
        // create a UserService object
        UserService userService = new UserService(userDAO, authDAO);

        // try to register the user, and if it fails catch the error and set to the correct HTTP code.
        try {
            var req = new Gson().fromJson(request.body(), RegisterRequest.class);
            var res = userService.register(req);
            return new Gson().toJson(res);
        } catch(DataAccessException e) {
            if(Objects.equals(e.getMessage(), "User already exists")) {
                response.status(403);
                return new Gson().toJson(new ErrorMessage("Error: already taken"));
            }
            if(Objects.equals(e.getMessage(), "Bad request")) {
                response.status(400);
                return new Gson().toJson(new ErrorMessage("Error: bad request"));
            }
            response.status(500);
            return new Gson().toJson(new ErrorMessage("Error: DataAccessException thrown but not caught correctly"));
        } catch(Exception e) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        }
    }

    /**
     * Login handler for login requests.
     * Calls the login service method of UserService.
     *
     * @param request: request Json object
     * @param response: response object
     * return: response Json object
     */
    private Object loginHandler(Request request, Response response) {
        // create a UserService object
        UserService userService = new UserService(userDAO, authDAO);

        // try to log in the user, and if it fails catch the error and set to the correct HTTP code.
        try {
            var req = new Gson().fromJson(request.body(), LoginRequest.class);
            var res = userService.login(req);
            return new Gson().toJson(res);
        } catch(DataAccessException e) {
            response.status(401);
            return new Gson().toJson(new ErrorMessage("Error: unauthorized"));
        } catch(Exception e) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        }
    }

    /**
     * Logout handler for logout requests.
     * Calls the logout service method of UserService.
     *
     * @param request: request Json object
     * @param response: response object
     * return: response Json object
     */
    private Object logoutHandler(Request request, Response response) {
        // create a UserService object
        UserService userService = new UserService(userDAO, authDAO);

        // try to log out the user, and if it fails catch the error and set to the correct HTTP code.
        try {
            LogoutRequest req = new LogoutRequest(request.headers("authorization"));
            userService.logout(req);
            return "";
        } catch(DataAccessException e) {
            response.status(401);
            return new Gson().toJson(new ErrorMessage("Error: unauthorized"));
        } catch(Exception e) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        }
    }

    /**
     * Create game handler for createGame requests.
     * Calls the createGame service method of GameService.
     *
     * @param request: request Json object
     * @param response: response object
     * return: response Json object
     */
    private Object createGameHandler(Request request, Response response) {
        // create a GameService object
        GameService gameService = new GameService(gameDAO, authDAO);

        // try to create a game, and if it fails catch the error and set to the correct HTTP code.
        try {
            String authToken = request.headers("authorization");
            var req = new Gson().fromJson(request.body(), CreateGameRequest.class);
            var res = gameService.createGame(req, authToken);
            return new Gson().toJson(res);
        } catch(DataAccessException e) {
            if(Objects.equals(e.getMessage(), "Bad request")) {
                response.status(400);
                return new Gson().toJson(new ErrorMessage("Error: bad request"));
            }
            if(Objects.equals(e.getMessage(), "Unauthorized")) {
                response.status(401);
                return new Gson().toJson(new ErrorMessage("Error: unauthorized"));
            }
            response.status(500);
            return new Gson().toJson(new ErrorMessage("Error: DataAccessException thrown but not caught correctly"));
        } catch(Exception e) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        }
    }

    /**
     * Join game handler for joinGame requests.
     * Calls the joinGame service method of GameService.
     *
     * @param request: request Json object
     * @param response: response object
     * return: response Json object
     */
    private Object joinGameHandler(Request request, Response response) {
        // create a GameService object
        GameService gameService = new GameService(gameDAO, authDAO);

        // try to join a game, and if it fails catch the error and set to the correct HTTP code.
        try {
            String authToken = request.headers("authorization");
            var req = new Gson().fromJson(request.body(), JoinGameRequest.class);
            gameService.joinGame(req, authToken);
            return "";
        } catch(DataAccessException e) {
            if(Objects.equals(e.getMessage(), "Unauthorized")) {
                response.status(401);
                return new Gson().toJson(new ErrorMessage("Error: unauthorized"));
            }
            if(Objects.equals(e.getMessage(), "Bad request")) {
                response.status(400);
                return new Gson().toJson(new ErrorMessage("Error: bad request"));
            }
            if(Objects.equals(e.getMessage(), "Already taken")) {
                response.status(403);
                return new Gson().toJson(new ErrorMessage("Error: already taken"));
            }
            response.status(500);
            return new Gson().toJson(new ErrorMessage("Error: DataAccessException thrown but not caught correctly"));
        } catch(Exception e) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        }
    }

    /**
     * List games handler for listGames requests.
     * Calls the listGames service method of GameService.
     *
     * @param request: request Json object
     * @param response: response object
     * return: response Json object
     */
    private Object listGamesHandler(Request request, Response response) {
        // create a GameService object
        GameService gameService = new GameService(gameDAO, authDAO);

        // try to list all games in the database, and if it fails catch the error and set to the correct HTTP code.
        try {
            ListGamesRequest req = new ListGamesRequest(request.headers("authorization"));
            var res = gameService.listGames(req);
            return new Gson().toJson(res);
        } catch(DataAccessException e) {
            response.status(401);
            return new Gson().toJson(new ErrorMessage("Error: unauthorized"));
        } catch(Exception e) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        }
    }

    /**
     * Delete handler for deleteAll requests.
     * Calls the delete service method of deleteService.
     *
     * @param request: request Json object
     * @param response: response object
     * return: response Json object
     */
    private Object deleteHandler(Request request, Response response) {
        // create a DeleteService object
        DeleteService deleteService = new DeleteService(userDAO, authDAO, gameDAO);

        // try to delete all the items in all the databases, and if it fails catch the error and set to the correct HTTP code.
        try {
            deleteService.deleteAll();
        } catch (Exception e) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(e.toString()));
        }
        return "";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
