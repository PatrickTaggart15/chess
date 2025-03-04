package chess;

import chess.calculators.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor team;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.team = pieceColor;
        this.type = type;
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
        return team;
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
        return switch (type) {
            case KING -> KingCalculator.getMoves(board, myPosition);
            case QUEEN -> QueenCalculator.getMoves(board, myPosition);
            case BISHOP -> BishopCalculator.getMoves(board, myPosition);
            case KNIGHT -> KnightCalculator.getMoves(board, myPosition);
            case ROOK -> RookCalculator.getMoves(board, myPosition);
            case PAWN -> PawnCalculator.getMoves(board, myPosition);
        };
    }

    @Override
    public String toString() {
        return switch (type) {
            case KING -> team == ChessGame.TeamColor.WHITE ? "K" : "k";
            case QUEEN -> team == ChessGame.TeamColor.WHITE ? "Q" : "q";
            case BISHOP -> team == ChessGame.TeamColor.WHITE ? "B" : "b";
            case KNIGHT -> team == ChessGame.TeamColor.WHITE ? "N" : "n";
            case ROOK -> team == ChessGame.TeamColor.WHITE ? "R" : "r";
            case PAWN -> team == ChessGame.TeamColor.WHITE ? "P" : "p";
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return team == that.team && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, type);
    }
}