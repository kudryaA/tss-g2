package tss.g2.fyre.models.datastorage.postgress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Date;

public class PostgresAddRecipe {
  private Connection connection;
  private String name;
  private String cookingSteps;
  private Date publicationDate;

  /**
   * Constructor.
   *
   * @param connection connection to database
   * @param name recipe name
   * @param cookingSteps recipe cooking steps
   * @param publicationDate recipe publication date
   */
  public PostgresAddRecipe(Connection connection, String name, String cookingSteps,
                           Date publicationDate) {
    this.connection = connection;
    this.name = name;
    this.cookingSteps = cookingSteps;
    this.publicationDate = publicationDate;
  }

  /**
   * Method for adding new recipe.
   *
   * @return result of adding new recipe
   */
  public boolean addRecipe() {
    boolean result = false;

    try (PreparedStatement statement = connection
            .prepareStatement("insert into recipe values (nextval('recipeSeq'), ?, ?, ?, 0)")) {
      statement.setString(1, name);
      statement.setString(2, cookingSteps);
      statement.setDate(3, new java.sql.Date(publicationDate.getTime()));
      result = statement.executeUpdate() == 1;
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return result;
  }
}
