package tss.g2.fyre.models.datastorage.postgress.utils.recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tss.g2.fyre.utils.RandomString;

/**
 * Class for adding recipe.
 *
 * @author Andrey Sherstyuk
 */
public class AddRecipe {
  private static Logger logger = LoggerFactory.getLogger(AddRecipe.class);

  private Connection connection;
  private String name;
  private String recipeComposition;
  private String cookingSteps;
  private String image;
  private Date publicationDate;
  private List<String> selectedTypes;
  private String user;
  private boolean isConfirmed;

  /**
   * Constructor.
   *
   * @param connection connection to database
   * @param name recipe name
   * @param recipeComposition composition of the recipe
   * @param cookingSteps recipe cooking steps
   * @param publicationDate recipe publication date
   * @param selectedTypes list with types that the moderator selects
   * @param image image of recipe
   * @param user owner of recipe
   * @param isConfirmed is the recipe confirmed
   */
  public AddRecipe(Connection connection, String name, String recipeComposition,
                   String cookingSteps, Date publicationDate, List<String> selectedTypes,
                   String image, String user, boolean isConfirmed) {
    this.connection = connection;
    this.name = name;
    this.recipeComposition = recipeComposition;
    this.cookingSteps = cookingSteps;
    this.publicationDate = publicationDate;
    this.selectedTypes = selectedTypes;
    this.image = image;
    this.user = user;
    this.isConfirmed = isConfirmed;
  }

  /**
   * Method for adding new recipe.
   *
   * @return result of adding new recipe
   */
  public String addRecipe() {
    String result = "";
    String key = new RandomString(20).generate() + new Date().getTime();
    logger.info("Start add recipe recipe id = {}.", key);

    List<String> list = new ArrayList<>();
    StringBuilder request = new StringBuilder("select name from type where name in (");

    IntStream.range(0, selectedTypes.size()).forEach(type -> request.append("?, "));
    request.delete(request.length() - 2, request.length());
    request.append(")");

    try (PreparedStatement checkStatement = connection.prepareStatement(request.toString())) {
      for (int i = 1; i <= selectedTypes.size(); i++) {
        checkStatement.setString(i, selectedTypes.get(i - 1));
      }
      logger.info("Start executing statement for check types - {}.",
              checkStatement);

      try (ResultSet resultSet = checkStatement.executeQuery()) {
        logger.info("Executing statement for check types complete successfully - {}",
                checkStatement);
        while (resultSet.next()) {
          list.add(resultSet.getString("name"));
        }
      }
    } catch (SQLException e) {
      logger.error("Executing statement for check types complete with errors - {}",
              e.getMessage());
    }

    try (PreparedStatement statement = connection
            .prepareStatement("insert into recipe values "
                    + "(?, ?, ?, ?, ?, ?, ?, " + -9223372036854775808L + ", ?)")) {
      statement.setString(1, key);
      statement.setString(2, name);
      statement.setString(3, recipeComposition);
      statement.setString(4, cookingSteps);
      statement.setTimestamp(5, new Timestamp(publicationDate.getTime()));
      statement.setString(6, image);
      statement.setString(7, user);
      statement.setBoolean(8, isConfirmed);

      logger.info("Executing statement for add recipe to database recipe started - {}",
              statement);
      if (statement.executeUpdate() == 1) {

        logger.info("Executing statement for add recipe to database recipe "
                + "complete successfully - {}", statement);
        int i = 0;

        for (String type : list) {
          try (PreparedStatement addRelationStatement =
                       connection.prepareStatement("insert into recipetype values (?, ?)")) {
            addRelationStatement.setString(1, key);
            addRelationStatement.setString(2, type);

            logger.info("Executing statement for add type to database recipetype started - {}",
                    addRelationStatement);
            if (addRelationStatement.executeUpdate() == 1) {
              logger.info("Executing statement for add type to database recipetype "
                      + "complete successfully - {}", addRelationStatement);
              i++;
            } else {
              logger.error("Executing statement for add type to database recipetype "
                      + "complete with error - {}", addRelationStatement);
            }
          }
        }

        if (i == list.size()) {
          logger.info("Execute adding recipe recipe id = {} complete successfully.", key);
          result += key;
        } else {
          logger.error("Execute adding recipe recipe id = {} complete with some error.", key);
        }
      } else {
        logger.error("Executing statement for add recipe to database complete with error - {}",
                statement);
      }
    } catch (SQLException e) {
      logger.error("Executing statement for add recipe to database complete with errors - {}",
              e.getMessage());
    }

    return result;
  }
}
