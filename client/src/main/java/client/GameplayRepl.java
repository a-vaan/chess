package client;

import model.GameData;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class GameplayRepl {

    private final GameData game;
    private final GameplayClient client;

    public GameplayRepl(GameData gameToDisplay) {
        game = gameToDisplay;
        client = new GameplayClient(game);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                // result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_YELLOW + "[LOGGED OUT]>>> " + SET_TEXT_COLOR_GREEN);
    }
}
