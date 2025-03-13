package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class GameService {

    GameDAO gameDAO;
    AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public HashSet<GameData> listGames(String authToken) throws UnauthorizedException {
        try {
            authDAO.getAuth(authToken);
        } catch (DataAccessException e) {
            throw new UnauthorizedException();
        }
        return gameDAO.listGames();
    }

    public int createGame(String authToken, String gameName) throws UnauthorizedException {
        try {
            authDAO.getAuth(authToken);
        } catch (DataAccessException e) {
            throw new UnauthorizedException();
        }

        int gameID;
        do { // Get random gameIDs until the gameID is not already in use
            gameID = ThreadLocalRandom.current().nextInt(1, 10000);
        } while (gameDAO.gameExists(gameID));

        try {
            gameDAO.createGame(new GameData(gameID, null, null, gameName, null));
        }catch(DataAccessException e){
           return -1;
        }
        return gameID;
    }

    /***
     *
     * @param authToken authToken of user
     * @param gameID gameID of the game to join
     * @param color (nullable) team color to join as
     * @return boolean of success
     * @throws UnauthorizedException invalid authToken
     * @throws BadRequestException bad request
     */
    public boolean joinGame(String authToken, int gameID, String color) throws UnauthorizedException, BadRequestException {
        AuthData authData;
        GameData gameData;
        try {
            authData = authDAO.getAuth(authToken);
        } catch (DataAccessException e) {
            throw new UnauthorizedException();
        }

        try {
            gameData = gameDAO.getGame(gameID);
        } catch (DataAccessException e) {
            throw new BadRequestException(e.getMessage());
        }

        String whiteUser = gameData.whiteUsername();
        String blackUser = gameData.blackUsername();

        if (color == null || (!color.equals("WHITE") && !color.equals("BLACK"))) {
            throw new BadRequestException("%s is not a valid team color".formatted(String.valueOf(color)));
        }

        if (color.equals("WHITE")) {
            if (whiteUser != null) {
                return false; // Spot taken
            }
            whiteUser = authData.username();
        } else { // color == "BLACK"
            if (blackUser != null) {
                return false; // Spot taken
            }
            blackUser = authData.username();
        }
        try {
            gameDAO.updateGame(new GameData(gameID, whiteUser, blackUser, gameData.gameName(), gameData.game()));
            return true;
        }catch(DataAccessException e){
            return false;
        }
    }

    public void clear() {
        gameDAO.clear();
    }
}
