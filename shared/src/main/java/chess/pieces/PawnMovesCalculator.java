package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator implements PieceMovesCalculator {

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
     * adds all the possible promotions to the moveList
     *
     * @param myPosition: the original position of the piece
     * @param currentPosition: the current position of the piece
     * @param moveList: HashSet of all the moves the current piece can make
     */
    private void promotePawn(ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moveList) {
        moveList.add(new ChessMove(myPosition.clone(), currentPosition.clone(), ChessPiece.PieceType.QUEEN));
        moveList.add(new ChessMove(myPosition.clone(), currentPosition.clone(), ChessPiece.PieceType.BISHOP));
        moveList.add(new ChessMove(myPosition.clone(), currentPosition.clone(), ChessPiece.PieceType.ROOK));
        moveList.add(new ChessMove(myPosition.clone(), currentPosition.clone(), ChessPiece.PieceType.KNIGHT));
    }

    /**
     * adds a move to the moveList if the spot is available for the pawn
     *
     * @param board: the chessboard
     * @param myPosition: the original position of the piece
     * @param currentPosition: the current position of the piece
     * @param moveList: HashSet of all the moves the current piece can make
     * @return bool which states if another move is possible
     */
    private boolean canMovePawn(ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moveList) {
        if (board.getPiece(currentPosition) == null) {
            if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE && currentPosition.getRow() == 8) {
                promotePawn(myPosition, currentPosition, moveList);
            } else if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK && currentPosition.getRow() == 1 ){
                promotePawn(myPosition, currentPosition, moveList);
            } else {
                moveList.add(new ChessMove(myPosition.clone(), currentPosition.clone(), null));
            }
            return true;
        }
        return false;
    }

    /**
     * adds moves to the moveList depending on if a black piece is being captured at the boarder of the board or
     * somewhere else
     *
     * @param board: the chessboard
     * @param myPosition: the original position of the piece
     * @param currentPosition: the current position of the piece
     * @param moveList: HashSet of all the moves the current piece can make
     */
    private void whitePawnEnemyCapture(ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moveList) {
        if (notGoOff(currentPosition) && board.getPiece(currentPosition) != null) {
            if (board.getPiece(currentPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (currentPosition.getRow() == 8) {
                    promotePawn(myPosition, currentPosition, moveList);
                } else {
                    moveList.add(new ChessMove(myPosition.clone(), currentPosition.clone(), null));
                }
            }
        }
    }

    /**
     * checks if there are black pieces that can be attacked, and calls whitePawnEnemyCapture if there is
     *
     * @param board: the chessboard
     * @param myPosition: the original position of the piece
     * @param currentPosition: the current position of the piece
     * @param moveList: HashSet of all the moves the current piece can make
     */
    private void whitePawnEnemyCheck(ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moveList) {
        currentPosition.setPosition(currentPosition.getRow() + 1, currentPosition.getColumn() - 1);
        whitePawnEnemyCapture(board, myPosition, currentPosition, moveList);
        currentPosition.setPosition(currentPosition.getRow(), currentPosition.getColumn() + 2);
        whitePawnEnemyCapture(board, myPosition, currentPosition, moveList);
        currentPosition.reset(myPosition);
    }

    /**
     * calls whitePawnEnemyCheck to see if there are enemies, then moves forward 2 spaces if it is the starting turn and 1
     * otherwise
     *
     * @param board: the chessboard
     * @param myPosition: the original position of the piece
     * @param currentPosition: the current position of the piece
     * @param moveList: HashSet of all the moves the current piece can make
     */
    private void whitePawn (ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moveList) {
        whitePawnEnemyCheck(board, myPosition, currentPosition, moveList);
        currentPosition.setPosition(currentPosition.getRow() + 1, currentPosition.getColumn());
        if (myPosition.getRow() == 2) {
            if (canMovePawn(board, myPosition, currentPosition, moveList)) {
                currentPosition.setPosition(currentPosition.getRow() + 1, currentPosition.getColumn());
                canMovePawn(board, myPosition, currentPosition, moveList);
            }
        } else if (notGoOff(currentPosition)) {
            canMovePawn(board, myPosition, currentPosition, moveList);
        }
    }

    /**
     * adds moves to the moveList depending on if a white piece is being captured at the boarder of the board or
     * somewhere else
     *
     * @param board: the chessboard
     * @param myPosition: the original position of the piece
     * @param currentPosition: the current position of the piece
     * @param moveList: HashSet of all the moves the current piece can make
     */
    private void blackPawnEnemyCapture(ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moveList) {
        if (notGoOff(currentPosition) && board.getPiece(currentPosition) != null) {
            if (board.getPiece(currentPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (currentPosition.getRow() == 1) {
                    promotePawn(myPosition, currentPosition, moveList);
                } else {
                    moveList.add(new ChessMove(myPosition.clone(), currentPosition.clone(), null));
                }
            }
        }
    }

    /**
     * checks if there are white pieces that can be attacked, and calls blackPawnEnemyCapture if there is
     *
     * @param board: the chessboard
     * @param myPosition: the original position of the piece
     * @param currentPosition: the current position of the piece
     * @param moveList: HashSet of all the moves the current piece can make
     */
    private void blackPawnEnemyCheck(ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moveList) {
        currentPosition.setPosition(currentPosition.getRow() - 1, currentPosition.getColumn() - 1);
        blackPawnEnemyCapture(board, myPosition, currentPosition, moveList);
        currentPosition.setPosition(currentPosition.getRow(), currentPosition.getColumn() + 2);
        blackPawnEnemyCapture(board, myPosition, currentPosition, moveList);
        currentPosition.reset(myPosition);
    }

    /**
     * calls blackPawnEnemyCheck to see if there are enemies, then moves forward 2 spaces if it is the starting turn and 1
     * otherwise
     *
     * @param board: the chessboard
     * @param myPosition: the original position of the piece
     * @param currentPosition: the current position of the piece
     * @param moveList: HashSet of all the moves the current piece can make
     */
    private void blackPawn (ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moveList) {
        blackPawnEnemyCheck(board, myPosition, currentPosition, moveList);
        currentPosition.setPosition(currentPosition.getRow() - 1, currentPosition.getColumn());
        if (myPosition.getRow() == 7) {
            if (canMovePawn(board, myPosition, currentPosition, moveList)) {
                currentPosition.setPosition(currentPosition.getRow() - 1, currentPosition.getColumn());
                canMovePawn(board, myPosition, currentPosition, moveList);
            }
        } else if (notGoOff(currentPosition)) {
            canMovePawn(board, myPosition, currentPosition, moveList);
        }
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // initialize the HashSet and currentPosition
        HashSet<ChessMove> moveList = new HashSet<>();
        ChessPosition currentPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            whitePawn(board, myPosition, currentPosition, moveList);
        } else {
            blackPawn(board, myPosition, currentPosition, moveList);
        }
        return moveList;
    }
}
