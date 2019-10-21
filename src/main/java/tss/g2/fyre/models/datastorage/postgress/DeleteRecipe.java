package tss.g2.fyre.models.datastorage.postgress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class for delete recipe.
 *
 * @author Andrey Sherstyuk
 */
public class DeleteRecipe {
  private Connection connection;
  private String name;

  /**
   * Constructor for delete recipe.
   *
   * @param connection connection to database
   * @param name recipe name
   */
  public DeleteRecipe(Connection connection, String name) {
    this.connection = connection;
    this.name = name;
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
