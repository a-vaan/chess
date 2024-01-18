package chess.pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator implements PieceMovesCalculator {

    /**
     * adds a move to the moveList if the spot is available
     *
     * @param board: the chessboard
     * @param myPosition: the original position of the piece
     * @param currentPosition: the current position of the piece
     * @param moveList: HashSet of all the moves the current piece can make
     */
    private void canMovePiece(ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moveList) {
        if (board.getPiece(currentPosition) == null) {
            moveList.add(new ChessMove(myPosition.clone(), currentPosition.clone(), null));
        } else {
            if (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                moveList.add(new ChessMove(myPosition.clone(), currentPosition.clone(), null));
            }
        }
    }

    /**
     * Calculates if the current space is on the chessboard
     *
     * @param currentPosition: the current position of the piece
     * @return bool of if the piece is on the board or not
     */
    private boolean notGoOff (ChessPosition currentPosition) {
        return currentPosition.getColumn() >= 1 && currentPosition.getColumn() <= 8 && currentPosition.getRow() >= 1 &&
                currentPosition.getRow() <= 8;
    }

    /**
     * Checks the spots to the left and right of the currentPosition
     * and adds them to the moveList if the knight is able to move there
     *
     * @param board: the chessboard
     * @param myPosition: the original position of the piece
     * @param currentPosition: the current position of the piece
     * @param moveList: HashSet of all the moves the current piece can make
     */
    private void knightLoopSide(ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moveList) {
        currentPosition.setPosition((currentPosition.getRow()), (currentPosition.getColumn() - 1));
        if (notGoOff(currentPosition)) {
            canMovePiece(board, myPosition, currentPosition, moveList);
        }
        currentPosition.setPosition((currentPosition.getRow()), (currentPosition.getColumn() + 2));
        if (notGoOff(currentPosition)) {
            canMovePiece(board, myPosition, currentPosition, moveList);
        }
        currentPosition.reset(myPosition);
    }

    /**
     * Checks the spots above and below the currentPosition
     * and adds them to the moveList if the knight is able to move there
     *
     * @param board: the chessboard
     * @param myPosition: the original position of the piece
     * @param currentPosition: the current position of the piece
     * @param moveList: HashSet of all the moves the current piece can make
     */
    private void knightLoopUp(ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moveList) {
        currentPosition.setPosition((currentPosition.getRow() - 1), (currentPosition.getColumn()));
        if (notGoOff(currentPosition)) {
            canMovePiece(board, myPosition, currentPosition, moveList);
        }
        currentPosition.setPosition((currentPosition.getRow() + 2), (currentPosition.getColumn()));
        if (notGoOff(currentPosition)) {
            canMovePiece(board, myPosition, currentPosition, moveList);
        }
        currentPosition.reset(myPosition);
    }

    /**
     * Calculates the moves the knight can make
     *
     * @param board: the chessboard
     * @param myPosition: the current position of the bishop
     * @return HashSet of all the different ChessMoves available to the bishop
     */
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // initialize the HashSet and currentPosition
        HashSet<ChessMove> moveList = new HashSet<>();
        ChessPosition currentPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
        // check the spaces above the knight
        knightLoopSide(board, myPosition, currentPosition, moveList);
        // check the spaces below the knight
        currentPosition.setPosition(currentPosition.getRow() - 2, currentPosition.getColumn());
        knightLoopSide(board, myPosition, currentPosition, moveList);
        // check the spaces to the left of the knight
        currentPosition.setPosition(currentPosition.getRow(), currentPosition.getColumn() - 2);
        knightLoopUp(board, myPosition, currentPosition, moveList);
        // check the spaces to the right of the knight
        currentPosition.setPosition(currentPosition.getRow(), currentPosition.getColumn() + 2);
        knightLoopUp(board, myPosition, currentPosition, moveList);
        return moveList;
    }
}
