package chess.calculators;

import chess.*;

import java.util.HashSet;

public class pawn_move_calculator implements generic_move_calculator {

    public static HashSet<ChessMove> getMoves(ChessBoard board, ChessPosition currPosition) {
        HashSet<ChessMove> moves = HashSet.newHashSet(16); //16 is the max number of moves of a Pawn
        int currX = currPosition.getColumn();
        int currY = currPosition.getRow();
        ChessPiece.PieceType[] promotionPieces = new ChessPiece.PieceType[]{null};

        ChessGame.TeamColor team = board.getTeamOfSquare(currPosition);
        int moveIncrement = team == ChessGame.TeamColor.WHITE ? 1 : -1;

        boolean promote = (team == ChessGame.TeamColor.WHITE && currY == 7) || (team == ChessGame.TeamColor.BLACK && currY == 2);
        if (promote) {
            promotionPieces = new ChessPiece.PieceType[]{ChessPiece.PieceType.ROOK,
                    ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN};
        }

        for (ChessPiece.PieceType promotionPiece : promotionPieces) {
            //Forward
            ChessPosition forwardMove = new ChessPosition(currY + moveIncrement, currX);
            if (generic_move_calculator.isValidSquare(forwardMove) && board.getPiece(forwardMove) == null) {
                moves.add(new ChessMove(currPosition, forwardMove, promotionPiece));
            }

            //doubleForwardMove
            ChessPosition doubleForwardMove = new ChessPosition(currY + moveIncrement*2, currX);
            if (generic_move_calculator.isValidSquare(doubleForwardMove) &&
                    ((team == ChessGame.TeamColor.WHITE && currY == 2) || (team == ChessGame.TeamColor.BLACK && currY == 7)) &&
                    board.getPiece(doubleForwardMove) == null &&
                    board.getPiece(forwardMove) == null) {
                moves.add(new ChessMove(currPosition, doubleForwardMove, promotionPiece));
            }

            //attackLeft
            ChessPosition attackLeft = new ChessPosition(currY + moveIncrement, currX-1);
            if (generic_move_calculator.isValidSquare(attackLeft) &&
                    board.getPiece(attackLeft) != null &&
                    board.getTeamOfSquare(attackLeft) != team) {
                moves.add(new ChessMove(currPosition, attackLeft, promotionPiece));
            }
            //attackRight
            ChessPosition attackRight = new ChessPosition(currY + moveIncrement, currX+1);
            if (generic_move_calculator.isValidSquare(attackRight) &&
                    board.getPiece(attackRight) != null &&
                    board.getTeamOfSquare(attackRight) != team) {
                moves.add(new ChessMove(currPosition, attackRight, promotionPiece));
            }


        }

        return moves;
    }

}