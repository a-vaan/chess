package client;

import model.result.LoginResult;
import model.result.RegisterResult;
import server.ResponseException;
import server.ServerFacade;

import java.util.Arrays;

public class PreLoginClient {

    private final ServerFacade server;
    private final String serverURL;

    public PreLoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        serverURL = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            RegisterResult registerData = server.register(params[0], params[1], params[2]);
            new PostLoginRepl(serverURL, registerData.authToken(), params[0]).run(params[0]);
            return "You have been logged out.";
        }
        throw new ResponseException("Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            LoginResult loginData = server.login(params[0], params[1]);
            try {
                new PostLoginRepl(serverURL, loginData.authToken(), params[0]).run(params[0]);
            } catch (Exception e) {
                return "Player does not exist. Please try again.\n";
            }
            return "You have been logged out.\n";
        }
        throw new ResponseException("Expected: <USERNAME> <PASSWORD>");
    }

    public String help() {
        return """
                    - register <USERNAME> <PASSWORD> <EMAIL>
                    - login <USERNAME> <PASSWORD>
                    - quit
                    """;
    }
}
