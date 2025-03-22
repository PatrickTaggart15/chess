package ui;

import chess.ChessBoard;
import chess.ChessGame;
import client.ServerFacadeTests;
import model.GameData;
import ui.EscapeSequences.*;

import java.util.*;

import static java.lang.System.out;
import static ui.EscapeSequences.*;

public class postLogin {

    ServerFacadeTests server;
    List<GameData> games;

    public postLogin(ServerFacadeTests server) {
        this.server = server;
        games = new ArrayList<>();
    }

    public void run(){


    }



}
