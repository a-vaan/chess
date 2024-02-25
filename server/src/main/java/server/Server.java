package server;

import com.google.gson.Gson;
import dataAccess.*;
import model.request.CreateGameRequest;
import model.request.LoginRequest;
import model.request.LogoutRequest;
import model.request.RegisterRequest;
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
        Spark.delete("/db", this::deleteHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object registerHandler(Request request, Response response) {
        UserService userService = new UserService(userDAO, authDAO);
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

    private Object loginHandler(Request request, Response response) {
        UserService userService = new UserService(userDAO, authDAO);
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

    private Object logoutHandler(Request request, Response response) {
        UserService userService = new UserService(userDAO, authDAO);
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

    private Object createGameHandler(Request request, Response response) {
        GameService gameService = new GameService(gameDAO, authDAO);

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

    private Object deleteHandler(Request request, Response response) {
        DeleteService deleteService = new DeleteService(userDAO, authDAO, gameDAO);
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
