package client.websocket;

import server.ResponseException;
import webSocketMessages.serverMessages.LoadGame;

public interface GameHandler {

    void updateGame(LoadGame loadGame) throws ResponseException;

    void printMessage(String message);
}
