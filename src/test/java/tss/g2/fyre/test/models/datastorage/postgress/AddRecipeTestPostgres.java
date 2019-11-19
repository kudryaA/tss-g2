package tss.g2.fyre.test.models.datastorage.postgress;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.utils.Configuration;
import tss.g2.fyre.utils.DateConverter;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddRecipeTestPostgres {
    private static Timestamp date;
    private static String recipeId;
    private static Properties properties = new Configuration("config/configuration.yml").getProperties();
    private static String host = properties.getProperty("database_host");
    private static String port = properties.getProperty("database_port");
    private static String database = properties.getProperty("database_database");
    private static String user = properties.getProperty("database_user");
    private static String password = properties.getProperty("database_password");

    @Before
    public void init() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO type (name, description, image) " +
                            "VALUES ('test_type1', 'test_type1', 'test_type1')," +
                            "('test_type2', 'test_type2', 'test_type2')"))  {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddRecipe() throws SQLException, ParseException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        date = new Timestamp((new DateConverter("15/10/2019 14:54:30").date()).getTime());
        List <String> list = new ArrayList<>();
        list.add("test_type1");
        list.add("test_type2");
        recipeId  = dataStorage.addRecipe("Test_recipe", "Test_composition",
               "Test_cooking_steps", date, list, "Test_image", "Test_user", true);
        try(Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM recipe WHERE recipe_id = ?")){
                statement.setString(1, recipeId);
                try (ResultSet resultSet = statement.executeQuery()){
                    if (resultSet.next()) {
                        String recipe = resultSet.getString("recipe_id");
                        String recipeName = resultSet.getString("name");
                        String recipeComposition = resultSet.getString("recipeComposition");
                        String cookingSteps = resultSet.getString("cookingSteps");
                        Timestamp publicationDate = resultSet.getTimestamp("publicationDate");
                        String image = resultSet.getString("image");
                        String creator = resultSet.getString("creator");
                        boolean isConfirmed = resultSet.getBoolean("isConfirmed");
                        Assert.assertEquals(recipeId,  recipe);
                        Assert.assertEquals("Test_recipe", recipeName);
                        Assert.assertEquals("Test_composition", recipeComposition);
                        Assert.assertEquals("Test_cooking_steps", cookingSteps);
                        Assert.assertEquals(date, publicationDate);
                        Assert.assertEquals("Test_image", image);
                        Assert.assertEquals("Test_user", creator);
                        Assert.assertEquals(true, isConfirmed);
                    }
                }
            }
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM recipeType where recipe_id = ?")){
                statement.setString(1, recipeId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    List <String> type = new ArrayList<>();
                    while (resultSet.next()) {
                        String typeName = resultSet.getString("type_name");
                        type.add(typeName);
                    }
                    Assert.assertEquals(list, type);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipeType WHERE recipe_id = ?")) {
                statement.setString(1, recipeId);
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM type WHERE name in ('test_type1', 'test_type2')")) {
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipe WHERE recipe_id = ?")) {
                statement.setString(1, recipeId);
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
