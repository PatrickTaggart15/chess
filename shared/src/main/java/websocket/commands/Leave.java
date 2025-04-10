package websocket.commands;

import chess.ChessGame;

public class Leave extends UserGameCommand {

    int gameID;

    public Leave(String authToken, int gameID) {
        super(authToken,gameID);
        this.commandType = CommandType.LEAVE;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
