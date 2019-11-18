package tss.g2.fyre.test.models.datastorage.postgress;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Comment;
import tss.g2.fyre.utils.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class SelectCommentsTestPostgres {
    List<Comment> comments1 = new ArrayList<>();
    List<Comment> comments2 = new ArrayList<>();
    private static Properties properties = new Configuration("config/configuration.yml").getProperties();
    private static String host = properties.getProperty("database_host");
    private static String port = properties.getProperty("database_port");
    private static String database = properties.getProperty("database_database");
    private static String user = properties.getProperty("database_user");
    private static String password = properties.getProperty("database_password");

    @Before
    public void init() {
        Comment comment1 = new Comment("john_comment_1", "my_comment1");
        Comment comment2 = new Comment("john_comment_1", "my_comment2");
        Comment comment3 = new Comment("john_comment_1", "my_comment3");
        comments1.add(comment1);
        comments1.add(comment3);
        comments2.add(comment2);
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO person (login, password, name, surname, bannedstatus, email, role) " +
                            "VALUES ('john_comment_1', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb'," +
                            "'john_1', 'doe', false, 'john@doe.com', 'user')")) {
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipe (recipe_id, name, recipeComposition, cookingSteps, publicationDate, image, creator, rating, isConfirmed) " +
                            "VALUES ('test_recipe1', 'test_com_recipe1', 'composition_com1', 'steps_com1', timestamp '2001-09-28 01:00', 'image_com1', 'julia', 178, true)," +
                            "('test_recipe2', 'test_com_recipe2', 'composition_com2', 'steps_com2', timestamp '2001-09-28 01:00', 'image_com2', 'julia', 178, true)")) {
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO comment (user_login, recipe_id, comment_text) " +
                            "VALUES ('john_comment_1', 'test_recipe1', 'my_comment1')," +
                            "('john_comment_1', 'test_recipe2', 'my_comment2')," +
                            "('john_comment_1', 'test_recipe1', 'my_comment3')")) {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testSelectComments() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        List<Comment> result1 = dataStorage.selectComments("test_recipe1").stream().
                filter(i -> comments1.contains(i)).collect(Collectors.toList());
        Collections.sort(comments1);
        Collections.sort(result1);
        Assert.assertEquals(comments1, result1);
        List<Comment> result2 = dataStorage.selectComments("test_recipe2").stream().
                filter(i -> comments2.contains(i)).collect(Collectors.toList());
        Collections.sort(comments2);
        Collections.sort(result2);
        Assert.assertEquals(comments2, result2);

        dataStorage.close();
    }


    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM comment WHERE user_login = 'john_comment_1'")) {
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM person WHERE login = 'john_comment_1'")) {
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM recipe WHERE recipe_id in ('test_recipe1', 'test_recipe2')")) {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
