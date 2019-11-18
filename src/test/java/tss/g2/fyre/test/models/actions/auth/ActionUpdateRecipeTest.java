package tss.g2.fyre.test.models.actions.auth;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.auth.SelectUnconfirmedRecipes;
import tss.g2.fyre.models.actions.auth.UpdateRecipe;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Roles;
import tss.g2.fyre.utils.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class ActionUpdateRecipeTest {
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
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)) {

            try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO person (login, password, name, surname, bannedstatus, email, role) VALUES " +
                    "('asd', 'qwe', 'john', " +
                    "'asd', false, 'asd@doe.com', 'admin')")) {
              statement.execute();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO person (login, password, name, surname, bannedstatus, email, role) VALUES " +
                            "('john_test_experiencedUser', 'qwe', 'john', " +
                            "'asd', false, 'asd@doe.com', 'experiencedUser')")) {
                statement.execute();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipe (recipe_id, name, recipecomposition, cookingsteps, publicationdate, image, creator, rating, isconfirmed) " +
                            "values ('some_recipe_id1', 'some_recipe_name', 'some_recipe_composition', " +
                            "'some_cooking_steps', current_timestamp , 'unnamed', 'john_test_experiencedUser', 1, false)")) {
                statement.execute();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testActionUpdateRecipeTest() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        UpdateRecipe selectUnconfirmedRecipes = new UpdateRecipe(dataStorage, "some_recipe_id1", "new_recipe_name",
                "new_recipe_composition", "new_cooking_steps");

        Assert.assertEquals(new Answer<>(true, false), selectUnconfirmedRecipes.getAnswer("user", Roles.user.toString()));
        Assert.assertEquals(new Answer<>(true, true), selectUnconfirmedRecipes.getAnswer("john_test_experiencedUser", Roles.experiencedUser.toString()));
        Assert.assertEquals(new Answer<>(true, true), selectUnconfirmedRecipes.getAnswer("asd", Roles.admin.toString()));
        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){

            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipe WHERE recipe_id = 'some_recipe_id1'")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM person WHERE login = 'john_test_experiencedUser'")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM person WHERE login = 'asd'")) {
              statement.executeUpdate();
            }
        } catch (SQLException e) {
        }
    }
}
