package chess.pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovesCalculator implements PieceMovesCalculator {

    /**
     * adds a move to the moveList if the spot is available
     *
     * @param board: the chessboard
     * @param myPosition: the original position of the piece
     * @param currentPosition: the current position of the piece
     * @param moveList: HashSet of all the moves the current piece can make
     * @return bool which states if another move is possible
     */
    private boolean canMovePiece(ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moveList) {
        if (board.getPiece(currentPosition) == null) {
            moveList.add(new ChessMove(myPosition.clone(), currentPosition.clone(), null));
            return true;
        } else {
            if (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                moveList.add(new ChessMove(myPosition.clone(), currentPosition.clone(), null));
            }
            return false;
        }
    }

    /**
     * checks all moves that can be made vertically and horizontally from the current piece
     *
     * @param board: the chessboard
     * @param myPosition: the original position of the piece
     * @param currentPosition: the current position of the piece
     * @param moveList: HashSet of all the moves the current piece can make
     */
    private void diagonal(ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moveList) {
        currentPosition.reset(myPosition);
        while ((currentPosition.getRow() + 1) <= 8 && (currentPosition.getColumn() + 1) <= 8) {
            currentPosition.setPosition((currentPosition.getRow() + 1), (currentPosition.getColumn() + 1));
            if (canMovePiece(board, myPosition, currentPosition, moveList)) {
                continue;
            }
            break;
        }
        currentPosition.reset(myPosition);
        while ((currentPosition.getRow() - 1) >= 1 && (currentPosition.getColumn() + 1) <= 8) {
            currentPosition.setPosition((currentPosition.getRow() - 1), (currentPosition.getColumn() + 1));
            if (canMovePiece(board, myPosition, currentPosition, moveList)) {
                continue;
            }
            break;
        }
        currentPosition.reset(myPosition);
        while ((currentPosition.getRow() - 1) >= 1 && (currentPosition.getColumn() - 1) >= 1) {
            currentPosition.setPosition((currentPosition.getRow() - 1), (currentPosition.getColumn() - 1));
            if (canMovePiece(board, myPosition, currentPosition, moveList)) {
                continue;
            }
            break;
        }
        currentPosition.reset(myPosition);
        while ((currentPosition.getRow() + 1) <= 8 && (currentPosition.getColumn() - 1) >= 1) {
            currentPosition.setPosition((currentPosition.getRow() + 1), (currentPosition.getColumn() - 1));
            if (canMovePiece(board, myPosition, currentPosition, moveList)) {
                continue;
            }
            break;
        }
    }

    /**
     * checks all moves that can be made vertically and horizontally from the current piece
     *
     * @param board: the chessboard
     * @param myPosition: the original position of the piece
     * @param currentPosition: the current position of the piece
     * @param moveList: HashSet of all the moves the current piece can make
     */
    private void horizontalAndVertical(ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moveList) {
        currentPosition.reset(myPosition);
        while ((currentPosition.getRow() + 1) <= 8) {
            currentPosition.setPosition((currentPosition.getRow() + 1), currentPosition.getColumn());
            if (canMovePiece(board, myPosition, currentPosition, moveList)) {
                continue;
            }
            break;
        }
        currentPosition.reset(myPosition);
        while ((currentPosition.getRow() - 1) >= 1) {
            currentPosition.setPosition((currentPosition.getRow() - 1), currentPosition.getColumn());
            if (canMovePiece(board, myPosition, currentPosition, moveList)) {
                continue;
            }
            break;
        }
        currentPosition.reset(myPosition);
        while ((currentPosition.getColumn() + 1) <= 8) {
            currentPosition.setPosition(currentPosition.getRow(), (currentPosition.getColumn() + 1));
            if (canMovePiece(board, myPosition, currentPosition, moveList)) {
                continue;
            }
            break;
        }
        currentPosition.reset(myPosition);
        while ((currentPosition.getColumn() - 1) >= 1) {
            currentPosition.setPosition(currentPosition.getRow(), (currentPosition.getColumn() - 1));
            if (canMovePiece(board, myPosition, currentPosition, moveList)) {
                continue;
            }
            break;
        }
    }

    /**
     * Calculates the moves the queen can make
     *
     * @param board: the chessboard
     * @param myPosition: the current position of the bishop
     * @return HashSet of all the different ChessMoves available to the bishop
     */
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moveList = new HashSet<>();
        ChessPosition currentPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        diagonal(board, myPosition, currentPosition, moveList);
        horizontalAndVertical(board, myPosition, currentPosition, moveList);
        return moveList;
    }
}
