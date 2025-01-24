package chess.MoveCalculators;

import chess.*;

import java.util.HashSet;

public class PawnMoveCalculator implements MoveCalculator {

    public static HashSet<ChessMove> getMoves(ChessBoard board, ChessPosition currPosition) {
        HashSet<ChessMove> moves = HashSet.newHashSet(16); //16 is the max number of moves of a Pawn
        int currX = currPosition.getColumn();
        int currY = currPosition.getRow();
        ChessPiece.PieceType[] promotionPieces = new ChessPiece.PieceType[]{null};

        ChessGame.TeamColor team = board.getTeamOfSquare(currPosition);
        int moveIncrement = team == ChessGame.TeamColor.WHITE ? 1 : -1;

        boolean promote = (team == ChessGame.TeamColor.WHITE && currY == 7) || (team == ChessGame.TeamColor.BLACK && currY == 2);
        if (promote) {
            promotionPieces = new ChessPiece.PieceType[]{ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN};
        }

        for (ChessPiece.PieceType promotionPiece : promotionPieces) {
            //Add moving forward, if available
            ChessPosition forwardPosition = new ChessPosition(currY + moveIncrement, currX);
            if (MoveCalculator.isValidSquare(forwardPosition) && board.getPiece(forwardPosition) == null) {
                moves.add(new ChessMove(currPosition, forwardPosition, promotionPiece));
            }
            //Add left attack, if available
            ChessPosition leftAttack = new ChessPosition(currY + moveIncrement, currX-1);
            if (MoveCalculator.isValidSquare(leftAttack) &&
                    board.getPiece(leftAttack) != null &&
                    board.getTeamOfSquare(leftAttack) != team) {
                moves.add(new ChessMove(currPosition, leftAttack, promotionPiece));
            }
            //Add right attack, if available
            ChessPosition rightAttack = new ChessPosition(currY + moveIncrement, currX+1);
            if (MoveCalculator.isValidSquare(rightAttack) &&
                    board.getPiece(rightAttack) != null &&
                    board.getTeamOfSquare(rightAttack) != team) {
                moves.add(new ChessMove(currPosition, rightAttack, promotionPiece));
            }

            //Add first move double, if available
            ChessPosition doubleForwardPosition = new ChessPosition(currY + moveIncrement*2, currX);
            if (MoveCalculator.isValidSquare(doubleForwardPosition) &&
                    ((team == ChessGame.TeamColor.WHITE && currY == 2) || (team == ChessGame.TeamColor.BLACK && currY == 7)) &&
                    board.getPiece(doubleForwardPosition) == null &&
                    board.getPiece(forwardPosition) == null) {
                moves.add(new ChessMove(currPosition, doubleForwardPosition, promotionPiece));
            }

        }

        return moves;
    }

}