package service;

import chess.ChessGame;
import chess.InvalidMoveException;
import dataAccess.DAOInterfaces.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.DAOInterfaces.GameDAO;
import model.AuthData;
import model.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.ListGamesRequest;
import model.result.CreateGameResult;
import model.result.ListGamesResult;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.MakeMove;

import java.util.Objects;

public class GameService {

    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    /**
     * Creates a new GameData object and saves it to the database.
     *
     * @param req: CreateGameRequest object containing the gameName as a string
     * @param authToken: the authToken of the user trying to make the game
     * return: CreateGameResult object containing the gameID
     * @throws DataAccessException: to be thrown if the user is not authorized or submits a bad request
     */
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

    /**
     * Attempts to join the player to a game if they specify a color, and if not add them as a viewer.
     *
     * @param req: JoinGameRequest object containing the player color and gameID
     * @param authToken: the authToken of the user trying to make the game
     * @throws DataAccessException: to be thrown if the user is not authorized or submits a bad request
     */
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
        if(req.playerColor().equals("white") || req.playerColor().equals("WHITE")) {
            if(game.whiteUsername() == null) {
                game = new GameData(game.gameID(), auth.username(), game.blackUsername(), game.gameName(), game.game());
            } else {
                throw new DataAccessException("Already taken");
            }
        } else if(req.playerColor().equals("black") || req.playerColor().equals("BLACK")){
            if(game.blackUsername() == null) {
                game = new GameData(game.gameID(), game.whiteUsername(), auth.username(), game.gameName(), game.game());
            } else {
                throw new DataAccessException("Already taken");
            }
        } else {
            System.out.println("Null being thrown");
        }

        gameDAO.updateGame(game);
    }

    /**
     * Lists all the games currently in the database.
     *
     * @param req: ListGamesRequest object containing the authToken
     * return: ListGames result object containing a list of all GameData objects in the database
     * @throws DataAccessException: to be thrown if the user is not authorized
     */
    public ListGamesResult listGames(ListGamesRequest req) throws DataAccessException {
        AuthData authData = authDAO.getAuth(req.authToken());

        if(authData == null) {
            throw new DataAccessException("Unauthorized");
        }

        return new ListGamesResult(gameDAO.listGames());
    }

    public String joinPlayer(JoinPlayer playerData) throws DataAccessException {
        AuthData authData = authDAO.getAuth(playerData.getAuthString());
        GameData gameData = gameDAO.getGame(playerData.getGameID());

        if(authData == null) {
            return "Error: bad auth token";
        }

        if(gameData == null) {
            return "Error: incorrect gameID";
        }

        if((playerData.getPlayerColor() == ChessGame.TeamColor.WHITE
                && gameData.whiteUsername() == null ||
                (playerData.getPlayerColor() == ChessGame.TeamColor.BLACK
                        && gameData.blackUsername() == null))) {
            return "Error: game empty";
        }

        if((playerData.getPlayerColor() == ChessGame.TeamColor.WHITE
                && Objects.equals(authData.username(), gameData.blackUsername())) ||
                (playerData.getPlayerColor() == ChessGame.TeamColor.BLACK
                && Objects.equals(authData.username(), gameData.whiteUsername()))) {
            return "Error: spot already taken";
        }

        return "";
    }

    public String joinObserver(JoinObserver playerData) throws DataAccessException {
        AuthData authData = authDAO.getAuth(playerData.getAuthString());
        GameData gameData = gameDAO.getGame(playerData.getGameID());

        if(authData == null) {
            return "Error: bad auth token";
        }

        if(gameData == null) {
            return "Error: incorrect gameID";
        }

        return authData.username();
    }

    public String makeMove(MakeMove moveData) throws DataAccessException {
        AuthData authData = authDAO.getAuth(moveData.getAuthString());
        GameData gameData = gameDAO.getGame(moveData.getGameID());

        ChessGame currentGame = gameData.game();
        if((currentGame.getTeamTurn() == ChessGame.TeamColor.WHITE
                && !Objects.equals(gameData.whiteUsername(), authData.username())) ||
                (currentGame.getTeamTurn() == ChessGame.TeamColor.BLACK
                && !Objects.equals(gameData.blackUsername(), authData.username()))) {
            return "Error: not current player's turn";
        }
        try {
            currentGame.makeMove(moveData.getMove());
            GameData updatedGameData = new GameData(gameData.gameID(), gameData.whiteUsername(),
                    gameData.blackUsername(), gameData.gameName(), currentGame);
            gameDAO.updateGame(updatedGameData);
            return authData.username() + "," + checkIfInCheck(gameData, currentGame);
        } catch (InvalidMoveException e) {
            return "Error: invalid move";
        }
    }

    private String checkIfInCheck(GameData gameData, ChessGame currentGame) {
        if(currentGame.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            return "checkmate," + gameData.whiteUsername();
        } else if(currentGame.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            return "checkmate," + gameData.blackUsername();
        } else if(currentGame.isInCheck(ChessGame.TeamColor.WHITE)) {
            return "check," + gameData.whiteUsername();
        } else if(currentGame.isInCheck(ChessGame.TeamColor.BLACK)) {
            return "check," + gameData.blackUsername();
        }
        return "null";
    }
}
