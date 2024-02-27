package chess.pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public abstract class PieceMovesCalculator {

    public abstract Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition myPosition);

    protected boolean canMove (ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moves) {
        if (board.getPiece(currentPosition) == null) {
            moves.add(new ChessMove(myPosition.makeNew(), currentPosition.makeNew(), null));
            return true;
        } else if (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
            moves.add(new ChessMove(myPosition.makeNew(), currentPosition.makeNew(), null));
        }
        return false;
    }

    protected void diagonal (ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> moves) {
        ChessPosition currentPosition = myPosition.makeNew();
        currentPosition.setRow(currentPosition.getRow() + 1);
        currentPosition.setColumn(currentPosition.getColumn() + 1);
        while(currentPosition.isSafe()) {
            if (!canMove(board, myPosition, currentPosition, moves)) {
                break;
            }
            currentPosition.setRow(currentPosition.getRow() + 1);
            currentPosition.setColumn(currentPosition.getColumn() + 1);
        }
        currentPosition = myPosition.makeNew();
        currentPosition.setRow(currentPosition.getRow() + 1);
        currentPosition.setColumn(currentPosition.getColumn() - 1);
        while(currentPosition.isSafe()) {
            if (!canMove(board, myPosition, currentPosition, moves)) {
                break;
            }
            currentPosition.setRow(currentPosition.getRow() + 1);
            currentPosition.setColumn(currentPosition.getColumn() - 1);
        }
        currentPosition = myPosition.makeNew();
        currentPosition.setRow(currentPosition.getRow() - 1);
        currentPosition.setColumn(currentPosition.getColumn() + 1);
        while(currentPosition.isSafe()) {
            if (!canMove(board, myPosition, currentPosition, moves)) {
                break;
            }
            currentPosition.setRow(currentPosition.getRow() - 1);
            currentPosition.setColumn(currentPosition.getColumn() + 1);
        }
        currentPosition = myPosition.makeNew();
        currentPosition.setRow(currentPosition.getRow() - 1);
        currentPosition.setColumn(currentPosition.getColumn() - 1);
        while(currentPosition.isSafe()) {
            if (!canMove(board, myPosition, currentPosition, moves)) {
                break;
            }
            currentPosition.setRow(currentPosition.getRow() - 1);
            currentPosition.setColumn(currentPosition.getColumn() - 1);
        }
    }

    protected void horizontalAndVertical (ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> moves) {
        ChessPosition currentPosition = myPosition.makeNew();
        currentPosition.setRow(currentPosition.getRow() + 1);
        while(currentPosition.isSafe()) {
            if (!canMove(board, myPosition, currentPosition, moves)) {
                break;
            }
            currentPosition.setRow(currentPosition.getRow() + 1);
        }
        currentPosition = myPosition.makeNew();
        currentPosition.setRow(currentPosition.getRow() - 1);
        while(currentPosition.isSafe()) {
            if (!canMove(board, myPosition, currentPosition, moves)) {
                break;
            }
            currentPosition.setRow(currentPosition.getRow() - 1);
        }
        currentPosition = myPosition.makeNew();
        currentPosition.setColumn(currentPosition.getColumn() + 1);
        while(currentPosition.isSafe()) {
            if (!canMove(board, myPosition, currentPosition, moves)) {
                break;
            }
            currentPosition.setColumn(currentPosition.getColumn() + 1);
        }
        currentPosition = myPosition.makeNew();
        currentPosition.setColumn(currentPosition.getColumn() - 1);
        while(currentPosition.isSafe()) {
            if (!canMove(board, myPosition, currentPosition, moves)) {
                break;
            }
            currentPosition.setColumn(currentPosition.getColumn() - 1);
        }
    }

}

