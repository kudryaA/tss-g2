package tss.g2.fyre.test.models.datastorage.postgress;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Type;
import tss.g2.fyre.models.entity.recipe.Recipe;
import tss.g2.fyre.models.entity.recipe.RecipeWithType;
import tss.g2.fyre.utils.Configuration;
import tss.g2.fyre.utils.DateConverter;

import java.sql.*;
import java.text.ParseException;
import java.util.*;

public class SelectRecipesTestPostgres {
    private static List<RecipeWithType> list1 = new ArrayList<>();
    private static List<RecipeWithType> list2 = new ArrayList<>();
    private static Properties properties = new Configuration("config/configuration.yml").getProperties();
    private static String host = properties.getProperty("database_host");
    private static String port = properties.getProperty("database_port");
    private static String database = properties.getProperty("database_database");
    private static String user = properties.getProperty("database_user");
    private static String password = properties.getProperty("database_password");

    @Before
    public void init() throws ParseException {
        Timestamp date1 = new Timestamp((new DateConverter("15/10/2016 14:54:30").date()).getTime());
        Timestamp date2 = new Timestamp((new DateConverter("15/10/2019 14:54:30").date()).getTime());
        Timestamp date3 = new Timestamp((new DateConverter("15/10/2018 14:54:30").date()).getTime());
        Timestamp date4 = new Timestamp((new DateConverter("15/10/2015 14:54:30").date()).getTime());
        Recipe recipe1 = new Recipe("test_id1", "test_recipe1", "test1 test2  test3",
                "steps", date1, "image", "null", 178);
        Recipe recipe2 = new Recipe("test_id2", "test_recipe2", "test2 test4",
                "steps", date2, "image", "null", 394);
        Recipe recipe3 = new Recipe("test_id3", "test_recipe3", "test5 test2",
                "steps", date3, "image", "null", 1448);
        Recipe recipe4 = new Recipe("test_id4", "test_recipe4", "test2 test4",
                "steps", date4, "image", "null", 1768);
        List<Type> type = new ArrayList<>();
        Type type1 = new Type("test_type1", "test_type1", "test_type1");
        type.add(type1);
        RecipeWithType rec1 = new RecipeWithType(recipe1, type);
        RecipeWithType rec2 = new RecipeWithType(recipe2, type);
        RecipeWithType rec3 = new RecipeWithType(recipe3, type);
        RecipeWithType rec4 = new RecipeWithType(recipe4, type);
        list1.add(rec4);
        list1.add(rec3);
        list1.add(rec2);
        list1.add(rec1);
        list2.add(rec2);
        list2.add(rec3);
        list2.add(rec1);
        list2.add(rec4);
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipe (recipe_id, name, recipeComposition, cookingSteps, publicationDate, image, creator, rating, isConfirmed) " +
                            "VALUES ('test_id1', 'test_recipe1', 'test1 test2  test3', 'steps', ?, 'image', 'julia', 178, true)," +
                            "('test_id2', 'test_recipe2', 'test2 test4', 'steps', ?, 'image', 'john', 394, true)," +
                            "('test_id3', 'test_recipe3', 'test5 test2', 'steps', ?, 'image', 'john', 1448, true )," +
                            "('test_id4', 'test_recipe4', 'test2 test4', 'steps', ?, 'image', 'john', 1768, true)")) {
                statement.setTimestamp(1, date1);
                statement.setTimestamp(2, date2);
                statement.setTimestamp(3, date3);
                statement.setTimestamp(4, date4);
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO type (name, description, image) " +
                            "VALUES ('test_type1', 'test_type1', 'test_type1')"))  {
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipeType (recipe_id, type_name) " +
                            "VALUES ('test_id1', 'test_type1')," +
                            "('test_id2', 'test_type1')," +
                            "('test_id3', 'test_type1')," +
                            "('test_id4', 'test_type1')"))  {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSelectRecipes() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        Map<String, Object> result1 = dataStorage.selectRecipes(1, 4, "test_type1", "rating");
        List<RecipeWithType> first = (List<RecipeWithType>) result1.get("recipes");
        Assert.assertEquals(list1.toString(), first.toString());
        Map<String, Object> result2 = dataStorage.selectRecipes(1, 4, "test_type1", "publicationDate");
        List<RecipeWithType> second = (List<RecipeWithType>) result2.get("recipes");
        Assert.assertEquals(list2.toString(), second.toString());
        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipeType WHERE type_name = 'test_type1'")) {
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM type WHERE name = 'test_type1'")) {
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipe WHERE recipe_id in ('test_id1', 'test_id2', 'test_id3', 'test_id4')")) {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
