package dataAccess;

import dataAccess.DAOInterfaces.UserDAO;
import model.UserData;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class UserDAODatabase implements UserDAO {

    public UserDAODatabase() throws DataAccessException {
        DAODatabaseFunctions daoFunctions = new DAODatabaseFunctions();
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
        executeUpdate(statement, username, password, email);
    }

    @Override
    public void deleteAllUsers() {
        var statement = "TRUNCATE user";
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
//            throw new DataAccessException(e.toString());
//        }
//    }
}
