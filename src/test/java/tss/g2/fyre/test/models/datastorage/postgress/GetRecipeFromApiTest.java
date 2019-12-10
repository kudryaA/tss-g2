package tss.g2.fyre.test.models.datastorage.postgress;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.datastorage.postgress.utils.recipe.GetRecipeFromApi;
import tss.g2.fyre.models.entity.Type;
import tss.g2.fyre.models.entity.recipe.Recipe;
import tss.g2.fyre.models.entity.recipe.RecipeWithType;
import tss.g2.fyre.utils.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GetRecipeFromApiTest {
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
                    "INSERT INTO person (login, password, name, surname, bannedstatus, email, role) " +
                            "VALUES ('john_test_admin', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'john', " +
                            "'doe', false, 'john@doe.com', 'admin')")) {
                statement.executeUpdate();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into keys values ('john_test_admin', 'some_key', 0)")) {
                statement.executeUpdate();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into recipe values ('test_id', 'name', 'composition', 'steps', null , null, 'john_test_admin', 0, true)")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into type values ('type_name', 'desc', null)")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into recipetype values ('test_id', 'type_name')")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void actionGetRecipeFromApiTest() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        Connection connection = DriverManager.getConnection(
                        "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password);
        GetRecipeFromApi getRecipeFromApi = new GetRecipeFromApi(connection, "test_id", "some_key");
        Recipe recipe = new Recipe("test_id", "name", "composition", "steps", null, null, "john_test_admin", 0);
        Type type = new Type("type_name", "asd", null);
        List<Type> list = new ArrayList<>();
        list.add(type);
        RecipeWithType recipe1 = new RecipeWithType(recipe, list);
        Assert.assertEquals(recipe1, getRecipeFromApi.getRecipeFromApi());
        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipetype WHERE recipe_id = 'test_id'")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM type WHERE name = 'type_name'")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipe WHERE recipe_id = 'test_id'")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM keys WHERE login = 'john_test_admin'")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM person WHERE login = 'john_test_admin'")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
        }
    }
}
