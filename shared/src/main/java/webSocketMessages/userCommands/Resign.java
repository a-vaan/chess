package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {

    private final Integer gameID;

    public Resign(String authToken, Integer gameIdentifier) {
        super(authToken);
        gameID = gameIdentifier;
        this.commandType = CommandType.RESIGN;
    }

    public Integer getGameID() {
        return gameID;
    }
}
