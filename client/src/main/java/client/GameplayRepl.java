package client;

import model.GameData;
import server.ResponseException;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class GameplayRepl {

    private final GameplayClient client;

    public GameplayRepl(String serverURL, String auth, GameData gameToDisplay, String user) throws ResponseException {
        client = new GameplayClient(serverURL, auth, gameToDisplay, user);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("leave")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_YELLOW + "[GAMEPLAY]>>> " + SET_TEXT_COLOR_GREEN);
    }
}
