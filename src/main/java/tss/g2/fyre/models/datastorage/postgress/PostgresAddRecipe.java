package tss.g2.fyre.models.datastorage.postgress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Class for adding recipe.
 *
 * @author Andrey Sherstyuk
 */
public class PostgresAddRecipe {
  private Connection connection;
  private String name;
  private String recipeComposition;
  private String cookingSteps;
  private Date publicationDate;
  List<String> selectedTypes;

  /**
   * Constructor.
   *
   * @param connection connection to database
   * @param name recipe name
   * @param recipeComposition composition of the recipe
   * @param cookingSteps recipe cooking steps
   * @param publicationDate recipe publication date
   * @param selectedTypes list with types that the moderator selects
   */
  public PostgresAddRecipe(Connection connection, String name, String recipeComposition,
                           String cookingSteps, Date publicationDate, List<String> selectedTypes) {
    this.connection = connection;
    this.name = name;
    this.recipeComposition = recipeComposition;
    this.cookingSteps = cookingSteps;
    this.publicationDate = publicationDate;
    this.selectedTypes = selectedTypes;
  }

  /**
   * Method for adding new recipe.
   *
   * @return result of adding new recipe
   */
  public boolean addRecipe() {
    boolean result = false;

    try (PreparedStatement statement = connection
            .prepareStatement("insert into recipe values (?, ?, ?, ?, 0)")) {
      statement.setString(1, name);
      statement.setString(2, recipeComposition);
      statement.setString(3, cookingSteps);
      statement.setDate(4, new java.sql.Date(publicationDate.getTime()));

      if (statement.executeUpdate() == 1) {
        int i = 0;

        for (String type : selectedTypes) {
          try (PreparedStatement addRelationStatement =
                       connection.prepareStatement("insert into recipetype values (?, ?)")) {
            addRelationStatement.setString(1, name);
            addRelationStatement.setString(2, type);

            if (addRelationStatement.executeUpdate() == 1) {
              i++;
            }
          }
        }

        if (i == selectedTypes.size()) {
          result = true;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return result;
  }
}
