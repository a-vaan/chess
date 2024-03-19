package clientTests;

import model.result.CreateGameResult;
import model.result.ListGamesResult;
import model.result.LoginResult;
import model.result.RegisterResult;
import org.junit.jupiter.api.*;
import server.ResponseException;
import server.Server;
import server.ServerFacade;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void registerSuccess() throws Exception {
        var authData = facade.register("playerRegister", "passwordRegister", "pR@email.com");
        Assertions.assertTrue(authData.authToken().length() > 10);
        facade.delete();
    }

    @Test
    void loginSuccess() throws Exception {
        facade.register("playerLogin", "passwordLogin", "pL@email.com");
        var loginData = facade.login("playerLogin", "passwordLogin");
        Assertions.assertTrue(loginData.authToken().length() > 10);
        facade.delete();
    }

    @Test
    void logoutSuccess() throws Exception {
        RegisterResult registerData = facade.register("playerLogout", "passwordLogout", "pLO@email.com");
        facade.logout(registerData.authToken());
        LoginResult loginData = facade.login("playerLogout", "passwordLogout");
        Assertions.assertNotEquals(registerData.authToken(), loginData.authToken());
        facade.delete();
    }

    @Test
    void createGameSuccess() throws Exception {
        RegisterResult registerData = facade.register("playerCreateGame", "passwordCreateGame", "pCG@email.com");
        CreateGameResult createGameData = facade.createGame("createGame", registerData.authToken());
        Assertions.assertTrue(createGameData.gameID() > 0);
        facade.delete();
    }

    @Test
    void joinGameSuccess() throws Exception {
        RegisterResult registerData = facade.register("playerJoinGame", "passwordJoinGame", "pJG@email.com");
        CreateGameResult createGameData = facade.createGame("joinGame", registerData.authToken());
        facade.joinGame(registerData.authToken(), "WHITE", createGameData.gameID());
        RegisterResult registerData1 = facade.register("playerJoinGame1", "passwordJoinGame1", "pJG1@email.com");
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(registerData1.authToken(), "WHITE", createGameData.gameID()));
        facade.delete();
    }

    @Test
    void listGamesSuccess() throws Exception {
        RegisterResult registerData = facade.register("playerListGames", "passwordListGames", "pLGs@email.com");
        facade.createGame("listGames0", registerData.authToken());
        facade.createGame("listGames1", registerData.authToken());
        facade.createGame("listGames2", registerData.authToken());
        ListGamesResult listGamesData = facade.listGames(registerData.authToken());
        Assertions.assertEquals(3, listGamesData.games().size());
        facade.delete();
    }

    @Test
    void deleteSuccess() throws Exception {
        facade.register("playerDelete1", "passwordDelete1", "pD1@email.com");
        facade.delete();
        Assertions.assertThrows(ResponseException.class, () -> facade.login("playerDelete1", "passwordDelete1"));
    }

}
