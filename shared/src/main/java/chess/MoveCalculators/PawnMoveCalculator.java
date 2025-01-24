package chess.MoveCalculators;

import chess.*;

import java.util.HashSet;

public class PawnMoveCalculator implements GenericMoveCalculator {

    public static HashSet<ChessMove> getMoves(ChessBoard board, ChessPosition currPosition) {
        HashSet<ChessMove> moves = HashSet.newHashSet(16); //16 is the max number of moves of a Pawn
        int Xcur = currPosition.getColumn();
        int Ycur = currPosition.getRow();
        ChessPiece.PieceType[] promotionPieces = new ChessPiece.PieceType[]{null};

        ChessGame.TeamColor team = board.getTeamOfSquare(currPosition);
        int moveIncrement = team == ChessGame.TeamColor.WHITE ? 1 : -1;

        boolean promote = (team == ChessGame.TeamColor.WHITE && Ycur == 7) || (team == ChessGame.TeamColor.BLACK && Ycur == 2);
        if (promote) {
            promotionPieces = new ChessPiece.PieceType[]{ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN};
        }

        for (ChessPiece.PieceType promotionPiece : promotionPieces) {
            //Forward
            ChessPosition forwardMove = new ChessPosition(Ycur + moveIncrement, Xcur);
            if (chess.MoveCalculators.GenericMoveCalculator.isValidSquare(forwardMove) && board.getPiece(forwardMove) == null) {
                moves.add(new ChessMove(currPosition, forwardMove, promotionPiece));
            }

            //doubleForwardMove
            ChessPosition doubleForwardMove = new ChessPosition(Ycur + moveIncrement*2, Xcur);
            if (chess.MoveCalculators.GenericMoveCalculator.isValidSquare(doubleForwardMove) &&
                    ((team == ChessGame.TeamColor.WHITE && Ycur == 2) || (team == ChessGame.TeamColor.BLACK && Ycur == 7)) &&
                    board.getPiece(doubleForwardMove) == null &&
                    board.getPiece(forwardMove) == null) {
                moves.add(new ChessMove(currPosition, doubleForwardMove, promotionPiece));
            }

            //attackLeft
            ChessPosition attackLeft = new ChessPosition(Ycur + moveIncrement, Xcur-1);
            if (chess.MoveCalculators.GenericMoveCalculator.isValidSquare(attackLeft) &&
                    board.getPiece(attackLeft) != null &&
                    board.getTeamOfSquare(attackLeft) != team) {
                moves.add(new ChessMove(currPosition, attackLeft, promotionPiece));
            }
            //attackRight
            ChessPosition attackRight = new ChessPosition(Ycur + moveIncrement, Xcur+1);
            if (chess.MoveCalculators.GenericMoveCalculator.isValidSquare(attackRight) &&
                    board.getPiece(attackRight) != null &&
                    board.getTeamOfSquare(attackRight) != team) {
                moves.add(new ChessMove(currPosition, attackRight, promotionPiece));
            }


        }

        return moves;
    }

}