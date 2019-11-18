package tss.g2.fyre.test.models.datastorage.postgress;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.recipe.Recipe;
import tss.g2.fyre.utils.Configuration;
import tss.g2.fyre.utils.DateConverter;

import java.sql.*;
import java.text.ParseException;
import java.util.*;

public class SearchRecipeTypePostgres {
    private static List<Recipe> list1 = new ArrayList<>();
    private static List<Recipe> list2 = new ArrayList<>();
    private static List<Recipe> list3 = new ArrayList<>();
    private static Properties properties = new Configuration("config/configuration.yml").getProperties();
    private static String host = properties.getProperty("database_host");
    private static String port = properties.getProperty("database_port");
    private static String database = properties.getProperty("database_database");
    private static String user = properties.getProperty("database_user");
    private static String password = properties.getProperty("database_password");

    @Before
    public void init() throws ParseException {
        Timestamp date = new Timestamp((new DateConverter("15/10/2019 14:54:30").date()).getTime());
        Recipe recipe1 = new Recipe("test_id1", "test_recipe1", "test1 test2  test3",
                "steps", date, "image", "julia", 178);
        Recipe recipe2 = new Recipe("test_id2", "test_recipe2", "test2 test4",
                "steps", date, "image", "john", 394);
        Recipe recipe3 = new Recipe("test_id3", "test_recipe3", "test5 test2",
                "steps", date, "image", "john", 1448);
        Recipe recipe4 = new Recipe("test_id4", "test_recipe4", "test2 test4",
                "steps", date, "image", "john", 1768);

        list1.add(recipe1);
        list1.add(recipe2);
        list1.add(recipe4);
        list2.add(recipe4);

        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipe (recipe_id, name, recipeComposition, cookingSteps, publicationDate, image, creator, rating, isConfirmed) " +
                            "VALUES ('test_id1', 'test_recipe1', 'test1 test2  test3', 'steps', ?, 'image', 'julia', 178, true)," +
                            "('test_id2', 'test_recipe2', 'test2 test4', 'steps', ?, 'image', 'john', 394, true)," +
                            "('test_id3', 'test_recipe3', 'test5 test2', 'steps', ?, 'image', 'john', 1448, false )," +
                            "('test_id4', 'test_recipe4', 'test2 test4', 'steps', ?, 'image', 'john', 1768, true)")) {
                statement.setTimestamp(1, date);
                statement.setTimestamp(2, date);
                statement.setTimestamp(3, date);
                statement.setTimestamp(4, date);
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSearchRecipe() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        Map<String, Object> result1 = dataStorage.searchRecipe("test2", 1, 3);
        List<Recipe> first1 = (List<Recipe>) result1.get("recipes");
        Collections.sort(first1);
        Collections.sort(list1);
        Assert.assertEquals(first1.toString(), list1.toString());

        Map<String, Object> result2 = dataStorage.searchRecipe("test4", 2, 1);
        List<Recipe> first2 = (List<Recipe>) result2.get("recipes");
        Collections.sort(first2);
        Collections.sort(list2);
        Assert.assertEquals(first2.toString(), list2.toString());

        Map<String, Object> result3 = dataStorage.searchRecipe("test5", 1, 1);
        List<Recipe> first3 = (List<Recipe>) result3.get("recipes");
        Assert.assertEquals(list3, first3);
        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipe WHERE recipe_id in ('test_id1', 'test_id2', 'test_id3', 'test_id4')")) {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
