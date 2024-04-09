package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {

    private final Integer gameID;
    private final ChessMove move;

    public MakeMove(String authToken, Integer gameIdentifier, ChessMove chessMove) {
        super(authToken);
        gameID = gameIdentifier;
        move = chessMove;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }
}
