package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{

    private final Integer gameID;
    private final ChessGame.TeamColor playerColor;

    public JoinPlayer(String authToken, Integer gameIdentifier, ChessGame.TeamColor currentPlayerColor) {
        super(authToken);
        gameID = gameIdentifier;
        playerColor = currentPlayerColor;
        this.commandType = CommandType.JOIN_PLAYER;
    }

    public Integer getGameID() {return gameID;}

    public ChessGame.TeamColor getPlayerColor() {return playerColor;}

}
