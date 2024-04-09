package webSocketMessages.serverMessages;

public class Error extends ServerMessage {

    private final String errorMessage;

    public Error(ServerMessageType type, String error) {
        super(type);
        errorMessage = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
