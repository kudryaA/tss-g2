package tss.g2.fyre.test.models.actions.auth.check;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.auth.AddType;
import tss.g2.fyre.models.actions.auth.DeleteRecipe;
import tss.g2.fyre.models.actions.auth.GetUsers;
import tss.g2.fyre.models.actions.auth.check.AuthUser;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Authorization;
import tss.g2.fyre.models.entity.Roles;
import tss.g2.fyre.utils.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AuthUserTest {
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
                    "insert into recipe values ('test_recipe_id', 'test_recipe', 'some recipe composition', " +
                            "'some cooking steps', current_timestamp, 'unnamed', 'john_test_1', 1, true )")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO person (login, password, name, surname, bannedstatus, email, role) " +
                            "VALUES ('john_test_1', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'john', " +
                            "'doe', false, 'john@doe.com', 'admin')")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void authUserTest() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        Map<String, Authorization> tokenStorage = new HashMap<>();
        tokenStorage.put("some_token", new Authorization("john_test_1", Roles.admin.toString()));
        Answer answer = new AuthUser(new DeleteRecipe(dataStorage, "test_recipe_id"), "some_token", tokenStorage).getAnswer();

        Assert.assertEquals(new Answer<>(true, true), answer);
        answer = new AuthUser(new DeleteRecipe(dataStorage, "test_recipe_id"), "some_wrong_token", tokenStorage).getAnswer();
        Assert.assertEquals(new Answer<>(false), answer);
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM person WHERE login = 'john_test_1'")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
        }
    }
}
