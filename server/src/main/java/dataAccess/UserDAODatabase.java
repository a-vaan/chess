package dataAccess;

import dataAccess.DAOInterfaces.UserDAO;
import model.UserData;

public class UserDAODatabase implements UserDAO {

    private final DAODatabaseFunctions daoFunctions = new DAODatabaseFunctions();

    public UserDAODatabase() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(45) NOT NULL,
              `password` varchar(254) NOT NULL,
              `email` varchar(45) NOT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """
        };
        daoFunctions.configureDatabase(createStatements);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new UserData(rs.getString(1), rs.getString(2), rs.getString(3));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.toString());
        }
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        daoFunctions.executeUpdate(statement, username, password, email);
    }

    @Override
    public void deleteAllUsers() {
        var statement = "TRUNCATE user";
        daoFunctions.executeUpdate(statement);
    }
}
