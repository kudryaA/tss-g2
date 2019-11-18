package tss.g2.fyre.test.models.datastorage.postgress;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.utils.Configuration;
import tss.g2.fyre.utils.DateConverter;

import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.util.Properties;

public class UpdateRecipeTestPostgres {
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
                    "INSERT INTO person (login, password, name, surname, bannedstatus, email, role) " +
                            "VALUES ('john_test_1', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'john', " +
                            "'doe', false, 'john@doe.com', 'admin')")) {
                statement.executeQuery();
            }
        } catch (SQLException e) {
        }
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipe (recipe_id, name, recipeComposition, cookingSteps, publicationDate, image, creator, rating, isConfirmed) " +
                            "VALUES ('test_id1', 'test_recipe1', 'composition', 'steps', ?, 'image_com', 'julia', 178, true)," +
                            "('test_id2', 'test_recipe2', 'composition', 'steps', ?, 'image_com', 'john', 178, true)")) {
                statement.setTimestamp(1, date);
                statement.setTimestamp(2, date);
                statement.executeQuery();
            }
        } catch (SQLException e) {
        }
    }

    @Test
    public void testUpdateRecipe() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        boolean result1 = dataStorage.updateRecipe("test_id1", "test_recipe_new", "composition_new", "steps", "julia");
        Assert.assertEquals(true, result1);
        try(Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM recipe WHERE recipe_id = 'test_id1'")){
                try (ResultSet resultSet = statement.executeQuery()){
                    if (resultSet.next()) {
                        String recipeId = resultSet.getString("recipe_id");
                        String recipeName = resultSet.getString("name");
                        String recipeComposition = resultSet.getString("recipeComposition");
                        String cookingSteps = resultSet.getString("cookingSteps");
                        Timestamp publicationDate = resultSet.getTimestamp("publicationDate");
                        String image = resultSet.getString("image");
                        String creator = resultSet.getString("creator");
                        BigDecimal rating = resultSet.getBigDecimal("rating");
                        boolean isConfirmed = resultSet.getBoolean("isConfirmed");
                        BigDecimal rat = new BigDecimal(178);
                        Assert.assertEquals("test_id1",  recipeId);
                        Assert.assertEquals("test_recipe_new", recipeName);
                        Assert.assertEquals("composition_new", recipeComposition);
                        Assert.assertEquals("steps", cookingSteps);
                        Assert.assertEquals(date, publicationDate);
                        Assert.assertEquals("image_com", image);
                        Assert.assertEquals("julia", creator);
                        Assert.assertEquals(rat, rating);
                        Assert.assertEquals(true, isConfirmed);

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        boolean result2 = dataStorage.updateRecipe("test_id2", "recipe_new", "new_composition", "new_steps", "john_test_1");
        Assert.assertEquals(true, result2);
        try(Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM recipe WHERE recipe_id = 'test_id2'")){
                try (ResultSet resultSet = statement.executeQuery()){
                    if (resultSet.next()) {
                        String recipeId = resultSet.getString("recipe_id");
                        String recipeName = resultSet.getString("name");
                        String recipeComposition = resultSet.getString("recipeComposition");
                        String cookingSteps = resultSet.getString("cookingSteps");
                        Timestamp publicationDate = resultSet.getTimestamp("publicationDate");
                        String image = resultSet.getString("image");
                        String creator = resultSet.getString("creator");
                        BigDecimal rating = resultSet.getBigDecimal("rating");
                        boolean isConfirmed = resultSet.getBoolean("isConfirmed");

                        BigDecimal rat = new BigDecimal(178);
                        Assert.assertEquals("test_id2",  recipeId);
                        Assert.assertEquals("recipe_new", recipeName);
                        Assert.assertEquals("new_composition", recipeComposition);
                        Assert.assertEquals("new_steps", cookingSteps);
                        Assert.assertEquals(date, publicationDate);
                        Assert.assertEquals("image_com", image);
                        Assert.assertEquals("john", creator);
                        Assert.assertEquals(rat, rating);
                        Assert.assertEquals(true, isConfirmed);
                    }
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
                    "DELETE FROM recipe WHERE recipe_id in ('test_id1', 'test_id2')")) {
                statement.executeQuery();
            }
        } catch (SQLException e) {
        }
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM person WHERE login = 'john_test_1'")) {
                statement.executeQuery();
            }
        } catch (SQLException e) {
        }
    }
}
