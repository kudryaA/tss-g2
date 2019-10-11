package tss.g2.fyre.models.datastorage.postgress;

import tss.g2.fyre.models.datastorage.DataStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgresRegistration {
    private Connection connection;
    private String login;
    private String password;
    private String name;
    private String surname;
    private String email;

    public PostgresRegistration(Connection connection, String login, String password, String name, String surname, String email) {
        this.connection = connection;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    /**
     * Method for get authorization info
     *
     * @return authorization
     */
    boolean createNewPerson() {
        boolean answer = false;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM person WHERE login = ?");
            statement.setString(1, login);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return answer;
            } else {
                statement = connection.prepareStatement("insert into person values(?, ?, ?, ?, false, ?)");
                statement.setString(1, login);
                statement.setString(2, password);
                statement.setString(3, name);
                statement.setString(4, surname);
                statement.setString(5, email);
                answer = statement.executeUpdate() == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return answer;
    }
}
