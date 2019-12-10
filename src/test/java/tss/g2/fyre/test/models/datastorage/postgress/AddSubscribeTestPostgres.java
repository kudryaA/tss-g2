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
import java.util.Properties;

public class AddSubscribeTestPostgres {
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
                            "'doe', false, 'john@doe.com', 'user')")) {
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
                            "'test_user1', 178, true)," +
                            "('test_id2', 'test_recipe', 'composition', 'steps', ?, 'image'," +
                            "'test_user1', 178, true)")) {
                statement.setTimestamp(1, date);
                statement.setTimestamp(2, date);
                statement.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testAddSubscribe() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        boolean result = dataStorage.addSubscribe("test_user2", "test_user1");
        Assert.assertEquals(true, result);
        try(Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement("SELECT count(*) n FROM userSubscribe WHERE user_login = 'test_user2' and sub_login = 'test_user1'")){
                try (ResultSet resultSet = statement.executeQuery()){
                    if (resultSet.next()) {
                        Integer n = resultSet.getInt("n");
                        boolean res = false;
                        if(n == 1){
                            res = true;
                        }
                        Assert.assertEquals(true, res);
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
                    "DELETE FROM userSubscribe WHERE user_login = 'test_user2'")) {
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipe WHERE recipe_id in ('test_id', 'test_id2')")) {
                statement.execute();
            }
            try (PreparedStatement statement1 = connection.prepareStatement(
                    "DELETE FROM person where login in ('test_user1', 'test_user2')")) {
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
