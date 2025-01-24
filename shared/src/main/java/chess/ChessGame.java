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

    private TeamColor teamTurn;
    private ChessBoard board;

    private boolean gameOver;

    public ChessGame() {
        board = new ChessBoard();
        setTeamTurn(TeamColor.WHITE);
    }


    public TeamColor getTeamTurn() {
        return teamTurn;
    }


    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }


    public enum TeamColor {
        WHITE,
        BLACK;
    }


    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece currPiece = board.getPiece(startPosition);
        if (currPiece == null) {
            return null;
        }
        HashSet<ChessMove> possibleMoves = (HashSet<ChessMove>) board.getPiece(startPosition).pieceMoves(board, startPosition);
        HashSet<ChessMove> validMoves = HashSet.newHashSet(possibleMoves.size());
        for (ChessMove move : possibleMoves) {
            ChessPiece tempPiece = board.getPiece(move.getEndPosition());
            board.addPiece(startPosition, null); //Remove the piece so that it can be moved to a new spot temporarily
            board.addPiece(move.getEndPosition(), currPiece);
            if (!isInCheck(currPiece.getTeamColor())) {
                validMoves.add(move);
            }
            //Reset the board to its original layout
            board.addPiece(move.getEndPosition(), tempPiece);
            board.addPiece(startPosition, currPiece);

        }
        return validMoves;
    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        boolean isTeamsTurn = getTeamTurn() == board.getTeamOfSquare(move.getStartPosition());
        Collection<ChessMove> goodMoves = validMoves(move.getStartPosition());
        if (goodMoves == null) {
            throw new InvalidMoveException("No valid moves available");
        }
        boolean isValidMove = goodMoves.contains(move);

        if (isValidMove && isTeamsTurn) {
            ChessPiece pieceToMove = board.getPiece(move.getStartPosition());
            if (move.getPromotionPiece() != null) { //Change piece type if promotion is applied
                pieceToMove = new ChessPiece(pieceToMove.getTeamColor(), move.getPromotionPiece());
            }

            board.addPiece(move.getStartPosition(), null);
            board.addPiece(move.getEndPosition(), pieceToMove);
            setTeamTurn(getTeamTurn() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
        }
        else {
            throw new InvalidMoveException(String.format("Valid move: %b  Your Turn: %b", isValidMove, isTeamsTurn));
        }
    }


    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = null;
        for (int y = 1; y <= 8 && kingPos == null; y++) { //Find the king
            for (int x = 1; x <= 8  && kingPos == null; x++) {
                ChessPiece currPiece = board.getPiece(new ChessPosition(y, x));
                if (currPiece == null) {
                    continue;
                }
                if (currPiece.getTeamColor() == teamColor && currPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    kingPos = new ChessPosition(y, x);
                }
            }
        }

        //This portion looks to see if any other piece is attacking the king
        for (int y = 1; y <= 8; y++) {
            for (int x = 1; x <= 8; x++) {
                ChessPiece currPiece = board.getPiece(new ChessPosition(y, x));
                if (currPiece == null || currPiece.getTeamColor() == teamColor) {
                    continue;
                }
                for (ChessMove enemyMove : currPiece.pieceMoves(board, new ChessPosition(y, x))) {
                    if (enemyMove.getEndPosition().equals(kingPos)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && isInStalemate(teamColor);
    }

    //We know if it is in Stalemate if there isn't any available moves
    public boolean isInStalemate(TeamColor teamColor) {
        for (int y = 1; y <= 8; y++) {
            for (int x = 1; x <= 8; x++) {
                ChessPosition currPosition = new ChessPosition(y, x);
                ChessPiece currPiece = board.getPiece(currPosition);
                Collection<ChessMove> moves;

                if (currPiece != null && teamColor == currPiece.getTeamColor()) {
                    moves = validMoves(currPosition);
                    if (moves != null && !moves.isEmpty()) {
                        return false; //if there's even one valid move, then it's not stalemate
                    }
                }
            }
        }
        return true;
    }


    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    public ChessBoard getBoard() {
        return board;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean getGameOver() {
        return gameOver;
    }
}