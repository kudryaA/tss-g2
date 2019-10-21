package tss.g2.fyre.models.datastorage.postgress;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tss.g2.fyre.models.entity.Type;

public class GetTypeInformation {

  private Connection connection;

  /**
   * Constructor.
   *
   * @param connection connection to database
   */
  public GetTypeInformation(Connection connection) {
    this.connection = connection;
  }

  /**
   * Method for select types information.
   *
   * @return list with types info
   */
  public List<Type> getTypesInformation() {
    List<Type> typesInfo = new ArrayList<>();

    try (Statement selectStatement = connection.createStatement()) {
      try (ResultSet resultSet = selectStatement.executeQuery("select * from type")) {
        while (resultSet.next()) {
          String typeName = resultSet.getString(1);
          String description = resultSet.getString(2);

          typesInfo.add(new Type(typeName, description));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return typesInfo;
  }
}
