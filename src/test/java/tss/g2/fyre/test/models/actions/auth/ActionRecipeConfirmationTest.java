package tss.g2.fyre.test.models.actions.auth;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.AnswerWithComment;
import tss.g2.fyre.models.actions.auth.RecipeConfirmation;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Roles;
import tss.g2.fyre.utils.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class ActionRecipeConfirmationTest {
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
                            "('john_test_adm', 'qwe', 'john', " +
                            "'asd', false, 'asd@doe.com', 'admin'), " +
                            "('john_test_user', 'asd', 'asd', " +
                            "'qwe', false, 'qwe@doe.com', 'user')")) {
                statement.execute();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipe (recipe_id, name, recipecomposition, cookingsteps, publicationdate, image, creator, rating, isconfirmed) " +
                            "values ('some_recipe_id', 'some_recipe_name', 'some_recipe_composition', " +
                            "'some_cooking_steps', current_timestamp, 'unnamed', 'john_test_user', 1, false)")) {
                statement.execute();
            } catch (Exception e) {
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO type values ('some_type_name', 'some_type_description', 'images')")) {
                statement.execute();
            } catch (Exception e) {
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipetype values ('some_recipe_id', 'some_type_name')")) {
                statement.execute();
            } catch (Exception e) {
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testActionRecipeConfirmation() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        RecipeConfirmation recipeConfirmation = new RecipeConfirmation(dataStorage, "some_recipe_id");

        Assert.assertEquals(new AnswerWithComment(true, false,
                        "You do not have permission to perform this operation."),
                recipeConfirmation.getAnswer("john_test_user", Roles.user.toString()));

        Assert.assertEquals(new AnswerWithComment(true, false,
                        "You do not have permission to perform this operation."),
                recipeConfirmation.getAnswer("john_test_experiencedUser", Roles.experiencedUser.toString()));

        Assert.assertEquals(new Answer<>(true, true),
                recipeConfirmation.getAnswer("john_test_adm", Roles.admin.toString()));
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipetype where recipe_id = 'some_recipe_id'")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM type where name = 'some_type_name'")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipe WHERE recipe_id = 'some_recipe_id'")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM person WHERE login in ('john_test_adm', 'john_test_user')")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
