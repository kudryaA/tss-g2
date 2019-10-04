package g2.tss.fyre.models.datastorage.postgress;

import g2.tss.fyre.models.entity.Authorization;

import java.sql.*;

/**
 * CLass for get authorization from postgres
 * @author ANton Kudryavtsev
 */
class PostgresGetAuthorization {

    private Connection connection;
    private String login;

    /**
     * Constructor
     * @param connection postgres jdbc connection
     * @param login for authorization
     */
    PostgresGetAuthorization(Connection connection, String login) {
        this.connection = connection;
    }

    /**
     * Method for get authorization info
     * @return authorization
     */
    Authorization getAuthorization() {
        Authorization result = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM authorization WHERE login = ?")){
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String password = resultSet.getString("password");
                result = new Authorization(login, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
