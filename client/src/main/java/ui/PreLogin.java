package ui;

import client.ServerFacade;

import java.util.Scanner;

import static java.lang.System.out;
import static ui.EscapeSequences.*;

public class PreLogin {

    ServerFacade server;
    PostLogin postLogin;

    public void run(){
        boolean loggedIn = false;
        out.print(RESET_TEXT_COLOR + RESET_BG_COLOR);
        out.println("Whats up? You here for chess? Enter 'help' to get started.");
        while (!loggedIn) {
            String[] input = getUserInput();
            switch (input[0]) {
                case "quit":
                    return;
                case "help":
                    printHelpMenu();
                case "login":
                    if (input.length != 3) {
                        out.println("Provide a username and password");
                        printLogin();
                        break;
                    }
                    if (server.login(input[1], input[2])) {
                        out.println("Login Successful");
                        loggedIn = true;
                        break;
                    }
                    else {
                        out.println("Username or password incorrect, please try again");
                        printLogin();
                        break;
                    }
                default:
                    out.println("Command not recognized, please try again");
                    printHelpMenu();
                    break;

            }
        }
        postLogin.run();
    }

    private String[] getUserInput(){
        out.print("/n LOGGED OUT] >>> ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().split(" ");
    }

    private void printRegister(){
        out.println("register <USERNAME> <PASSWORD> <EMAIL> - create a new user");
    }

    private void printLogin(){
        out.println("login <USERNAME> <PASSWORD> - login to an existing user");
    }

    private void printHelpMenu(){
        printRegister();
        printLogin();
        out.println("quit - stop playing");
        out.println("help = show this menu");
    }

}
