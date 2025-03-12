package server;


import dataaccess.*;
import org.eclipse.jetty.websocket.api.Session;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.concurrent.ConcurrentHashMap;

public class Server {

    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    static UserService userService;
    static GameService gameService;

    UserHandler userHandler;
    GameHandler gameHandler;

    public Server() {

        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();

        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);

        userHandler = new UserHandler(userService);
        gameHandler = new GameHandler(gameService);

    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        //Spark.webSocket("/connect", WebsocketHandler.class);

        Spark.delete("/db", this::clear);
        Spark.post("/user", userHandler::register);
        Spark.post("/session", userHandler::login);
        Spark.delete("/session", userHandler::logout);

        Spark.get("/game", gameHandler::listGames);
        Spark.post("/game", gameHandler::createGame);
        Spark.put("/game", gameHandler::joinGame);

        Spark.exception(BadRequestException.class, this::badRequestExceptionHandler);
        Spark.exception(UnauthorizedException.class, this::unauthorizedExceptionHandler);
        Spark.exception(Exception.class, this::genericExceptionHandler);
        //This line initializes the server and can be removed once you have a functioning endpoint

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clear(Request req, Response resp) {

        userService.clear();
        gameService.clear();

        resp.status(200);
        return "{}";
    }

    private void badRequestExceptionHandler(BadRequestException ex, Request req, Response resp) {
        resp.status(400);
        resp.body("{ \"message\": \"Error: bad request\" }");
    }

    private void unauthorizedExceptionHandler(UnauthorizedException ex, Request req, Response resp) {
        resp.status(401);
        resp.body("{ \"message\": \"Error: unauthorized\" }");
    }

    private void genericExceptionHandler(Exception ex, Request req, Response resp) {
        resp.status(500);
        resp.body("{ \"message\": \"Error: %s\" }".formatted(ex.getMessage()));
    }

}
