package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard pieces;
    private TeamColor currentTeamTurn;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeamTurn = team;
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "pieces=" + pieces +
                ", currentTeamTurn=" + currentTeamTurn +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(pieces, chessGame.pieces) && currentTeamTurn == chessGame.currentTeamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieces, currentTeamTurn);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (pieces.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.KING &&
                pieces.getPiece(startPosition).getTeamColor() == currentTeamTurn) {
            Collection<ChessMove> allKingMoves = pieces.getPiece(startPosition).pieceMoves(pieces, startPosition);
            Collection<ChessMove> legalKingMoves = new HashSet<>();
            for (ChessMove move: allKingMoves) {
                kingMoveChecker(legalKingMoves, move);
            }
            return legalKingMoves;
        }
        Collection<ChessMove> allMoves = pieces.getPiece(startPosition).pieceMoves(pieces, startPosition);
        Collection<ChessMove> legalMoves = new HashSet<>();
        for (ChessMove move: allMoves) {
            legalMoveChecker(legalMoves, move);
        }
        return legalMoves;

    }

    private void kingMoveChecker(Collection<ChessMove> legalKingMoves, ChessMove move) {
        boolean doesNotMakeCheck = true;
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                if (checkChecker(pieces.getPiece(move.getStartPosition()).getTeamColor(),
                        move.getEndPosition(), row, col)) {
                    doesNotMakeCheck = false;
                }
            }
        }
        if (doesNotMakeCheck) {
            legalKingMoves.add(move);
        }
    }

    private void legalMoveChecker(Collection<ChessMove> legalMoves, ChessMove move) {
        ChessPiece savedPiece = pieces.getPiece(move.getEndPosition());
        pieces.addPiece(move.getEndPosition(), pieces.getPiece(move.getStartPosition()));
        pieces.deletePiece(move.getStartPosition());
        if (!isInCheck(pieces.getPiece(move.getEndPosition()).getTeamColor())) {
            legalMoves.add(move);
        }
        pieces.addPiece(move.getStartPosition(), pieces.getPiece(move.getEndPosition()));
        pieces.deletePiece(move.getEndPosition());
        if (savedPiece != null) {
            pieces.addPiece(move.getEndPosition(), savedPiece);
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(isInCheckmate(TeamColor.WHITE) || isInCheckmate(TeamColor.BLACK)) {
            throw new InvalidMoveException("Move not valid");
        }
        if (pieces.getPiece(move.getStartPosition()).getTeamColor() != currentTeamTurn) {
            throw new InvalidMoveException("Move not valid");
        }
        Collection<ChessMove> validMoveSet = validMoves(move.getStartPosition());
        if(validMoveSet.contains(move)) {
            pieces.addPiece(move.getEndPosition(), pieces.getPiece(move.getStartPosition()));
            pieces.deletePiece(move.getStartPosition());
            if (isInCheck(currentTeamTurn)) {
                if (pieces.getPiece(move.getEndPosition()).getPieceType() != ChessPiece.PieceType.KING) {
                    pieces.addPiece(move.getStartPosition(), pieces.getPiece(move.getEndPosition()));
                    pieces.deletePiece(move.getEndPosition());
                    throw new InvalidMoveException("Move not valid");
                }
            }
            if (pieces.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.PAWN &&
                    currentTeamTurn == TeamColor.WHITE &&
                    move.getEndPosition().getRow() == 8 &&
                    move.getPromotionPiece() != null) {
                pieces.addPiece(move.getEndPosition(), new ChessPiece(currentTeamTurn, move.getPromotionPiece()));
            } else if (pieces.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.PAWN &&
                    currentTeamTurn == TeamColor.BLACK &&
                    move.getEndPosition().getRow() == 1 &&
                    move.getPromotionPiece() != null) {
                pieces.addPiece(move.getEndPosition(), new ChessPiece(currentTeamTurn, move.getPromotionPiece()));
            }
            if (currentTeamTurn == TeamColor.WHITE) {
                setTeamTurn(TeamColor.BLACK);
            } else {
                setTeamTurn(TeamColor.WHITE);
            }
        } else {
            throw new InvalidMoveException("Move not valid");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor);

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                if (checkChecker(teamColor, kingPosition, row, col)) {
                    return true;
                }
            }
        }
        return false;
    }

    private ChessPosition findKing(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPiece currentPiece = pieces.getPiece(new ChessPosition(row, col));
                if (currentPiece == null) {
                    continue;
                }
                if (currentPiece.getPieceType() == ChessPiece.PieceType.KING &&
                        currentPiece.getTeamColor() == teamColor) {
                    return new ChessPosition(row, col);
                }
            }
        }
        return null;
    }

    private boolean checkChecker (TeamColor teamColor, ChessPosition kingPosition, int row, int col) {
        ChessPiece currentPiece = pieces.getPiece(new ChessPosition(row, col));
        if (currentPiece == null) {
            return false;
        }
        if (currentPiece.getTeamColor() != teamColor) {
            Collection<ChessMove> validMoveSet = currentPiece.pieceMoves(pieces, new ChessPosition(row, col));
            for (ChessMove move: validMoveSet) {
                if (move.getEndPosition().equals(kingPosition)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor);

        if (isInCheck(teamColor)) {
            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    if (notCheckmateChecker(teamColor, kingPosition, row, col)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean notCheckmateChecker (TeamColor teamColor, ChessPosition kingPosition, int row, int col) {
        ChessPiece currentPiece = pieces.getPiece(new ChessPosition(row, col));
        if (currentPiece == null) {
            return false;
        }
        if (currentPiece.getTeamColor() == teamColor) {
            Collection<ChessMove> validMoveSet = validMoves(new ChessPosition(row, col));
            for (ChessMove move: validMoveSet) {
                pieces.addPiece(move.getEndPosition(), pieces.getPiece(move.getStartPosition()));
                pieces.deletePiece(move.getStartPosition());
                if (!checkChecker(teamColor, kingPosition, row, col)) {
                    pieces.addPiece(move.getStartPosition(), pieces.getPiece(move.getEndPosition()));
                    pieces.deletePiece(move.getEndPosition());
                    return true;
                }
                pieces.addPiece(move.getStartPosition(), pieces.getPiece(move.getEndPosition()));
                pieces.deletePiece(move.getEndPosition());
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPiece currentPiece = pieces.getPiece(new ChessPosition(row, col));
                if (currentPiece == null) {
                    continue;
                }
                if (currentPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> allValidMoves = validMoves(new ChessPosition(row, col));
                    if (currentPiece.getPieceType() == ChessPiece.PieceType.KING &&
                            !allValidMoves.isEmpty()) {
                        for (ChessMove move: allValidMoves) {
                            pieces.addPiece(move.getEndPosition(), pieces.getPiece(move.getStartPosition()));
                            pieces.deletePiece(move.getStartPosition());
                            if (!isInCheck(teamColor)) {
                                pieces.addPiece(move.getStartPosition(), pieces.getPiece(move.getEndPosition()));
                                pieces.deletePiece(move.getEndPosition());
                                return false;
                            }
                            pieces.addPiece(move.getStartPosition(), pieces.getPiece(move.getEndPosition()));
                            pieces.deletePiece(move.getEndPosition());
                        }
                    }
                    if (!allValidMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        pieces = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return pieces;
    }
}
