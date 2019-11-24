package tss.g2.fyre.test.models.actions.auth;

import io.javalin.http.UploadedFile;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.auth.AddComment;
import tss.g2.fyre.models.actions.auth.AddRecipe;
import tss.g2.fyre.models.actions.simple.GetRecipe;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Roles;
import tss.g2.fyre.models.entity.Type;
import tss.g2.fyre.models.entity.recipe.Recipe;
import tss.g2.fyre.models.entity.recipe.RecipeWithType;
import tss.g2.fyre.utils.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class ActionAddRecipeTest {
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
                            "VALUES ('john_test_1', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'john', " +
                            "'doe', false, 'john@doe.com', 'admin')")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO type values ('some_type_name', 'test_type', 'unnamed')")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
        }
    }

    @Test
    public void actionAddCommentTest() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        try {
            UploadedFile file = new UploadedFile(
                    new FileInputStream(new File("images/unnamed")), "text",
                    Files.readAllBytes(Paths.get("images/unnamed")).length,
                    "images/unnamed", "unnamed");
            Date date = new Date();

            AddRecipe addRecipe = new AddRecipe(
                    dataStorage, "test_recipe", "some recipe composition",
                    "some cooking steps", date, "some_type_name", file);

            Answer addRecipeAnswer = addRecipe.getAnswer("john_test_1", Roles.admin.toString());
            GetRecipe getRecipe = new GetRecipe(dataStorage, addRecipeAnswer.getObj().toString());
            Answer getRecipeAnswer = getRecipe.getAnswer();

            RecipeWithType recipeById = (RecipeWithType) getRecipeAnswer.getObj();
            Recipe recipe = new Recipe(addRecipeAnswer.getObj().toString(), "test_recipe", "some recipe composition",
                    "some cooking steps", recipeById.getPublicationDate(), recipeById.getImage(),
                    "john_test_1", recipeById.getRating());
            Type type = new Type("some_type_name", "asd", "asd");
            ArrayList<Type> list = new ArrayList(Collections.singleton(type));

            RecipeWithType recipeWithType = new RecipeWithType(recipe, list);

            boolean result = recipeWithType.getRating() == recipeById.getRating()
                    && Objects.equals(recipeWithType.getName(), recipeById.getName())
                    && Objects.equals(recipeWithType.getComposition(), recipeById.getComposition())
                    && Objects.equals(recipeWithType.getCookingSteps(), recipeById.getCookingSteps())
                    && Objects.equals(recipeWithType.getPublicationDate(), recipeById.getPublicationDate())
                    && Objects.equals(recipeWithType.getImage(), recipeById.getImage())
                    && Objects.equals(recipeWithType.getCreator(), recipeById.getCreator())
                    && Objects.equals(recipeWithType.getId(), recipeById.getId())
                    && recipeWithType.equals(recipeById);
            Assert.assertTrue(result);
            dataStorage.close();
        } catch (IOException e) {
        }



    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipetype WHERE type_name = 'some_type_name'")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM type WHERE name = 'some_type_name'")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipe WHERE name = 'test_recipe'")) {
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM person WHERE login = 'john_test_1'")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
