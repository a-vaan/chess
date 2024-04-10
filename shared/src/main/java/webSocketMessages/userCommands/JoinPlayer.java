package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{

    private final Integer gameID;
    private final ChessGame.TeamColor playerColor;
    private final String playerName;

    public JoinPlayer(String authToken, Integer gameIdentifier, ChessGame.TeamColor currentPlayerColor, String pN) {
        super(authToken);
        gameID = gameIdentifier;
        playerColor = currentPlayerColor;
        playerName = pN;
        this.commandType = CommandType.JOIN_PLAYER;
    }

    public Integer getGameID() {return gameID;}

    public ChessGame.TeamColor getPlayerColor() {return playerColor;}

    public String getPlayerName() {
        return playerName;
    }
}
