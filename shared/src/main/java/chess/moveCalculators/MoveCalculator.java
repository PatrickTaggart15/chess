package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public interface MoveCalculator {

    //A Hashset in Java just implements a Set. A set interface which means
    // it stores unique elements and does not maintain any specific order
    static HashSet<ChessMove> getMoves(ChessBoard board, ChessPosition currPosition) {
        return null;
    }

    static boolean isValidSquare(ChessPosition position) {
        return (position.getRow() > 0 && position.getRow() < 9) &&
                (position.getColumn() > 0 && position.getColumn() < 9);
    }

    // Generate all possible moves for static moves (Knight, Pawn, King)
    static HashSet<ChessMove> staticMoves(ChessPosition currPosition, int[][] relativeMoves, ChessBoard board) {
        HashSet<ChessMove> moves = HashSet.newHashSet(8); //8 is the max number of moves of a Knight

        int currX = currPosition.getColumn();
        int currY = currPosition.getRow();

        ChessGame.TeamColor team = board.getTeamOfSquare(currPosition);
        for (int[] relativeMove : relativeMoves) {
            ChessPosition possiblePosition = new ChessPosition(currX + relativeMove[0], currY + relativeMove[1]);
            if (MoveCalculator.isValidSquare(possiblePosition) && board.getTeamOfSquare(possiblePosition) != team)
                moves.add(new ChessMove(currPosition, possiblePosition, null));
        }
        return moves;
    }

    // Generate all possible moves for variable moves (Rook, Bishop, Queen)
    static HashSet<ChessMove> generateDirectionalMoves(ChessBoard board, ChessPosition currPosition, int[][] moveDirections, int currY, int currX, ChessGame.TeamColor team) {
        HashSet<ChessMove> moves = HashSet.newHashSet(27);
        for (int[] direction : moveDirections) {
            boolean obstructed = false;
            int i = 1;
            while (!obstructed) {
                ChessPosition possiblePosition = new ChessPosition(currY + direction[1]*i, currX + direction[0]*i);
                if (!MoveCalculator.isValidSquare(possiblePosition)) {
                    obstructed = true;
                }
                else if (board.getPiece(possiblePosition) == null) {
                    moves.add(new ChessMove(currPosition, possiblePosition, null));
                }
                else if (board.getTeamOfSquare(possiblePosition) != team) {
                    moves.add(new ChessMove(currPosition, possiblePosition, null));
                    obstructed = true;
                }
                else if (board.getTeamOfSquare(possiblePosition) == team) {
                    obstructed = true;
                }
                else {
                    obstructed = true;
                }
                i++;
            }
        }
        return moves;
    }

}