package chess.pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator extends PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessPosition currentPosition = myPosition.makeNew();
        currentPosition.setRow(currentPosition.getRow() + 2);
        moveHorizontal(board, myPosition, currentPosition, moves);
        currentPosition = myPosition.makeNew();
        currentPosition.setRow(currentPosition.getRow() - 2);
        moveHorizontal(board, myPosition, currentPosition, moves);
        currentPosition = myPosition.makeNew();
        currentPosition.setColumn(currentPosition.getColumn() + 2);
        moveVertical(board, myPosition, currentPosition, moves);
        currentPosition = myPosition.makeNew();
        currentPosition.setColumn(currentPosition.getColumn() - 2);
        moveVertical(board, myPosition, currentPosition, moves);
        return moves;
    }

    private void moveHorizontal (ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moves) {
        currentPosition.setColumn(currentPosition.getColumn() - 1);
        if (currentPosition.isSafe()) {
            canMove(board, myPosition, currentPosition, moves);
        }
        currentPosition.setColumn(currentPosition.getColumn() + 2);
        if (currentPosition.isSafe()) {
            canMove(board, myPosition, currentPosition, moves);
        }
    }

    private void moveVertical (ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moves) {
        currentPosition.setRow(currentPosition.getRow() - 1);
        if (currentPosition.isSafe()) {
            canMove(board, myPosition, currentPosition, moves);
        }
        currentPosition.setRow(currentPosition.getRow() + 2);
        if (currentPosition.isSafe()) {
            canMove(board, myPosition, currentPosition, moves);
        }
    }

}
