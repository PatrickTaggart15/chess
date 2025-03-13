package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class UserService {

    UserDAO userDAO;
    AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData createUser(UserData userData) throws BadRequestException {

        try {
            String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
            UserData hashed_user_data = new UserData(userData.username(), hashedPassword, userData.email());
            userDAO.createUser(hashed_user_data);
        } catch (DataAccessException e) {
            throw new BadRequestException(e.getMessage());
        }
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(userData.username(), authToken);
        authDAO.addAuth(authData);

        return authData;
    }

    public AuthData loginUser(UserData userData) throws UnauthorizedException {
        boolean userAuthenticated = false;
        //Rehash the password
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());

        //Check is the hashed password and the regular password are the same
        userAuthenticated = BCrypt.checkpw(userData.password(), hashedPassword);
        //userAuthenticated = userDAO.authenticateUser(userData.username(), userData.password());


        if (userAuthenticated) {
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(userData.username(), authToken);
            authDAO.addAuth(authData);
            return authData;
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public void logoutUser(String authToken) throws UnauthorizedException {
        try {
            authDAO.getAuth(authToken);
        } catch (DataAccessException e) {
            throw new UnauthorizedException();
        }
        authDAO.deleteAuth(authToken);
    }


    public void clear() {
        userDAO.clear();
        authDAO.clear();
    }
}
