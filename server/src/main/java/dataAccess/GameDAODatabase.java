package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DAOInterfaces.GameDAO;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class GameDAODatabase implements GameDAO {

    public GameDAODatabase() throws DataAccessException {
        DAODatabaseFunctions daoFunctions = new DAODatabaseFunctions();
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  game (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUserName` varchar(45) DEFAULT NULL,
              `blackUserName` varchar(45) DEFAULT NULL,
              `gameName` varchar(45) NOT NULL,
              `gameData` TEXT(65000) DEFAULT NULL,
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """
        };
        daoFunctions.configureDatabase(createStatements);
    }

    @Override
    public Integer createGame(String gameName) {
        var statement = "INSERT INTO game (whiteUserName, blackUserName, gameName, gameData) VALUES (?, ?, ?, ?)";
        ChessGame newChess = new ChessGame();
        var json = new Gson().toJson(newChess);
        return executeUpdate(statement, null, null, gameName, json);
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        int gameID = game.gameID();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "UPDATE game SET whiteUserName = ?, blackUserName = ?, gameName = ?, gameData = ? WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, game.whiteUsername());
                ps.setString(2, game.blackUsername());
                ps.setString(3, game.gameName());
                ps.setString(4, new Gson().toJson(game.game()));
                ps.setInt(5, gameID);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.toString());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUserName, blackUserName, gameName, gameData FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readChess(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.toString());
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUserName, blackUserName, gameName, gameData FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readChess(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.toString());
        }
        return result;
    }

    @Override
    public void deleteAllGames() {
        var statement = "TRUNCATE game";
        executeUpdate(statement);
    }

    private GameData readChess (ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUserName = rs.getString("whiteUserName");
        var blackUserName = rs.getString("blackUserName");
        var gameName = rs.getString("gameName");
        var json = rs.getString("gameData");
        ChessGame gameData = new Gson().fromJson(json, ChessGame.class);
        return new GameData(gameID, whiteUserName, blackUserName, gameName, gameData);
    }

    private int executeUpdate(String statement, Object... params) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //    private void configureDatabase() throws DataAccessException {
//        DatabaseManager.createDatabase();
//        try (var conn = DatabaseManager.getConnection()) {
//            for (var statement : createStatements) {
//                try (var preparedStatement = conn.prepareStatement(statement)) {
//                    preparedStatement.executeUpdate();
//                }
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
