package tss.g2.fyre.test.models.datastorage.postgress;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.utils.Configuration;

import java.sql.*;
import java.util.Properties;

public class DeleteRecipeTestPostgres {
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
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO person (login, password, name, surname, bannedstatus, email, role) " +
                            "VALUES ('john_test_1', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'john', " +
                            "'doe', false, 'john@doe.com', 'admin')")) {
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO type (name, description, image) " +
                            "VALUES ('test_type1', 'test_type1', 'test_type1')," +
                            "('test_type2', 'test_type2', 'test_type2')"))  {
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipe (recipe_id, name, recipeComposition, cookingSteps, publicationDate," +
                            "image, creator, rating, isConfirmed)" +
                            "VALUES ('test_id1', 'test_recipe1','composition', 'steps', timestamp '2001-09-28 01:00'," +
                            "'image_com', 'julia', 178, true)," +
                            "('test_id2', 'test_recipe2', 'composition', 'steps', timestamp '2001-09-28 01:00'," +
                            "'image_com', 'john', 178, true)")) {
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipeType (recipe_id, type_name) " +
                            "VALUES ('test_id1', 'test_type1')," +
                            "('test_id1', 'test_type2')," +
                            "('test_id2', 'test_type2')"))  {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteRecipe() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        boolean result1 = dataStorage.deleteRecipe("test_id1", "julia");
        boolean result2 = dataStorage.deleteRecipe("test_id2", "john_test_1");
        try(Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement("SELECT count(*) n FROM recipe WHERE recipe_id = 'test_id1'")){
                try (ResultSet resultSet = statement.executeQuery()){
                    if (resultSet.next()) {
                        Integer n = resultSet.getInt("n");
                        boolean res = false;
                        if(n == 0){
                            res = true;
                        }
                        Assert.assertEquals(true, res);
                    }
                }
            }
            try (PreparedStatement statement = connection.prepareStatement("SELECT count(*) n FROM recipeType where recipe_id = 'test_id1'")){
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        Integer n = resultSet.getInt("n");
                        boolean res = false;
                        if(n == 0){
                            res = true;
                        }
                        Assert.assertEquals(true, res);
                    }
                }
            }
            try (PreparedStatement statement = connection.prepareStatement("SELECT count(*) n FROM recipe WHERE recipe_id = 'test_id2'")){
                try (ResultSet resultSet = statement.executeQuery()){
                    if (resultSet.next()) {
                        Integer n = resultSet.getInt("n");
                        boolean res = false;
                        if(n == 0){
                            res = true;
                        }
                        Assert.assertEquals(true, res);
                    }
                }
            }
            try (PreparedStatement statement = connection.prepareStatement("SELECT count(*) n FROM recipeType where recipe_id = 'test_id2'")){
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        Integer n = resultSet.getInt("n");
                        boolean res = false;
                        if(n == 0){
                            res = true;
                        }
                        Assert.assertEquals(true, res);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(true, result1);
        Assert.assertEquals(true, result2);
        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM type WHERE name in ('test_type1', 'test_type2')")) {
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM person WHERE login = 'john_test_1'")) {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
