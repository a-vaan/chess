package webSocketMessages.serverMessages;

public class Notification extends ServerMessage {

    private final String message;
    public Notification(String m) {
        super(ServerMessageType.NOTIFICATION);
        message = m;
    }

    public String getMessage() {
        return message;
    }
}
