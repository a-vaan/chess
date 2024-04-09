package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand {

    private final Integer gameID;

    public JoinObserver(String authToken, Integer gameIdentifier) {
        super(authToken);
        gameID = gameIdentifier;
    }

    public Integer getGameID() {return gameID;}
}
