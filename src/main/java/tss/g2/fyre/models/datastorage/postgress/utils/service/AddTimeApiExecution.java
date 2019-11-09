package tss.g2.fyre.models.datastorage.postgress.utils.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Store api execution time.
 */
public class AddTimeApiExecution {

  private String api;
  private long time;
  private Connection connection;
  private static Logger logger = LoggerFactory.getLogger(AddTimeApiExecution.class);

  /**
   * Constructor.
   * @param connection connection to database
   * @param api api
   * @param time time
   */
  public AddTimeApiExecution(Connection connection, String api, long time) {
    this.api = api;
    this.time = time;
    this.connection = connection;
  }

  public void add() {
    try (PreparedStatement statement = connection
        .prepareStatement("insert into api_time values (?, ?)")) {
      statement.setString(1, api);
      statement.setLong(2, time);

      logger.info("Execution statement for add api execution time ({}) started", statement);
      if (statement.executeUpdate() == 1) {
        logger.info("Execution statement for add api execution time ({}) finished successfully", statement);
      } else {
        logger.error("Execution statement for add api execution time ({}) finished with error", statement);
      }
    } catch (SQLException e) {
      logger.error("Execution statement for add api execution time are failed with error", e);
    }
  }
}
