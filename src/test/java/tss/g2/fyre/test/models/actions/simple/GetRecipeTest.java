package tss.g2.fyre.test.models.actions.simple;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.simple.GetRecipe;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Type;
import tss.g2.fyre.models.entity.recipe.Recipe;
import tss.g2.fyre.models.entity.recipe.RecipeWithType;
import tss.g2.fyre.utils.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class GetRecipeTest {
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
            //password = a
            try (PreparedStatement statement1 = connection.prepareStatement(
                    "INSERT INTO recipe (recipe_id, name, recipeComposition, cookingSteps, " +
                            "publicationDate, image, creator, rating, isConfirmed) " +
                            "VALUES ('1', 'testname', 'testcomposition', 'cookingsteps', " +
                            "null, 'testimage', 'testcreator', 1, true)")) {
                statement1.execute();
            }
            try (PreparedStatement statement2 = connection.prepareStatement(
                    "INSERT INTO type (name, description, image) " +
                            "VALUES ('testname', 'testdescription', 'testimage')"
            )){
                statement2.execute();
            }
            try (PreparedStatement statement3 = connection.prepareStatement(
                    "INSERT INTO recipetype (recipe_id, type_name) " +
                            "VALUES ('1', 'testname')"
            )){
                statement3.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetRecipeAction() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        //Assert.assertEquals(testPerson, result);
        GetRecipe recipe = new GetRecipe(dataStorage, "1");
        Answer answer = recipe.getAnswer();
        System.out.println(recipe.getAnswer().toJson());
        Type type = new Type("testname", "testdescription", "testimage");
        RecipeWithType testRecipe = new RecipeWithType(new Recipe("1", "testname", "testcomposition",
                "cookingsteps", null, "testimage", "testcreator", 1),
                Collections.singletonList(type));
        ArrayList<RecipeWithType> list = new ArrayList<>();
        list.add(testRecipe);
        Answer answer1 = new Answer<>(true, list);
        RecipeWithType recipeWithType = (RecipeWithType) answer.getObj();

        boolean result = answer.isStatus() == answer.isStatus()
                && recipeWithType.getRating() == testRecipe.getRating()
                && Objects.equals(recipeWithType.getName(), testRecipe.getName())
                && Objects.equals(recipeWithType.getComposition(), testRecipe.getComposition())
                && Objects.equals(recipeWithType.getCookingSteps(), testRecipe.getCookingSteps())
                && Objects.equals(recipeWithType.getPublicationDate(), testRecipe.getPublicationDate())
                && Objects.equals(recipeWithType.getImage(), testRecipe.getImage())
                && Objects.equals(recipeWithType.getCreator(), testRecipe.getCreator())
                && Objects.equals(recipeWithType.getId(), testRecipe.getId())
                && recipeWithType.equals(testRecipe);

        Assert.assertTrue(result);
        dataStorage.close();
        //Assert.assertEquals(true, answer.getObj());
    }

    @Test
    public void testGetNonexistentRecipe() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        //Assert.assertEquals(testPerson, result);
        GetRecipe recipe = new GetRecipe(dataStorage, "2");
        Answer answer = recipe.getAnswer();
        //System.out.println(recipe.getAnswer().toJson());
        dataStorage.close();
        Assert.assertEquals(false, answer.isStatus());
        //Assert.assertEquals(true, answer.getObj());
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement1 = connection.prepareStatement(
                    "DELETE FROM recipetype WHERE recipe_id = '1'")) {
                statement1.execute();
            }
            try (PreparedStatement statement1 = connection.prepareStatement(
                    "DELETE FROM type WHERE name = 'testname'")) {
                statement1.execute();
            }
            try (PreparedStatement statement1 = connection.prepareStatement(
                    "DELETE FROM recipe WHERE recipe_id = '1'")) {
                statement1.execute();
            }
        } catch (SQLException e) {
        }
    }
}
