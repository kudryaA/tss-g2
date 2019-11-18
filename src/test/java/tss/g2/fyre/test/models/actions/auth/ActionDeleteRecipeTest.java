package tss.g2.fyre.test.models.actions.auth;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.AnswerWithComment;
import tss.g2.fyre.models.actions.auth.ChangeBannedStatus;
import tss.g2.fyre.models.actions.auth.DeleteRecipe;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Roles;
import tss.g2.fyre.utils.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class ActionDeleteRecipeTest {
    private final static Properties properties = new Configuration("config/configuration.yml").getProperties();
    private final static String host = properties.getProperty("database_host");
    private final static String port = properties.getProperty("database_port");
    private final static String database = properties.getProperty("database_database");
    private final static String user = properties.getProperty("database_user");
    private final static String password = properties.getProperty("database_password");

    @Before
    public void init() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO person (login, password, name, surname, bannedstatus, email, role) VALUES " +
                            "('john_test_experiencedUser', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'john', " +
                            "'doe', false, 'john@doe.com', 'experiencedUser'), " +
                            "('john_test_admin', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'john', " +
                            "'doe', false, 'john@doe.com', 'admin'), " +
                            "('john_test_user', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'john', " +
                            "'doe', false, 'john@doe.com', 'user')")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
        }
    }

    @Test
    public void actionDeleteRecipeTest() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        DeleteRecipe deleteRecipe = new DeleteRecipe(dataStorage, "some_recipe_id");

        addRecipe();
        Assert.assertEquals(new Answer<>(true, false),
                deleteRecipe.getAnswer("john_test_user", Roles.user.toString()));

        Assert.assertEquals(new Answer<>(true, true),
                deleteRecipe.getAnswer("john_test_admin", Roles.admin.toString()));

        addRecipe();
        Assert.assertEquals(new Answer<>(true, true),
                deleteRecipe.getAnswer("john_test_experiencedUser", Roles.experiencedUser.toString()));

        Assert.assertEquals(new Answer<>(true, false),
                deleteRecipe.getAnswer("john_test_experiencedUser", Roles.experiencedUser.toString()));

        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM person WHERE login in ('john_test_moderator', 'john_test_experiencedUser', 'john_test_user')")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addRecipe() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipe values ('some_recipe_id', 'some_recipe_name', 'some_recipe_composition', 'some_cooking_steps', current_timestamp, 'unnamed', 'john_test_experiencedUser', 1, true)")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
        }
    }
}
