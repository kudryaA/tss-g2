package tss.g2.fyre.test.models.actions.simple;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.simple.SelectRecipes;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Type;
import tss.g2.fyre.models.entity.recipe.Recipe;
import tss.g2.fyre.models.entity.recipe.RecipeWithType;
import tss.g2.fyre.utils.Configuration;
import tss.g2.fyre.utils.DateConverter;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ActionSelectRecipesTest {
    private Timestamp timestamp = new Timestamp(new DateConverter("10/10/2010 10:10:10").date().getTime());

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
                    "INSERT INTO type values ('some_test_type_name', 'test_type', 'unnamed')")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipe values " +
                        "('test_recipe_id1', 'name', 'test_recipe_composition', 'test_recipe_cooking_steps', ?, null, 'john_test_admin', 0, true), " +
                        "('test_recipe_id2', 'name', 'test_recipe_composition', 'test_recipe_cooking_steps', ?, null, 'john_test_admin', 0, true)")) {
                statement.setTimestamp(1, timestamp);
                statement.setTimestamp(2, timestamp);
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipetype values ('test_recipe_id1', 'some_test_type_name'), ('test_recipe_id2', 'some_test_type_name')")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void actionSelectRecipesTest() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        SelectRecipes selectRecipes = new SelectRecipes(dataStorage, 1, 1000, "some_test_type_name", "rating");
        Answer answer = selectRecipes.getAnswer();

        Assert.assertTrue(answer.isStatus());
        Map<String, Object> map = (Map<String, Object>) answer.getObj();

        RecipeWithType recipe1 = new RecipeWithType(
                new Recipe("test_recipe_id1", "name", "test_recipe_composition",
                "test_recipe_cooking_steps", timestamp, null, "john_test_admin", 0),
                Collections.singletonList(new Type("some_test_type_name", "test_type", "unnamed")));
        RecipeWithType recipe2 = new RecipeWithType(
                new Recipe("test_recipe_id2", "name", "test_recipe_composition",
                        "test_recipe_cooking_steps", timestamp, null, "john_test_admin", 0),
                Collections.singletonList(new Type("some_test_type_name", "test_type", "unnamed")));

        List<RecipeWithType> list = (List<RecipeWithType>) map.get("recipes");
        boolean result = list.contains(recipe1) && list.contains(recipe2);
        Assert.assertTrue(result);
        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipetype WHERE type_name = 'some_test_type_name'")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM type WHERE name = 'some_test_type_name'")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipe WHERE recipe_id in ('test_recipe_id1', 'test_recipe_id2')")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM person WHERE login = 'john_test_admin'")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
