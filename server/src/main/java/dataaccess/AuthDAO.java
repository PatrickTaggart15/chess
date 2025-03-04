package dataaccess;

import data_model_classes.AuthData;

public interface AuthDAO {

    void addAuth(AuthData authData);

    void deleteAuth(String authToken);

    AuthData getAuth(String authToken) throws DataAccessException;

    void clear();

}
