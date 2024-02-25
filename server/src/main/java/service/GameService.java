package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.AuthData;
import model.request.CreateGameRequest;
import model.result.CreateGameResult;

public class GameService {

    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public CreateGameResult createGame(CreateGameRequest req, String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);

        if(authData == null) {
            throw new DataAccessException("Unauthorized");
        }

        if(req.gameName() == null || req.gameName().isEmpty()) {
            throw new DataAccessException("Bad request");
        }

        Integer gameID = gameDAO.createGame(req.gameName());
        return new CreateGameResult(gameID);
    }

}
