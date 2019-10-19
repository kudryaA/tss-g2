package tss.g2.fyre.models.datastorage.postgress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Date;
import java.util.List;

/**
 * Class for adding recipe.
 *
 * @author Andrey Sherstyuk
 */
public class PostgresActionWithRecipe {
  private Connection connection;
  private String name;
  private String recipeComposition;
  private String cookingSteps;
  private Date publicationDate;
  private List<String> selectedTypes;

  /**
   * Constructor for delete recipe.
   *
   * @param connection connection to database
   * @param name recipe name
   */
  public PostgresActionWithRecipe(Connection connection, String name) {
    this.connection = connection;
    this.name = name;
  }

  /**
   * Constructor for add recipe.
   *
   * @param connection connection to database
   * @param name recipe name
   * @param recipeComposition composition of the recipe
   * @param cookingSteps recipe cooking steps
   * @param publicationDate recipe publication date
   * @param selectedTypes list with types that the moderator selects
   */
  public PostgresActionWithRecipe(Connection connection, String name,
                                  String recipeComposition, String cookingSteps,
                                  Date publicationDate, List<String> selectedTypes) {
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

  /**
   * Method for removing the recipe.
   *
   * @return result of deleting
   */
  public boolean deleteRecipe() {
    boolean result = false;

    try (PreparedStatement deleteRelationStatement =
                 connection.prepareStatement("delete from recipetype where recipe_name = ?")) {
      deleteRelationStatement.setString(1, name);
      deleteRelationStatement.executeUpdate();

      try (PreparedStatement deleteRecipeStatement =
                   connection.prepareStatement("delete from recipe where name = ?")) {
        deleteRecipeStatement.setString(1, name);
        result = deleteRecipeStatement.executeUpdate() == 1;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return result;
  }
}
