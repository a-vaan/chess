package server;

import com.google.gson.Gson;
import dataAccess.*;
import model.request.RegisterRequest;
import model.result.ErrorMessage;
import service.DeleteService;
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

        Spark.init();
        Spark.post("/user", this::registerHandler);
        Spark.delete("/db", this::deleteHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object registerHandler(Request request, Response response) {
        UserService userService = new UserService(userDAO, authDAO);
        try {
            var reg = new Gson().fromJson(request.body(), RegisterRequest.class);
            var res = userService.register(reg);
            return new Gson().toJson(res);
        } catch(DataAccessException exception) {
            if(Objects.equals(exception.toString(), "User already exists")) {
                response.status(403);
                return new Gson().toJson(new ErrorMessage("Error: already taken"));
            }
            response.status(500);
            return new Gson().toJson(new ErrorMessage(exception.toString()));
        } catch(Exception e) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(e.toString()));
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
