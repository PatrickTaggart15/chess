package ui;

import client.ServerFacadeTests;

import java.util.Scanner;

import static java.lang.System.out;
import static ui.EscapeSequences.*;

public class preLogin {

    ServerFacadeTests server;
    postLogin postLogin;

    public void run(){

    }

    public preLogin(ServerFacadeTests server) {
        this.server = server;
        postLogin = new postLogin(server);
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
