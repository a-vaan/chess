package server;

import com.google.gson.Gson;
import model.result.CreateGameResult;
import model.result.ListGamesResult;
import model.result.LoginResult;
import model.result.RegisterResult;

import java.io.*;
import java.net.*;
import java.util.Map;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(String username, String password, String email) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + "/user")).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            http.addRequestProperty("Content-Type", "application/json");
            var body = Map.of("username", username,
                    "password", password,
                    "email", email);
            try (var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            }
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, RegisterResult.class);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public LoginResult login(String username, String password) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + "/session")).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            http.addRequestProperty("Content-Type", "application/json");
            var body = Map.of("username", username,
                    "password", password);
            try (var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            }
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, LoginResult.class);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void logout(String authToken) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + "/session")).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("DELETE");
            http.setDoOutput(true);

            http.addRequestProperty("Content-Type", "application/json");
            http.addRequestProperty("authorization", authToken);
            http.connect();
            throwIfNotSuccessful(http);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public CreateGameResult createGame(String gameName, String authToken) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + "/game")).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            http.addRequestProperty("Content-Type", "application/json");
            http.addRequestProperty("authorization", authToken);
            var body = Map.of("gameName", gameName);
            try (var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            }
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, CreateGameResult.class);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + "/game")).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("PUT");
            http.setDoOutput(true);

            http.addRequestProperty("Content-Type", "application/json");
            http.addRequestProperty("authorization", authToken);
            var body = Map.of("playerColor", playerColor,
                    "gameID", gameID);
            try (var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            }
            http.connect();
            throwIfNotSuccessful(http);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public ListGamesResult listGames(String authToken) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + "/game")).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(true);

            http.addRequestProperty("Content-Type", "application/json");
            http.addRequestProperty("authorization", authToken);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, ListGamesResult.class);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void delete() throws ResponseException {
        try {
            URL url = (new URI(serverUrl + "/db")).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("DELETE");
            http.setDoOutput(true);

            http.addRequestProperty("Content-Type", "application/json");
            http.connect();
            throwIfNotSuccessful(http);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

//    public Pet[] listPets() throws server.ResponseException {
//        var path = "/pet";
//        record listPetResponse(Pet[] pet) {
//        }
//        var response = this.makeRequest("GET", path, null, listPetResponse.class);
//        return response.pet();
//    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
