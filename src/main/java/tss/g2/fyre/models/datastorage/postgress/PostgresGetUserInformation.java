package tss.g2.fyre.models.datastorage.postgress;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tss.g2.fyre.models.entity.Person;

public class PostgresGetUserInformation {

  private Connection connection;

  /**
   * Constructor.
   *
   * @param connection connection to database
   */
  public PostgresGetUserInformation(Connection connection) {
    this.connection = connection;
  }

  /**
   * Method for select users information.
   * @return list with users info
   */
  public List<Person> getUsersInformation() {
    List<Person> personsInfo = new ArrayList<>();

    try (Statement selectStatement = connection.createStatement()) {
      try (ResultSet resultSet = selectStatement
              .executeQuery("select login, name, surname, bannedstatus, email from person")) {
        while (resultSet.next()) {
          String login = resultSet.getString(1);
          String name = resultSet.getString(2);
          String surname = resultSet.getString(3);
          boolean bannedStatus = resultSet.getBoolean(4);
          String email = resultSet.getString(5);

          Person person = new Person(login, name, surname, bannedStatus, email);
          personsInfo.add(person);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return personsInfo;
  }
}
