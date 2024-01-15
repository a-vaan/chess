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

    private boolean movePiece(ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moveList) {
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

    private HashSet<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moveList = new HashSet<>();
        ChessPosition currentPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while ((currentPosition.getRow() + 1) <= 8 && (currentPosition.getColumn() + 1) <= 8) {
            currentPosition.setPosition((currentPosition.getRow() + 1), (currentPosition.getColumn() + 1));
            if (movePiece(board, myPosition, currentPosition, moveList)) {
                continue;
            }
            break;
        }
        currentPosition.reset(myPosition);
        while ((currentPosition.getRow() - 1) >= 1 && (currentPosition.getColumn() + 1) <= 8) {
            currentPosition.setPosition((currentPosition.getRow() - 1), (currentPosition.getColumn() + 1));
            if (movePiece(board, myPosition, currentPosition, moveList)) {
                continue;
            }
            break;
        }
        currentPosition.reset(myPosition);
        while ((currentPosition.getRow() - 1) >= 1 && (currentPosition.getColumn() - 1) >= 1) {
            currentPosition.setPosition((currentPosition.getRow() - 1), (currentPosition.getColumn() - 1));
            if (movePiece(board, myPosition, currentPosition, moveList)) {
                continue;
            }
            break;
        }
        currentPosition.reset(myPosition);
        while ((currentPosition.getRow() + 1) <= 8 && (currentPosition.getColumn() - 1) >= 1) {
            currentPosition.setPosition((currentPosition.getRow() + 1), (currentPosition.getColumn() - 1));
            if (movePiece(board, myPosition, currentPosition, moveList)) {
                continue;
            }
            break;
        }
        return moveList;
    }

    private boolean notGoOff (ChessPosition currentPosition) {
        if (currentPosition.getColumn() < 1 || currentPosition.getColumn() > 8 || currentPosition.getRow() < 1 ||
                currentPosition.getRow() > 8) {
            return false;
        }
        return true;
    }

    private void kingLoop (ChessBoard board, ChessPosition myPosition, ChessPosition currentPosition, HashSet<ChessMove> moveList) {
        for (int i = 0; i < 3; i++) {
            currentPosition.setPosition((currentPosition.getRow()), (currentPosition.getColumn() + 1));
            if (notGoOff(currentPosition)) {
                movePiece(board, myPosition, currentPosition, moveList);
            }
        }
    }

    private HashSet<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moveList = new HashSet<>();
        ChessPosition currentPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2);
        kingLoop(board, myPosition, currentPosition, moveList);
        currentPosition.reset(myPosition);
        currentPosition.setPosition(currentPosition.getRow(), currentPosition.getColumn() - 2);
        kingLoop(board, myPosition, currentPosition, moveList);
        currentPosition.reset(myPosition);
        currentPosition.setPosition(currentPosition.getRow() - 1, currentPosition.getColumn() - 2);
        kingLoop(board, myPosition, currentPosition, moveList);
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
        return new ArrayList<>();
    }
}
