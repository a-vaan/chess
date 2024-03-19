package clientTests;

import org.junit.jupiter.api.*;
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
    }

    @Test
    void loginSuccess() throws Exception {
        facade.register("playerLogin", "passwordLogin", "pL@email.com");
        var loginData = facade.login("playerLogin", "passwordLogin");
        Assertions.assertTrue(loginData.authToken().length() > 10);
    }

}
