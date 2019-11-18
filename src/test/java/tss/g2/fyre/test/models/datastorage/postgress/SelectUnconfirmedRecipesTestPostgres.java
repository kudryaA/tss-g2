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
import java.util.stream.Collectors;

public class SelectUnconfirmedRecipesTestPostgres {
    private static List <RecipeWithType> recipe = new ArrayList<>();
    private static List <RecipeWithType> recipes = new ArrayList<>();
    private static Properties properties = new Configuration("config/configuration.yml").getProperties();
    private static String host = properties.getProperty("database_host");
    private static String port = properties.getProperty("database_port");
    private static String database = properties.getProperty("database_database");
    private static String user = properties.getProperty("database_user");
    private static String password = properties.getProperty("database_password");

    @Before
    public void init() throws ParseException {
        Timestamp date = new Timestamp((new DateConverter("15/10/2019 14:54:30").date()).getTime());
        Recipe recipe3 = new Recipe("test_id3", "test_recipe3", "composition",
                "steps", date, "image", "john", 1448);
        Recipe recipe4 = new Recipe("test_id4", "test_recipe4", "composition",
                "steps", date, "image", "john", 1768);
        Recipe recipe1 = new Recipe("test_id1", "test_recipe1", "composition",
                "steps", date, "image", "julia", 178);
        Recipe recipe2 = new Recipe("test_id2", "test_recipe2", "composition",
                "steps", date, "image", "john", 394);
        List <Type> list1 = new ArrayList<>();
        Type type1 = new Type("test_type1", "test_type1", "test_type1");
        Type type2 = new Type("test_type2", "test_type2", "test_type2");
        list1.add(type2);
        List <Type> list2 = new ArrayList<>();
        list2.add(type1);
        list2.add(type2);
        RecipeWithType recipeWithType1 = new RecipeWithType(recipe1, list1);
        RecipeWithType recipeWithType2 = new RecipeWithType(recipe2, list1);
        RecipeWithType recipeWithType3 = new RecipeWithType(recipe3, list1);
        RecipeWithType recipeWithType4 = new RecipeWithType(recipe4, list2);
        recipe.add(recipeWithType3);
        recipe.add(recipeWithType4);
        recipes.add(recipeWithType1);
        recipes.add(recipeWithType2);
        recipes.add(recipeWithType3);
        recipes.add(recipeWithType4);
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO type (name, description, image) " +
                            "VALUES ('test_type1', 'test_type1', 'test_type1')," +
                            "('test_type2', 'test_type2', 'test_type2')"))  {
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipe (recipe_id, name, recipeComposition, cookingSteps, publicationDate, image, creator, rating, isConfirmed) " +
                            "VALUES ('test_id1', 'test_recipe1', 'composition', 'steps', ?, 'image', 'julia', 178, true)," +
                            "('test_id2', 'test_recipe2', 'composition', 'steps', ?, 'image', 'john', 394, true)," +
                            "('test_id3', 'test_recipe3', 'composition', 'steps', ?, 'image', 'john', 1448, false )," +
                            "('test_id4', 'test_recipe4', 'composition', 'steps', ?, 'image', 'john', 1768, false)")) {
                statement.setTimestamp(1, date);
                statement.setTimestamp(2, date);
                statement.setTimestamp(3, date);
                statement.setTimestamp(4, date);
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipeType (recipe_id, type_name) " +
                            "VALUES ('test_id1', 'test_type1')," +
                            "('test_id1', 'test_type2')," +
                            "('test_id2', 'test_type2')," +
                            "('test_id3', 'test_type2')," +
                            "('test_id4', 'test_type1')," +
                            "('test_id4', 'test_type2')"))  {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSelectUnconfirmedRecipes() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        List<RecipeWithType> result =  dataStorage.selectUnconfirmedRecipes().
                stream().filter(i -> recipes.contains(i)).collect(Collectors.toList());
        Collections.sort(result);
        Collections.sort(recipe);
        for (int i = 0; i < result.size(); i++) {
            Assert.assertEquals(recipe.get(i).getId(), result.get(i).getId());
            Assert.assertEquals(recipe.get(i).getTypes(), result.get(i).getTypes());
            Assert.assertEquals(recipe.get(i).getName(), result.get(i).getName());
            Assert.assertEquals(recipe.get(i).getComposition(), result.get(i).getComposition());
            Assert.assertEquals(recipe.get(i).getCookingSteps(), result.get(i).getCookingSteps());
            Assert.assertEquals(recipe.get(i).getRating(), result.get(i).getRating());
            Assert.assertEquals(recipe.get(i).getImage(), result.get(i).getImage());
            Assert.assertEquals(recipe.get(i).getCreator(), result.get(i).getCreator());
            Assert.assertEquals("2019-10-15", result.get(i).getPublicationDate().toString());
        }
        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipeType WHERE recipe_id in ('test_id1', 'test_id2', 'test_id3', 'test_id4')")) {
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipe WHERE recipe_id in ('test_id1', 'test_id2', 'test_id3', 'test_id4')")) {
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM type WHERE name in ('test_type1', 'test_type2')")) {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
