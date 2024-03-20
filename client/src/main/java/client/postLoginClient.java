package client;

import model.GameData;
import model.result.ListGamesResult;
import server.ResponseException;
import server.ServerFacade;

import java.util.Arrays;
import java.util.HashMap;

public class postLoginClient {

    private final ServerFacade server;
    private final String authToken;
    private final HashMap<Integer, GameData> gameList;

    public postLoginClient(String serverUrl, String auth) {
        server = new ServerFacade(serverUrl);
        authToken = auth;
        gameList = new HashMap<>();
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> createGame(params);
                case "list" -> listGames();
//                case "join" -> joinGame(params);
//                case "observe" -> observeGame(params);
//                case "logout" -> logout(params);
                case "logout" -> "logout";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String createGame(String... params) throws ResponseException {
        if (params.length == 1) {
            server.createGame(params[0], authToken);
            return String.format("Chess game %s created.", params[0]);
        }
        throw new ResponseException(400, "Expected: <GAME NAME>");
    }

    public String listGames() throws ResponseException {
        ListGamesResult listedGames = server.listGames(authToken);
        String response = "";
        int i = 0;
        var games = listedGames.games();
        for(GameData game: games) {
            response += i + ": " + game.gameName() + "\n";
            i++;
            gameList.put(i, game);
        }
        return response;
    }

    public void logout() throws ResponseException {
        server.logout(authToken);
    }

    public String help() {
        return """
                    - create <GAME NAME>
                    - list
                    - join <ID> [WHITE | BLACK]
                    - observe <ID>
                    - logout
                    """;
    }
}
