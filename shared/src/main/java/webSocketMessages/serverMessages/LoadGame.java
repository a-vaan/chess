package webSocketMessages.serverMessages;

public class LoadGame extends ServerMessage {

    private final Integer game;

    public LoadGame(Integer gameID) {
        super(ServerMessageType.LOAD_GAME);
        game = gameID;
    }

    public Integer getGame() {
        return game;
    }
}
