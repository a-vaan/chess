package server.websocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSessions {

    static private final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Session>> sessionMap = new ConcurrentHashMap<>();
    static private final ConcurrentHashMap<Integer, ConcurrentHashMap<ChessGame.TeamColor, String>> gameColorAuths = new ConcurrentHashMap<>();

    public void addSessionToGame(Integer gameID, String authToken, Session session) {
        ConcurrentHashMap<String, Session> existingAuthMap = sessionMap.get(gameID);
        if(existingAuthMap != null) {
            existingAuthMap.put(authToken, session);
        } else {
            ConcurrentHashMap<String, Session> authMap = new ConcurrentHashMap<>();
            authMap.put(authToken, session);
            sessionMap.put(gameID, authMap);
        }
    }

    public void addPlayerColorToGame(Integer gameID, String authToken, ChessGame.TeamColor color) {
        ConcurrentHashMap<ChessGame.TeamColor, String> existingColorMap = gameColorAuths.get(gameID);
        if(existingColorMap != null) {
            existingColorMap.put(color, authToken);
        } else {
            ConcurrentHashMap<ChessGame.TeamColor, String> authMap = new ConcurrentHashMap<>();
            authMap.put(color, authToken);
            gameColorAuths.put(gameID, authMap);
        }
    }

    public void removeSessionFromGame(Integer gameID, String authToken, Session session) {
        ConcurrentHashMap<String, Session> authMap = sessionMap.get(gameID);
        authMap.remove(authToken, session);
    }

    public void removeSession(Session session) {
        for(Map.Entry<Integer, ConcurrentHashMap<String, Session>> sessionMapBreak : sessionMap.entrySet()) {
            for(Map.Entry<String, Session> authMapBreak : sessionMapBreak.getValue().entrySet()) {
                if(authMapBreak.getValue() == session) {
                    ConcurrentHashMap<String, Session> authMap = sessionMap.get(sessionMapBreak.getKey());
                    authMap.remove(authMapBreak.getKey(), authMapBreak.getValue());
                }
            }
        }
    }

    public ConcurrentHashMap<String, Session> getSessionsForGame(Integer gameID) {
        return sessionMap.get(gameID);
    }

    public ConcurrentHashMap<ChessGame.TeamColor, String> getPlayerColorsForGame(Integer gameID) {
        return gameColorAuths.get(gameID);
    }

}