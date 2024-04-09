package webSocketMessages.serverMessages;

public class LoadGame extends ServerMessage {

    private final Integer game;

    public LoadGame(ServerMessageType type, Integer gameID) {
        super(type);
        game = gameID;
    }

    public Integer getGame() {
        return game;
    }
}
