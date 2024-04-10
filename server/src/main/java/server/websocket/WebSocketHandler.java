package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@WebSocket
public class WebSocketHandler {

    private final WebSocketSessions sessions;

    public WebSocketHandler(WebSocketSessions s) {
        sessions = s;
    }

//    @OnWebSocketConnect
//    public void onConnect(Session session) {}
//
//    @OnWebSocketClose
//    public void onClose(Session session) {
//        sessions.removeSession(session);
//    }
//
//    @OnWebSocketError
//    public void onError(Throwable e) {}

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(new Gson().fromJson(message, JoinPlayer.class), session);
//            case JOIN_OBSERVER -> joinObserver(new Gson().fromJson(message, JoinObserver.class), session);
        }
    }

    private void joinPlayer(JoinPlayer playerData, Session session) throws IOException {
        sessions.addSessionToGame(playerData.getGameID(), playerData.getAuthString(), session);
        String color;
        if(playerData.getPlayerColor() == ChessGame.TeamColor.WHITE) {
            color = "white";
        } else {
            color = "black";
        }
        sendMessage(new LoadGame(playerData.getGameID()), session);
        var message = String.format("%s is joining as", color);
        var notification = new Notification(message);
        broadcastMessage(playerData.getGameID(), notification, playerData.getAuthString());
    }

//    private void joinObserver(JoinObserver playerData) throws IOException {
//        connections.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new Notification(Notification.Type.DEPARTURE, message);
//        connections.broadcast(visitorName, notification);
//    }

    private void sendMessage(ServerMessage message, Session session) throws IOException {
        session.getRemote().sendString(new Gson().toJson(message));
    }

    private void broadcastMessage(Integer gameID, ServerMessage message, String exceptThisAuthToken) throws IOException {
        ConcurrentHashMap<String, Session> gameSessions = sessions.getSessionsForGame(gameID);
        for(Map.Entry<String, Session> authMapBreak : gameSessions.entrySet()) {
            if(!(Objects.equals(authMapBreak.getKey(), exceptThisAuthToken))) {
                authMapBreak.getValue().getRemote().sendString(new Gson().toJson(message));
            }
        }
    }
}
