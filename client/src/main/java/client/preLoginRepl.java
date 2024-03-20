package client;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class preLoginRepl {

    private final preLoginClient client;

    public preLoginRepl(String serverUrl) {
        client = new preLoginClient(serverUrl);
    }

    public void run() {
        System.out.println("♕ Welcome to chess. Sign in to start. ♕");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
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
        System.out.print("\n" + SET_TEXT_COLOR_YELLOW + ">>> " + SET_TEXT_COLOR_GREEN);
    }

}
