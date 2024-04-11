package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSessions {

    static private final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Session>> sessionMap = new ConcurrentHashMap<>();
    static private final ArrayList<Integer> resignedGames = new ArrayList<>();

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

    public void addGameToResigned(Integer gameID) {
        resignedGames.add(gameID);
    }

    public void removeSessionFromGame(Integer gameID, String authToken, Session session) {
        ConcurrentHashMap<String, Session> authMap = sessionMap.get(gameID);
        authMap.remove(authToken, session);
    }

    public boolean checkIfResigned(Integer gameID) {
        return resignedGames.contains(gameID);
    }

    public ConcurrentHashMap<String, Session> getSessionsForGame(Integer gameID) {
        return sessionMap.get(gameID);
    }

    public void deleteAllResignedIDs () {
        resignedGames.clear();
    }

}
