package tss.g2.fyre.test.models.datastorage.postgress;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.utils.Configuration;

import java.sql.*;
import java.util.Properties;

public class RecipeConfirmationTestPostgres {
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
                    "INSERT INTO recipe (recipe_id, name, recipeComposition, cookingSteps, publicationDate, image, creator, rating, isConfirmed) " +
                            "VALUES ('test_id1', 'test_recipe1', 'composition', 'steps', timestamp '2001-09-28 01:00', 'image_com', 'julia', 178, true)," +
                            "('test_id2', 'test_recipe2', 'composition', 'steps', timestamp '2001-09-28 01:00', 'image_com', 'john', 178, false)")) {
                statement.executeQuery();
            }
        } catch (SQLException e) {
        }
    }

    @Test
    public void testRecipeConfirmation() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        boolean result1 = dataStorage.recipeConfirmation("test_id1");
        try(Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM recipe WHERE recipe_id = 'test_id1'")){
                try (ResultSet resultSet = statement.executeQuery()){
                    if (resultSet.next()) {
                        boolean isConfirmed = resultSet.getBoolean("isConfirmed");
                        Assert.assertEquals(true, isConfirmed);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        boolean result2 = dataStorage.recipeConfirmation("test_id2");
        try(Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM recipe WHERE recipe_id = 'test_id2'")){
                try (ResultSet resultSet = statement.executeQuery()){
                    if (resultSet.next()) {
                        boolean isConfirmed = resultSet.getBoolean("isConfirmed");
                        Assert.assertEquals(true, isConfirmed);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(true, result1);
        Assert.assertEquals(true, result2);
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
    }
}
