package chess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

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
            if (board.getPiece(currentPosition).pieceColor != this.pieceColor) {
                moveList.add(new ChessMove(myPosition.clone(), currentPosition.clone(), null));
            }
            return false;
        }
    }

    /**
     * Calculates the moves the bishop can make
     *
     * @param board: the chessboard
     * @param myPosition: the current position of the bishop
     * @return HashSet of all the different ChessMoves available to the bishop
     */
    private HashSet<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moveList = new HashSet<>();
        ChessPosition currentPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
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
        return moveList;
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
     * a loop used to check each space around the king to see what spots can be moved to
     *
     * @param board: the chessboard
     * @param myPosition: the original position of the piece
     * @param currentPosition: the current position of the piece
     * @param moveList: HashSet of all the moves the current piece can make
     */
    private void kingLoop (ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moveList) {
        for (int i = 0; i < 3; i++) {
            currentPosition.setPosition((currentPosition.getRow()), (currentPosition.getColumn() + 1));
            if (notGoOff(currentPosition)) {
                canMovePiece(board, myPosition, currentPosition, moveList);
            }
        }
        currentPosition.reset(myPosition);
    }

    /**
     * Calculates the moves the king can make
     *
     * @param board: the chessboard
     * @param myPosition: the current position of the bishop
     * @return HashSet of all the different ChessMoves available to the bishop
     */
    private HashSet<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        // initialize the HashSet and currentPosition
        HashSet<ChessMove> moveList = new HashSet<>();
        ChessPosition currentPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2);
        // check the spots above the king
        kingLoop(board, myPosition, currentPosition, moveList);
        // check the spots to the left and right of the king
        currentPosition.setPosition(currentPosition.getRow(), currentPosition.getColumn() - 2);
        kingLoop(board, myPosition, currentPosition, moveList);
        // check the spots below the king
        currentPosition.setPosition(currentPosition.getRow() - 1, currentPosition.getColumn() - 2);
        kingLoop(board, myPosition, currentPosition, moveList);
        return moveList;
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
    private HashSet<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
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

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (type == PieceType.BISHOP) {
            return bishopMoves(board, myPosition);
        }
        if (type == PieceType.KING) {
            return kingMoves(board, myPosition);
        }
        if (type == PieceType.KNIGHT) {
            return knightMoves(board, myPosition);
        }
        return new ArrayList<>();
    }
}
