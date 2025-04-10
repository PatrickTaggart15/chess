import chess.*;
import client.ServerFacade;
import ui.PreLogin;

public class Main {
    public static void main(String[] args) throws Exception {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        ServerFacade server = new ServerFacade();
        PreLogin preLogin = new PreLogin(server);
        preLogin.run();
    }
}