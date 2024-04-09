package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.Map;

public class WebSocketSessions {

    private static final HashMap<String, HashMap<String, Session>> sessionMap = new HashMap<>();

    public void addSessionToGame(String gameID, String authToken, Session session) {
        HashMap<String, Session> authMap = new HashMap<>();
        authMap.put(authToken, session);
        sessionMap.put(gameID, authMap);
    }

    public void removeSessionFromGame(String gameID, String authToken, Session session) {
        HashMap<String, Session> authMap = sessionMap.get(gameID);
        authMap.remove(authToken, session);
    }

    public void removeSession(Session session) {
        for(Map.Entry<String, HashMap<String, Session>> sessionMapBreak : sessionMap.entrySet()) {
            for(Map.Entry<String, Session> authMapBreak : sessionMapBreak.getValue().entrySet()) {
                if(authMapBreak.getValue() == session) {
                    HashMap<String, Session> authMap = sessionMap.get(sessionMapBreak.getKey());
                    authMap.remove(authMapBreak.getKey(), authMapBreak.getValue());
                }
            }
        }
    }

    public HashMap<String, Session> getSessionsForGame(String gameID) {
        return sessionMap.get(gameID);
    }

}
