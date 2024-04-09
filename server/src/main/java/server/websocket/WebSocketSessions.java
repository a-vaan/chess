package server.websocket;

import spark.Session;

import java.util.HashMap;

public class WebSocketSessions {

    private static HashMap<String, HashMap<String, Session>> sessionMap = new HashMap<>();

    public void addSessionToGame(String gameID, String authToken, Session session) {
        HashMap<String, Session> authMap = new HashMap<>().put(authToken, session);
        sessionMap.put(gameID, authMap);
    }

    public void removeSessionFromGame(String gameID, String authToken, Session session) {
        HashMap<String, Session> authMap = sessionMap.get(gameID);
    }

    public void removeSession(Session session) {

    }

    public HashMap<String, Session> getSessionsForGame(String gameID) {
        return new HashMap<>();
    }

}
