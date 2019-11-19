package tss.g2.fyre.test.models.actions.auth;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.AnswerWithComment;
import tss.g2.fyre.models.actions.auth.RecipeConfirmation;
import tss.g2.fyre.models.actions.auth.SelectUnconfirmedRecipes;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Roles;
import tss.g2.fyre.models.entity.Type;
import tss.g2.fyre.models.entity.recipe.Recipe;
import tss.g2.fyre.models.entity.recipe.RecipeWithType;
import tss.g2.fyre.utils.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class ActionSelectUnconfirmedRecipesTest {
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
                            "values ('some_recipe_id1', 'some_recipe_name', 'some_recipe_composition', " +
                            "'some_cooking_steps', null, null, 'john_test_user', 1, false), " +
                            "('some_recipe_id2', 'some_recipe_name', 'some_recipe_composition', " +
                            "'some_cooking_steps', null, null, 'john_test_user', 1, false)")) {
                statement.execute();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO type values ('some_type_name', 'desc', null)")) {
                statement.execute();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipetype values ('some_recipe_id1', 'some_type_name'), ('some_recipe_id2', 'some_type_name')")) {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testActionSelectUnconfirmedRecipes() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        SelectUnconfirmedRecipes selectUnconfirmedRecipes = new SelectUnconfirmedRecipes(dataStorage);

        Assert.assertEquals(new AnswerWithComment(true, false,
                        "You do not have permission to perform this operation."),
                selectUnconfirmedRecipes.getAnswer("john_test_user", Roles.user.toString()));

        Assert.assertEquals(new AnswerWithComment(true, false,
                        "You do not have permission to perform this operation."),
                selectUnconfirmedRecipes.getAnswer("john_test_experiencedUser", Roles.experiencedUser.toString()));

        Answer answer = selectUnconfirmedRecipes.getAnswer("john_test_adm", Roles.admin.toString());
        List<RecipeWithType> list = (List<RecipeWithType>) answer.getObj();
        Type type = new Type("some_type_name", null, null);
        RecipeWithType recipe1 = new RecipeWithType(new Recipe("some_recipe_id1", "some_recipe_name", "some_recipe_composition",
                "some_cooking_steps", null, null, "john_test_user", 1), new ArrayList<>(Collections.singleton(type)));
        RecipeWithType recipe2 = new RecipeWithType(new Recipe("some_recipe_id2", "some_recipe_name", "some_recipe_composition",
                "some_cooking_steps", null, null, "john_test_user", 1), new ArrayList<>(Collections.singleton(type)));

        boolean result = list.contains(recipe1) && list.contains(recipe1);
        Assert.assertTrue(result);
        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipetype WHERE recipe_id in ('some_recipe_id1', 'some_recipe_id2') and type_name = 'some_type_name'")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM type WHERE name = 'some_type_name'")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipe WHERE recipe_id in ('some_recipe_id1', 'some_recipe_id2')")) {
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
