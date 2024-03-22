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
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
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
        gameList.clear();
        ListGamesResult listedGames = server.listGames(authToken);
        String response = "";
        int i = 0;
        var games = listedGames.games();
        for(GameData game: games) {
            response += i + ": " + game.gameName() + "\n";
            gameList.put(i, game);
            i++;
        }
        return response;
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length == 2) {
            GameData game = gameList.get(Integer.parseInt(params[0]));
            try {
                server.joinGame(authToken, params[1], game.gameID());
            } catch (NullPointerException e) {
                return "Game does not exist. Please try again.\n";
            }
            GameplayClient gameClient = new GameplayClient(game);
            System.out.println(gameClient.displayWhiteGame());
            System.out.println(gameClient.displayBlackGame());
            return String.format("Chess game %s exited.\n", params[0]);
        }
        throw new ResponseException(400, "Expected: <ID> [WHITE | BLACK]");
    }

    public String observeGame(String... params) throws ResponseException {
        if (params.length == 1) {
            GameData game = gameList.get(Integer.parseInt(params[0]));
            try {
                server.joinGame(authToken, "null", game.gameID());
            } catch (NullPointerException e) {
                return "Game does not exist. Please try again.\n";
            }
            GameplayClient gameClient = new GameplayClient(game);
            System.out.println(gameClient.displayWhiteGame());
            System.out.println(gameClient.displayBlackGame());
            return String.format("Chess game %s is done being observed.\n", params[0]);
        }
        throw new ResponseException(400, "Expected: <ID> [WHITE | BLACK]");
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
