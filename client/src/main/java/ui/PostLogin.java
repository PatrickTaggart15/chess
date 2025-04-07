package ui;

import chess.ChessGame;
import client.ServerFacade;
import model.GameData;

import java.util.*;

import static java.lang.System.out;
import static ui.EscapeSequences.*;

public class PostLogin {

    ServerFacade server;
    List<GameData> games;

    public PostLogin(ServerFacade server) {
        this.server = server;
        games = new ArrayList<>();
    }

    public void run() {
        boolean loggedIn = true;
        out.print(RESET_TEXT_COLOR + RESET_BG_COLOR);
        while (loggedIn) {
            String[] input = getUserInput();
            switch (input[0]) {
                case "quit":
                    return;
                case "help":
                    printHelpMenu();
                    break;
                case "logout":
                    server.logout();
                    loggedIn = false;
                    break;
                case "list":
                    refreshGames();
                    printGames();
                    break;
                case "create":
                    if (input.length != 2) {
                        out.println("Provide a super sweet name");
                        printCreate();
                        break;
                    }
                    int gameID = server.createGame(input[1]);
                    out.printf("Created Game", gameID);
                    break;

                case "join":
                    if (input.length != 3 || !input[1].matches("\\d") || !input[2].toUpperCase().matches("WHITE|BLACK")) {
                        out.println("Please provide a game ID and color choice");
                        printJoin();
                        break;
                    }
                    int gameNum = Integer.parseInt(input[1]);
                    if (games.isEmpty() || games.size() <= gameNum) {
                        refreshGames();
                        if (games.isEmpty()) {
                            out.println("Error: please first create a game");
                            break;
                        }
                        if (games.size() <= gameNum) {
                            out.println("Error: that Game ID does not exist");
                            printGames();
                            break;
                        }
                    }
                    GameData joinGame = games.get(Integer.parseInt(input[1]));
                    ChessGame.TeamColor color = input[2].equalsIgnoreCase("WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
                    boolean perspective = true;
                    if (color == ChessGame.TeamColor.BLACK){
                        perspective = false;
                    }

                    if (server.joinGame(joinGame.gameID(), input[2].toUpperCase())) {
                        out.println("You have joined the game");
                        new BoardPrinter(joinGame.game().getBoard()).printBoard(perspective);
                        break;
                    } else {
                        out.println("Game does not exist or color taken");
                        printJoin();
                        break;
                    }

                case "observe":
                    if (input.length != 2) {
                        out.println("Provide a game ID");
                        printObserve();
                        break;
                    }
                    int gameNum1 = Integer.parseInt(input[1]);
                    if (games.isEmpty() || games.size() <= gameNum1) {
                        refreshGames();
                        if (games.isEmpty() || games.size() <= gameNum1) {
                            out.println("Error: that Game ID does not exist");
                            printGames();
                            break;
                        }
                    }

                    GameData observeGame = games.get(Integer.parseInt(input[1]));
                    if (true) {
                        out.println("You have joined the game as an observer");
                        new BoardPrinter(observeGame.game().getBoard()).printBoard(true);
                        break;
                    } else {
                        out.println("Game does not exist. Sorry bro :(");
                        printObserve();
                        break;
                    }
                default:
                    out.println("Command not recognized, please try again");
                    printHelpMenu();
                    break;
            }
        }

        PreLogin prelogin = new PreLogin(server);
        prelogin.run();
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
