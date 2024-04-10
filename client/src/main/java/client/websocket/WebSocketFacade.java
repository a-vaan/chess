package client.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import server.ResponseException;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.Resign;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint implements MessageHandler.Whole<String> {

    Session session;
    GameHandler gameHandler;

    public WebSocketFacade(String url, GameHandler gameHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.gameHandler = gameHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String m) {
                    ServerMessage message = new Gson().fromJson(m, ServerMessage.class);
                    switch (message.getServerMessageType()) {
                        case LOAD_GAME -> {
                            try {
                                gameHandler.updateGame(new Gson().fromJson(m, LoadGame.class));
                            } catch (ResponseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        case NOTIFICATION -> gameHandler.printMessage(new Gson().fromJson(m, Notification.class).getMessage());
                        case ERROR -> gameHandler.printMessage(new Gson().fromJson(m, Error.class).getErrorMessage());
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void joinPlayer(String authToken, Integer gameID, ChessGame.TeamColor playerColor, String playerName) throws ResponseException {
        try {
            var command = new JoinPlayer(authToken, gameID, playerColor, playerName);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void joinObserver(String authToken, Integer gameID) throws ResponseException {
        try {
            var command = new JoinObserver(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void makeMove(String authToken, Integer gameID, ChessMove move) throws ResponseException {
        try {
            var command = new MakeMove(authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void resignGame(String authToken, Integer gameID) throws ResponseException {
        try {
            var command = new Resign(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }



    @Override
    public void onMessage(String s) {
    }
}
