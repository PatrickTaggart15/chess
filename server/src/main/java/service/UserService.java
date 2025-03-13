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
            UserData hashedUserData = new UserData(userData.username(), hashedPassword, userData.email());
            userDAO.createUser(hashedUserData);
        } catch (DataAccessException e) {
            throw new BadRequestException(e.getMessage());
        }
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(userData.username(), authToken);
        authDAO.addAuth(authData);

        return authData;
    }

    public AuthData loginUser(UserData userRequestData) throws UnauthorizedException {
        boolean userAuthenticated = false;

        try {
            //Rehash the password
            String password = userDAO.getUser(userRequestData.username()).password();
            userAuthenticated = BCrypt.checkpw(userRequestData.password(), password);
        } catch (DataAccessException noUser){
            throw new UnauthorizedException();
        }

        if (userAuthenticated) {
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(userRequestData.username(), authToken);
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
