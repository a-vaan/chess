package chess;

import chess.pieces.*;

import java.util.ArrayList;
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
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (type == PieceType.BISHOP) {
        BishopMovesCalculator moves = new BishopMovesCalculator();
        return moves.pieceMoves(board, myPosition);
        }
        if (type == PieceType.KING) {
            KingMovesCalculator moves = new KingMovesCalculator();
            return moves.pieceMoves(board, myPosition);
        }
        if (type == PieceType.KNIGHT) {
            KnightMovesCalculator moves = new KnightMovesCalculator();
            return moves.pieceMoves(board, myPosition);
        }
        if (type == PieceType.PAWN) {
            PawnMovesCalculator moves = new PawnMovesCalculator();
            return moves.pieceMoves(board, myPosition);
        }
        if (type == PieceType.ROOK) {
            RookMovesCalculator moves = new RookMovesCalculator();
            return moves.pieceMoves(board, myPosition);
        }
        if (type == PieceType.QUEEN) {
            QueenMovesCalculator moves = new QueenMovesCalculator();
            return moves.pieceMoves(board, myPosition);
        }
        return new ArrayList<>();
    }
}
