package tss.g2.fyre.test.models.actions.simple;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.simple.SelectUsersRating;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.UserRating;
import tss.g2.fyre.utils.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class SelectUserRatingTest {
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
                            "VALUES ('test_user1', 'a', 'john', " +
                            "'doe', false, 'john@doe.com', 'user'), " +
                            "('test_user2', 'b', 'john'," +
                            "'doe', false, 'john@doe.com', 'admin')," +
                            "('test_user3', 'c', 'john'," +
                            "'doe', true, 'john@doe.com', 'user')")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO users_rating (user_login, rating) values ('test_user1', 3), ('test_user2', 2), ('test_user3', 1)")) {
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSelectUsersRating() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        SelectUsersRating rating = new SelectUsersRating(dataStorage);
        Answer answer = rating.getAnswer();
        UserRating user1 = new UserRating("test_user1", (long)3);
        UserRating user2 = new UserRating("test_user2", (long)2);
        UserRating user3 = new UserRating("test_user3", (long)1);
        ArrayList<UserRating> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        Answer answer1 = new Answer<>(true, list);
        Assert.assertEquals(answer1.isStatus(), answer.isStatus());
        List<UserRating> list1 = (List<UserRating>) answer.getObj();
        boolean result = list1.contains(user1) && list1.contains(user2);
        Assert.assertTrue(result);
        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement1 = connection.prepareStatement(
                    "DELETE FROM users_rating WHERE user_login in ('test_user1', 'test_user2', 'test_user3')")) {
                statement1.execute();
            }
            try (PreparedStatement statement1 = connection.prepareStatement(
                    "DELETE FROM person where login in ('test_user1', 'test_user2', 'test_user3')")) {
                statement1.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
