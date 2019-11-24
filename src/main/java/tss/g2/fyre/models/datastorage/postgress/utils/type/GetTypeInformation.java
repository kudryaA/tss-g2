package tss.g2.fyre.models.datastorage.postgress.utils.type;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tss.g2.fyre.models.entity.Type;

/**
 * Class for get type information.
 */
public class GetTypeInformation {
  private static Logger logger = LoggerFactory.getLogger(GetTypeInformation.class);

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
        logger.info(selectStatement.toString());
        while (resultSet.next()) {
          String typeName = resultSet.getString("name");
          String description = resultSet.getString("description");
          String image = resultSet.getString("image");
          typesInfo.add(new Type(typeName, description, image));
        }
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
    }
    return typesInfo;
  }
}
