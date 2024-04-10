package client;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class postLoginRepl {

    private final postLoginClient client;

    public postLoginRepl(String serverUrl, String authToken, String user) {
        client = new postLoginClient(serverUrl, authToken, user);
    }

    public void run(String username) {
        System.out.println("Logged in as " + username + ".");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var postResult = "";
        while (!postResult.equals("logout")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                postResult = client.eval(line);
                System.out.print(SET_TEXT_COLOR_YELLOW + postResult);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        try {
            System.out.println();
            client.logout();
        } catch (Throwable e) {
            var msg = e.toString();
            System.out.print(msg);
        }

    }

    private void printPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_GREEN + "[LOGGED IN]>>> " + SET_TEXT_COLOR_BLUE);
    }
}
