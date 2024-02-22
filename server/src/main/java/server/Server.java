package server;

import com.google.gson.Gson;
import model.request.RegisterRequest;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.init();
        Spark.post("/user", this::registerHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object registerHandler(Request request, Response response) {
        var reg = new Gson().fromJson(request.body(), RegisterRequest.class);
        var res = service.UserService.register(reg);
        return new Gson().toJson(res);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
