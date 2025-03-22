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
        boolean loggedIn = true;
        out.print(RESET_TEXT_COLOR + RESET_BG_COLOR);
        while (loggedIn) {
            String[] input = getUserInput();


    }

        private String[] getUserInput() {
            out.print("\n[LOGGED IN] >>> ");
            Scanner scanner = new Scanner(System.in);
            return scanner.nextLine().split(" ");
        }

        private void refreshGames() {
            games = new ArrayList<>();
            HashSet<GameData> gameList = server.listGames();
            games.addAll(gameList);
        }

        private void printGames() {
            for (int i = 0; i < games.size(); i++) {
                GameData game = games.get(i);
                String whiteUser = game.whiteUsername() != null ? game.whiteUsername() : "open";
                String blackUser = game.blackUsername() != null ? game.blackUsername() : "open";
                out.printf("%d -- Game Name: %s  |  White User: %s  |  Black User: %s %n", i, game.gameName(), whiteUser, blackUser);
            }
        }

        private void printHelpMenu() {
            printCreate();
            out.println("list - list all games");
            printJoin();
            printObserve();
            out.println("logout - log out of current user");
            out.println("quit - stop playing");
            out.println("help - show this menu");
        }

        private void printCreate() {
            out.println("create <NAME> - create a new game");
        }

        private void printJoin() {
            out.println("join <ID> [WHITE|BLACK] - join a game as color");
        }

        private void printObserve() {
            out.println("observe <ID> - observe a game");
        }

}
