package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import service.GameService;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@WebSocket
public class WebSocketHandler {

    private final WebSocketSessions sessions;
    private final GameService gameService;

    public WebSocketHandler(WebSocketSessions s, GameService g) {
        sessions = s;
        gameService = g;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(new Gson().fromJson(message, JoinPlayer.class), session);
            case JOIN_OBSERVER -> joinObserver(new Gson().fromJson(message, JoinObserver.class), session);
            case MAKE_MOVE -> makeMove(new Gson().fromJson(message, MakeMove.class), session);
            case RESIGN -> resignGame(new Gson().fromJson(message, Resign.class), session);
        }
    }

    private void joinPlayer(JoinPlayer playerData, Session session) throws IOException, DataAccessException {
        String response = gameService.joinPlayer(playerData);
        if (response.contains("Error")) {
            sendMessage(new Error(response), session);
            return;
        }
        sessions.addSessionToGame(playerData.getGameID(), playerData.getAuthString(), session);
        sendMessage(new LoadGame(playerData.getGameID()), session);
        String color;
        if(playerData.getPlayerColor() == ChessGame.TeamColor.WHITE) {
            color = "white";
        } else {
            color = "black";
        }
        var message = String.format("%s is joining as %s", playerData.getPlayerName(),color);
        var notification = new Notification(message);
        broadcastMessage(playerData.getGameID(), notification, playerData.getAuthString());
    }

    private void joinObserver(JoinObserver playerData, Session session) throws IOException, DataAccessException {
        String response = gameService.joinObserver(playerData);
        if (response.contains("Error")) {
            sendMessage(new Error(response), session);
            return;
        }
        sessions.addSessionToGame(playerData.getGameID(), playerData.getAuthString(), session);
        sendMessage(new LoadGame(playerData.getGameID()), session);
        var message = String.format("%s joined as an observer", response);
        var notification = new Notification(message);
        broadcastMessage(playerData.getGameID(), notification, playerData.getAuthString());
    }

    private void makeMove(MakeMove moveData, Session session) throws IOException, DataAccessException{
        if(sessions.checkIfResigned(moveData.getGameID())) {
            sendMessage(new Error("A player has already resigned"), session);
            return;
        }
        String response = gameService.makeMove(moveData);
        if (response.contains("Error")) {
            sendMessage(new Error(response), session);
            return;
        }
        sendMessage(new LoadGame(moveData.getGameID()), session);
        broadcastMessage(moveData.getGameID(), new LoadGame(moveData.getGameID()), moveData.getAuthString());
        String[] responseArray = response.split(",");
        if(Objects.equals(responseArray[1], "checkmate")) {
            broadcastMessage(moveData.getGameID(), new Notification(String.format("%s is in checkmate! Good game!", responseArray[2])), "null");
        } else if(Objects.equals(responseArray[1], "check")) {
            broadcastMessage(moveData.getGameID(), new Notification(String.format("%s is in check!", responseArray[2])), "null");
        }
        var message = String.format("%s moved the piece at %s to %s", response,
                moveData.getMove().getStartPosition().toString(), moveData.getMove().getEndPosition().toString());
        var notification = new Notification(message);
        broadcastMessage(moveData.getGameID(), notification, moveData.getAuthString());
    }

    private void resignGame(Resign resignData, Session session) throws DataAccessException, IOException {
        String response = gameService.resign(resignData);
        if(response.contains("Error")) {
            sendMessage(new Error(response), session);
            return;
        }
        if(sessions.checkIfResigned(resignData.getGameID())) {
            sendMessage(new Error("Another player has already resigned"), session);
            return;
        }
        sessions.addGameToResigned(resignData.getGameID());
        var message = String.format("%s joined as an observer", response);
        var notification = new Notification(message);
        broadcastMessage(resignData.getGameID(), notification, null);
    }

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
