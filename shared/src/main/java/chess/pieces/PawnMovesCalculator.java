package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator extends PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessPosition currentPosition = myPosition.makeNew();
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            whitePawnMoves(board, myPosition, currentPosition, moves);
        } else {
            blackPawnMoves(board, myPosition, currentPosition, moves);
        }
        return moves;
    }

    private void whitePawnMoves(ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moves) {
        if (myPosition.getRow() == 2) {
            currentPosition.setRow(currentPosition.getRow() + 1);
            if (board.getPiece(currentPosition) == null) {
                currentPosition.setRow(currentPosition.getRow() + 1);
                if (board.getPiece(currentPosition) == null) {
                    moves.add(new ChessMove(myPosition.makeNew(), currentPosition.makeNew(), null));
                }
            }
            currentPosition = myPosition.makeNew();
        }
        whiteCheckEnemies(board, myPosition, currentPosition, moves);
        currentPosition = myPosition.makeNew();
        currentPosition.setRow(currentPosition.getRow() + 1);
        if (currentPosition.isSafe()) {
            if (currentPosition.getRow() == 8) {
                promote(myPosition, currentPosition, moves);
            } else {
                canMove(board, myPosition, currentPosition, moves);
            }
        }
    }

    private void whiteCheckEnemies(ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moves) {
        currentPosition.setRow(currentPosition.getRow() + 1);
        currentPosition.setColumn(currentPosition.getColumn() - 1);
        whiteCheckEnemiesDuplicate(board, myPosition, currentPosition, moves);
        currentPosition.setColumn(currentPosition.getColumn() + 2);
        whiteCheckEnemiesDuplicate(board, myPosition, currentPosition, moves);
    }

    private void whiteCheckEnemiesDuplicate(ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moves) {
        if (currentPosition.isSafe() && board.getPiece(currentPosition) != null &&
                board.getPiece(currentPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
            if (currentPosition.getRow() == 8) {
                promote (myPosition, currentPosition, moves);
            } else {
                moves.add(new ChessMove(myPosition.makeNew(), currentPosition.makeNew(), null));
            }
        }
    }

    private void blackPawnMoves(ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moves) {
        if (myPosition.getRow() == 7) {
            currentPosition.setRow(currentPosition.getRow() - 1);
            if (board.getPiece(currentPosition) == null) {
                currentPosition.setRow(currentPosition.getRow() - 1);
                if (board.getPiece(currentPosition) == null) {
                    moves.add(new ChessMove(myPosition.makeNew(), currentPosition.makeNew(), null));
                }
            }
            currentPosition = myPosition.makeNew();
        }
        blackCheckEnemies(board, myPosition, currentPosition, moves);
        currentPosition = myPosition.makeNew();
        currentPosition.setRow(currentPosition.getRow() - 1);
        if (currentPosition.isSafe()) {
            if (currentPosition.getRow() == 1) {
                promote(myPosition, currentPosition, moves);
            } else {
                canMove(board, myPosition, currentPosition, moves);
            }
        }
    }

    private void blackCheckEnemies(ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moves) {
        currentPosition.setRow(currentPosition.getRow() - 1);
        currentPosition.setColumn(currentPosition.getColumn() - 1);
        blackCheckEnemiesDuplicate(board, myPosition, currentPosition, moves);
        currentPosition.setColumn(currentPosition.getColumn() + 2);
        blackCheckEnemiesDuplicate(board, myPosition, currentPosition, moves);
    }

    private void blackCheckEnemiesDuplicate(ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moves) {
        if (currentPosition.isSafe() && board.getPiece(currentPosition) != null &&
                board.getPiece(currentPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (currentPosition.getRow() == 1) {
                promote (myPosition, currentPosition, moves);
            } else {
                moves.add(new ChessMove(myPosition.makeNew(), currentPosition.makeNew(), null));
            }
        }
    }

    private void promote (ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moves) {
        moves.add(new ChessMove(myPosition.makeNew(), currentPosition.makeNew(), ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(myPosition.makeNew(), currentPosition.makeNew(), ChessPiece.PieceType.KNIGHT));
        moves.add(new ChessMove(myPosition.makeNew(), currentPosition.makeNew(), ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(myPosition.makeNew(), currentPosition.makeNew(), ChessPiece.PieceType.QUEEN));
    }

    @Override
    protected boolean canMove(ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moves) {
        if (board.getPiece(currentPosition) == null) {
            moves.add(new ChessMove(myPosition.makeNew(), currentPosition.makeNew(), null));
            return true;
        }
        return false;
    }
}
