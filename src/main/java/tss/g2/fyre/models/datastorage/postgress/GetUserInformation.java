package tss.g2.fyre.models.datastorage.postgress;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tss.g2.fyre.models.entity.Person;

class GetUserInformation {
  private Logger getUserInformationLogger = LoggerFactory.getLogger(GetUserInformation.class);

  private Connection connection;

  /**
   * Constructor.
   *
   * @param connection connection to database
   */
  GetUserInformation(Connection connection) {
    this.connection = connection;
  }

  /**
   * Method for select users information.
   * @return list with users info
   */
  List<Person> getUsersInformation() {
    List<Person> personsInfo = new ArrayList<>();

    try (Statement selectStatement = connection.createStatement()) {
      try (ResultSet resultSet = selectStatement
              .executeQuery("select login, name, surname, bannedstatus, email, role from person")) {
        getUserInformationLogger.info(selectStatement.toString());
        while (resultSet.next()) {
          String login = resultSet.getString(1);
          String name = resultSet.getString(2);
          String surname = resultSet.getString(3);
          boolean bannedStatus = resultSet.getBoolean(4);
          String email = resultSet.getString(5);
          String role = resultSet.getString(6);

          Person person = new Person(login, name, surname, bannedStatus, email, role);
          personsInfo.add(person);
        }
      }
    } catch (SQLException e) {
      getUserInformationLogger.error(e.getMessage());
    }

    return personsInfo;
  }
}
