package chess.calculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class bishop_move_calculator implements generic_move_calculator {

    public static HashSet<ChessMove> getMoves(ChessBoard board, ChessPosition currPosition) {
        int currX = currPosition.getColumn();
        int currY = currPosition.getRow();
        int[][] potentialMoveDirections = {{-1, 1}, {1, 1}, {1, -1}, {-1, -1}};

        ChessGame.TeamColor team = board.getTeamOfSquare(currPosition);

        return generic_move_calculator.generateDirectionalMoves(board, currPosition, potentialMoveDirections, currY, currX, team);
    }

}