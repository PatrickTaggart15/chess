
package chess.calculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class RookCalculator implements GenericCalculator {

    public static HashSet<ChessMove> getMoves(ChessBoard board, ChessPosition currPosition) {
        int currX = currPosition.getColumn();
        int currY = currPosition.getRow();
        int[][] potentialMoveDirections = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        ChessGame.TeamColor team = board.getTeamOfSquare(currPosition);

        return GenericCalculator.generateDirectionalMoves(board, currPosition, potentialMoveDirections, currY, currX, team);
    }

}
