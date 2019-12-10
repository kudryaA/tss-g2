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
import java.util.Map;
import java.util.Properties;

public class GetDashboardTestPostgres {
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
                            "VALUES ('test_user1', 'a', 'john', " +
                            "'doe', false, 'john@doe.com', 'user'), " +
                            "('test_user2', 'b', 'john'," +
                            "'doe', false, 'john@doe.com', 'experiencedUser')," +
                            "('test_user3', 'c', 'john'," +
                            "'doe', false, 'john@doe.com', 'moderator')," +
                            "('test_user4', 'c', 'john'," +
                            "'doe', false, 'john@doe.com', 'admin')")) {
                statement.executeUpdate();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO type (name, description, image) " +
                            "VALUES ('type', 'type', 'type')")) {
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipe (recipe_id, name, recipeComposition, cookingSteps, publicationDate, image, creator, rating, isConfirmed) " +
                            "VALUES ('test_id', 'test_recipe', 'composition', 'steps', ?, 'image'," +
                            "'test_user1', 178, true)")) {
                statement.setTimestamp(1, date);
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO comment (user_login, recipe_id, comment_text) " +
                            "VALUES ('test_user1', 'test_id', 'composition')," +
                            "('test_user1', 'test_id', 'composition2')," +
                            "('test_user2', 'test_id', 'composition3')")) {
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO likes (user_login, recipe_id) " +
                            "VALUES ('test_user1', 'test_id')," +
                            "('test_user2', 'test_id')")) {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testGetDashboard() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        Map<String, Integer> dashboard = dataStorage.getDashboard("test_user4");
        Assert.assertTrue(dashboard.get("Users") >= 1);
        Assert.assertTrue(dashboard.get("Experienced users") >= 1);
        Assert.assertTrue(dashboard.get("Moderators") >= 1);
        Assert.assertTrue(dashboard.get("Admins") >= 1);
        Assert.assertTrue(dashboard.get("Comments") >= 3);
        Assert.assertTrue(dashboard.get("Likes") >= 2);
        Assert.assertTrue(dashboard.get("All recipes") >= 1);
        Assert.assertTrue( dashboard.get("Recipes that were added in the last 7 days") >= 0);
        dataStorage.close();
    }


    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM comment WHERE user_login in ('test_user1', 'test_user2')")) {
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM likes WHERE recipe_id = 'test_id'")) {
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipe WHERE recipe_id = 'test_id'")) {
                statement.execute();
            }
            try (PreparedStatement statement1 = connection.prepareStatement(
                    "DELETE FROM person where login in ('test_user1', 'test_user2', 'test_user3', 'test_user4')")) {
                statement1.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM type WHERE name = 'type'")) {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
