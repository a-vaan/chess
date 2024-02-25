package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.AuthData;
import model.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.ListGamesRequest;
import model.result.CreateGameResult;
import model.result.ListGamesResult;

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

    public void joinGame(JoinGameRequest req, String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if(authData == null) {
            throw new DataAccessException("Unauthorized");
        }

        GameData game = gameDAO.getGame(req.gameID());
        if(game == null) {
            throw new DataAccessException("Bad request");
        }

        if(req.playerColor() == null) {
            return;
        }

        AuthData auth = authDAO.getAuth(authToken);
        if(req.playerColor().equals("WHITE")) {
            if(game.whiteUsername() == null) {
                game = new GameData(game.gameID(), auth.username(), game.blackUsername(), game.gameName(), game.game());
            } else {
                throw new DataAccessException("Already taken");
            }
        } else {
            if(game.blackUsername() == null) {
                game = new GameData(game.gameID(), game.whiteUsername(), auth.username(), game.gameName(), game.game());
            } else {
                throw new DataAccessException("Already taken");
            }
        }

        gameDAO.updateGame(game);
    }

    public ListGamesResult listGames(ListGamesRequest req) throws DataAccessException {
        AuthData authData = authDAO.getAuth(req.authToken());
        if(authData == null) {
            throw new DataAccessException("Unauthorized");
        }

        return new ListGamesResult(gameDAO.listGames());
    }
}
