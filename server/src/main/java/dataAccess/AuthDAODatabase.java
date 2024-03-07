package dataAccess;

import dataAccess.DAOInterfaces.AuthDAO;
import model.AuthData;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;
import java.sql.SQLException;
import java.util.UUID;

public class AuthDAODatabase implements AuthDAO {

    public AuthDAODatabase() throws DataAccessException {
        DAODatabaseFunctions daoFunctions = new DAODatabaseFunctions();
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  auth (
              `authToken` varchar(45) NOT NULL,
              `username` varchar(45) NOT NULL,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """
        };
        daoFunctions.configureDatabase(createStatements);
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        try {
            var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            String authToken = UUID.randomUUID().toString();
            executeUpdate(statement, authToken, username);
            return authToken;
        } catch (Exception e) {
            throw new DataAccessException(e.toString());
        }
    }

    @Override
    public AuthData getAuth(String authToken) {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(rs.getString(1), rs.getString(2));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {
        var statement = "DELETE FROM auth WHERE authToken=?";
        executeUpdate(statement, authToken);
    }

    @Override
    public void deleteAllAuths() {
        var statement = "TRUNCATE auth";
        executeUpdate(statement);
    }

    private int executeUpdate(String statement, Object... params) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
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
