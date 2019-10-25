package tss.g2.fyre.models.datastorage.postgress;

import java.sql.*;

import java.util.Date;
import java.util.List;

/**
 * Class for adding recipe.
 *
 * @author Andrey Sherstyuk
 */
class AddRecipe {
  private Connection connection;
  private String name;
  private String recipeComposition;
  private String cookingSteps;
  private String image;
  private Date publicationDate;
  private List<String> selectedTypes;
  private String user;

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
   */
  public AddRecipe(Connection connection, String name, String recipeComposition,
                   String cookingSteps, Date publicationDate,
                   List<String> selectedTypes, String image, String user) {
    this.connection = connection;
    this.name = name;
    this.recipeComposition = recipeComposition;
    this.cookingSteps = cookingSteps;
    this.publicationDate = publicationDate;
    this.selectedTypes = selectedTypes;
    this.image = image;
    this.user = user;
  }

  /**
   * Method for adding new recipe.
   *
   * @return result of adding new recipe
   */
  boolean addRecipe() {
    boolean result = false;

    try (Statement createIdStatement = connection.createStatement()) {
      try (ResultSet resultSet = createIdStatement.executeQuery("select nextval('recipeseq')")) {
        if (resultSet.next()) {
          int seqValue = resultSet.getInt(1);

          try (PreparedStatement statement = connection
                  .prepareStatement("insert into recipe values "
                          + "(?, ?, ?, ?, ?, ?, ?, " + -9223372036854775808L + ")")) {
            statement.setInt(1, seqValue);
            statement.setString(2, name);
            statement.setString(3, recipeComposition);
            statement.setString(4, cookingSteps);
            statement.setTimestamp(5, new Timestamp(publicationDate.getTime()));
            statement.setString(6, image);
            statement.setString(7, user);

            if (statement.executeUpdate() == 1) {
              int i = 0;

              for (String type : selectedTypes) {
                try (PreparedStatement addRelationStatement =
                             connection.prepareStatement("insert into recipetype values (?, ?)")) {
                  addRelationStatement.setInt(1, seqValue);
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
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return result;
  }
}
