package dataAccess;

import dataAccess.DAOInterfaces.AuthDAO;
import model.AuthData;

import java.util.UUID;

public class AuthDAODatabase implements AuthDAO {

    private final DAODatabaseFunctions daoFunctions = new DAODatabaseFunctions();

    public AuthDAODatabase() throws DataAccessException {
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
            daoFunctions.executeUpdate(statement, authToken, username);
            return authToken;
        } catch (Exception e) {
            throw new DataAccessException(e.toString());
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
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
            throw new DataAccessException(e.toString());
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {
        var statement = "DELETE FROM auth WHERE authToken=?";
        daoFunctions.executeUpdate(statement, authToken);
    }

    @Override
    public void deleteAllAuths() {
        var statement = "TRUNCATE auth";
        daoFunctions.executeUpdate(statement);
    }
}
