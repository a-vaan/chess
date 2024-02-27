package chess.pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator extends PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessPosition currentPosition = myPosition.makeNew();
        currentPosition.setRow(currentPosition.getRow() + 1);
        currentPosition.setColumn(currentPosition.getColumn() - 2);
        kingLoop(board, myPosition, currentPosition, moves);
        currentPosition = myPosition.makeNew();
        currentPosition.setRow(currentPosition.getRow());
        currentPosition.setColumn(currentPosition.getColumn() - 2);
        kingLoop(board, myPosition, currentPosition, moves);
        currentPosition = myPosition.makeNew();
        currentPosition.setRow(currentPosition.getRow() - 1);
        currentPosition.setColumn(currentPosition.getColumn() - 2);
        kingLoop(board, myPosition, currentPosition, moves);
        return moves;
    }

    private void kingLoop (ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moves) {
        for(int i = 0; i < 3; i++) {
            currentPosition.setColumn(currentPosition.getColumn() + 1);
            if(currentPosition.isSafe()) {
                canMove(board, myPosition, currentPosition, moves);
            }
        }
    }

}
