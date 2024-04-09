package webSocketMessages.userCommands;

public class Leave extends UserGameCommand {

    private final Integer gameID;

    public Leave(String authToken, Integer gameIdentifier) {
        super(authToken);
        gameID = gameIdentifier;
    }

    public Integer getGameID() {
        return gameID;
    }
}
