package tss.g2.fyre.test.models.datastorage.postgress;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.recipe.Recipe;
import tss.g2.fyre.models.entity.recipe.RecipeWithType;
import tss.g2.fyre.utils.Configuration;
import tss.g2.fyre.utils.DateConverter;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GetRecipeTestPostgres {
    private static Timestamp date;
    private static Properties properties = new Configuration("config/configuration.yml").getProperties();
    private static String host = properties.getProperty("database_host");
    private static String port = properties.getProperty("database_port");
    private static String database = properties.getProperty("database_database");
    private static String user = properties.getProperty("database_user");
    private static String password = properties.getProperty("database_password");

    @Before
    public void init() throws ParseException {
        date = new Timestamp((new DateConverter("15/10/2019 14:54:30").date()).getTime());
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO type (name, description, image) " +
                            "VALUES ('test_type1', 'test_type1', 'test_type1')," +
                            "('test_type2', 'test_type2', 'test_type2')"))  {
                statement.executeQuery();
            }
        } catch (SQLException e) {
        }
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipe (recipe_id, name, recipeComposition, cookingSteps, publicationDate, image, creator, rating, isConfirmed) " +
                            "VALUES ('test_id', 'test_recipe', 'composition', 'steps', ?, 'image'," +
                            "'julia', 178, true)")) {
                statement.setTimestamp(1, date);
                statement.executeQuery();
            }
        } catch (SQLException e) {
        }
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipeType (recipe_id, type_name) " +
                            "VALUES ('test_id', 'test_type1')," +
                            "('test_id', 'test_type2')"))  {
                statement.executeQuery();
            }

        } catch (SQLException e) {
        }

    }

    @Test
    public void testGetRecipe() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        Recipe res = dataStorage.getRecipe("test_id");
        RecipeWithType result = (RecipeWithType) res;
        System.out.println(res);
        System.out.println(result);
        Assert.assertEquals(res, result);
        Assert.assertEquals("test_id", result.getId());
        Assert.assertEquals("test_recipe", result.getName());
        Assert.assertEquals("composition", result.getComposition());
        Assert.assertEquals("steps", result.getCookingSteps());
        Assert.assertEquals(date, result.getPublicationDate());
        Assert.assertEquals("image", result.getImage());
        Assert.assertEquals("julia", result.getCreator());
        Assert.assertEquals(178+1, result.getRating());
        List<String> types = new ArrayList();
        types.add("test_type1");
        types.add("test_type2");
        Assert.assertEquals(types, result.getTypes());
        Recipe result2 = dataStorage.getRecipe("test_id");
        Assert.assertEquals(result.getRating()+1, result2.getRating());
        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipeType WHERE recipe_id = 'test_id'")) {
                statement.executeQuery();
            }
        } catch (SQLException e) {
        }
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipe WHERE recipe_id = 'test_id'")) {
                statement.executeQuery();
            }
        } catch (SQLException e) {
        }
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM type WHERE name in ('test_type1', 'test_type2')")) {
                //statement.setString(1, recipeId);
                statement.executeQuery();
            }
        } catch (SQLException e) {
        }
    }
}
