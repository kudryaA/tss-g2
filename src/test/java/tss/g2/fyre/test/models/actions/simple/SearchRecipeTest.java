package tss.g2.fyre.test.models.actions.simple;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.simple.CheckAuthorization;
import tss.g2.fyre.models.actions.simple.SearchRecipe;
import tss.g2.fyre.models.actions.simple.SelectComments;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Comment;
import tss.g2.fyre.models.entity.recipe.Recipe;
import tss.g2.fyre.utils.Configuration;
import tss.g2.fyre.utils.DateConverter;

import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Date;

public class SearchRecipeTest {
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
                            "?, 'testimage', 'testcreator', 1, true)")) {
                statement1.setTimestamp(1, new Timestamp(new DateConverter("10/10/2010 10:10:10").date().getTime()));
                statement1.execute();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
        }
    }

    @Test
    public void testSearchRecipes() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        //Assert.assertEquals(testPerson, result);
        SearchRecipe recipe = new SearchRecipe(dataStorage, "testcomposition", 1, 10);
        //Answer answer = comment.getAnswer();
        //System.out.println(recipe.getAnswer().getObj());
        Map<String, Object> r = (HashMap<String, Object>) recipe.getAnswer().getObj();
        List<Recipe> list = (List<Recipe>) r.get("recipes");
        Recipe recipe1 = list.get(0);
        //System.out.println(recipe1);
        Assert.assertEquals("1", recipe1.getId());
        dataStorage.close();
        /*Assert.assertTrue(answer.isStatus());
        Comment comment1 = new Comment("john_test_1", "testcomment");
        ArrayList<Comment> list = new ArrayList<>();
        list.add(comment1);
        ArrayList<Comment> resultList = (ArrayList<Comment>) answer.getObj();
        Assert.assertEquals(list, resultList);*/
        //System.out.println(recipe.getAnswer().getObj());
    }


    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement2 = connection.prepareStatement(
                    "DELETE FROM recipe WHERE recipe_id = '1'")) {
                statement2.execute();
            }
        } catch (SQLException e) {
        }
    }
}
