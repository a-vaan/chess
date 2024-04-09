package webSocketMessages.serverMessages;

public class Notification extends ServerMessage {

    private final String message;
    public Notification(ServerMessageType type, String m) {
        super(type);
        message = m;
    }

    public String getMessage() {
        return message;
    }
}
