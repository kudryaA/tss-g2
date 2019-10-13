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
  private int rating;

  /**
   * Constructor.
   *
   * @param connection connection to database
   * @param name recipe name
   * @param cookingSteps recipe cooking steps
   * @param publicationDate recipe publication date
   * @param rating recipe rating
   */
  public PostgresAddRecipe(Connection connection, String name, String cookingSteps,
                           Date publicationDate, int rating) {
    this.connection = connection;
    this.name = name;
    this.cookingSteps = cookingSteps;
    this.publicationDate = publicationDate;
    this.rating = rating;
  }

  /**
   * Method for adding new recipe.
   *
   * @return result of adding new recipe
   */
  public boolean addRecipe() {
    boolean result = false;
    PreparedStatement statement = null;

    try {
      statement = connection
              .prepareStatement("insert into recipe values (nextval('recipeSeq'), ?, ?, ?, ?)");
      statement.setString(1, name);
      statement.setString(2, cookingSteps);
      statement.setDate(3, new java.sql.Date(publicationDate.getTime()));
      statement.setInt(4, rating);
      result = statement.executeUpdate() == 1;
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

    return result;
  }
}
