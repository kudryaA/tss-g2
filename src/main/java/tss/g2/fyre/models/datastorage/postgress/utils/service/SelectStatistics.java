package tss.g2.fyre.models.datastorage.postgress.utils.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tss.g2.fyre.models.entity.Statistic;

public class SelectStatistics {
  private Connection connection;

  /**
   * Constructor.
   * @param connection connection to database
   */
  public SelectStatistics(Connection connection) {
    this.connection = connection;
  }

  /**
   * Method for select time statistics.
   * @return time statistics
   */
  public List<Statistic> selectStatistics() {
    List<Statistic> statistics = new ArrayList<>();

    try (Statement selectStatement = connection.createStatement()) {
      try (ResultSet resultSet = selectStatement
              .executeQuery("SELECT api, AVG(time_execution) as avg, "
              + "MIN(time_execution) as min, MAX(time_execution) as max, COUNT(*) as count "
              + "FROM api_time GROUP BY api")) {
        while (resultSet.next()) {
          statistics.add(new Statistic(resultSet.getString("api"),
                                     resultSet.getDouble("avg"),
                                     resultSet.getInt("min"),
                                     resultSet.getLong("max"),
                                     resultSet.getLong("count")));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return statistics;
  }
}
