package dataaccess;

import dataModelClasses.AuthData;

public interface AuthDAO {

    void addAuth(AuthData authData);

    void deleteAuth(String authToken);

    AuthData getAuth(String authToken) throws DataAccessException;

    void clear();

}
