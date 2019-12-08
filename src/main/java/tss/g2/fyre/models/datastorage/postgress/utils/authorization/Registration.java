package tss.g2.fyre.models.datastorage.postgress.utils.authorization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for registration users.
 *
 * @author Andrey Sherstyuk
 */
public class Registration {
  private static Logger logger = LoggerFactory.getLogger(Registration.class);

  private Connection connection;
  private String login;
  private String password;
  private String name;
  private String surname;
  private String email;

  /**
   * Constructor.
   *
   * @param connection connection to database
   * @param login user login
   * @param password user password
   * @param name user name
   * @param surname user surname
   * @param email user email
   */
  public Registration(Connection connection, String login, String password,
                      String name, String surname, String email) {
    this.connection = connection;
    this.login = login;
    this.password = password;
    this.name = name;
    this.surname = surname;
    this.email = email;
  }

  /**
    * Method for registration person.
    *
    * @return result of adding person
    */
  public boolean createUser() {
    boolean answer = false;
    try (PreparedStatement statement
                 = connection.prepareStatement("SELECT * FROM person WHERE login = ?")) {
      statement.setString(1, login);
      logger.info(statement.toString());
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return false;
        } else {
          try (PreparedStatement registrationStatement = connection
                  .prepareStatement("insert into person values(?, ?, ?, ?, false, ?, 'user')")) {
            registrationStatement.setString(1, login);
            registrationStatement.setString(2, password);
            registrationStatement.setString(3, name);
            registrationStatement.setString(4, surname);
            registrationStatement.setString(5, email);
            logger.info(registrationStatement.toString());
            answer = registrationStatement.executeUpdate() == 1;
          }
          try (PreparedStatement insertStatement = connection
                  .prepareStatement("insert into users_rating values(?, 0)")) {
            insertStatement.setString(1, login);

            logger.info(insertStatement.toString());
            insertStatement.executeUpdate();
          }
        }
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
    }
    return answer;
  }
}
