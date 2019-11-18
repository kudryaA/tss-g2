package tss.g2.fyre.test.models.datastorage.postgress;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.utils.Configuration;

import java.sql.*;
import java.util.Properties;

public class AddCommentTestPostgres {
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
                    "INSERT INTO person (login, password, name, surname, bannedstatus, email, role) " +
                            "VALUES ('john_comment_1', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb'," +
                            "'john_1', 'doe', false, 'john@doe.com', 'user')")) {
                statement.executeQuery();
            }
        } catch (SQLException e) {
        }
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipe (recipe_id, name, recipeComposition, cookingSteps, publicationDate, image, creator, rating, isConfirmed) " +
                            "VALUES ('test_com_recipe', 'test_com_recipe', 'composition_com', 'steps_com', timestamp '2001-09-28 01:00', 'image_com', 'julia', 178, true)")) {
                statement.executeQuery();
            }
        } catch (SQLException e) {
        }
    }

    @Test
    public void testAddComment() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        boolean result = dataStorage.addComment("john_comment_1", "test_com_recipe", "my_comment");
        Assert.assertEquals(true, result);
        try(Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM comment WHERE user_login = 'john_comment_1' and " +
                    "recipe_id = 'test_com_recipe' and comment_text = 'my_comment'")){
                try (ResultSet resultSet = statement.executeQuery()){
                    if (resultSet.next()) {
                        String userLogin = resultSet.getString("user_login");
                        String recipeId = resultSet.getString("recipe_id");
                        String commentText = resultSet.getString("comment_text");
                        Assert.assertEquals("john_comment_1", userLogin);
                        Assert.assertEquals("test_com_recipe", recipeId);
                        Assert.assertEquals("my_comment", commentText);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(true, result);
        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM comment WHERE user_login = 'john_comment_1' and recipe_id = 'test_com_recipe' and comment_text = 'my_comment'")) {
                statement.executeQuery();
            }
        } catch (SQLException e) {
        }
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM person WHERE login = 'john_comment_1'")) {
                statement.executeQuery();
            }
        } catch (SQLException e) {
        }
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipe WHERE recipe_id = 'test_com_recipe'")) {
                statement.executeQuery();
            }
        } catch (SQLException e) {
        }
    }
}
