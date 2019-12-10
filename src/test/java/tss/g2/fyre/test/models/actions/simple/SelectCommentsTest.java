package tss.g2.fyre.test.models.actions.simple;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.simple.CheckAuthorization;
import tss.g2.fyre.models.actions.simple.SelectComments;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Comment;
import tss.g2.fyre.utils.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class SelectCommentsTest {
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
                    "INSERT INTO person (login, password, name, surname, bannedstatus, email, role) " +
                            "VALUES ('john_test_1', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'john', " +
                            "'doe', false, 'john@doe.com', 'admin')")) {
                statement2.execute();
            }
            try (PreparedStatement statement3 = connection.prepareStatement(
                    "INSERT INTO comment (user_login, recipe_id, comment_text) " +
                            "VALUES ('john_test_1', '1', 'testcomment')")) {
                statement3.execute();
            }
        } catch (SQLException e) {
        }
    }

    @Test
    public void testSelectComments() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        //Assert.assertEquals(testPerson, result);
        SelectComments comment = new SelectComments(dataStorage, "1");
        Answer answer = comment.getAnswer();

        dataStorage.close();
        Assert.assertTrue(answer.isStatus());
        Comment comment1 = new Comment("john_test_1", "testcomment");
        ArrayList<Comment> list = new ArrayList<>();
        list.add(comment1);
        ArrayList<Comment> resultList = (ArrayList<Comment>) answer.getObj();
        Assert.assertEquals(list, resultList);
    }


    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement1 = connection.prepareStatement(
                    "DELETE FROM comment WHERE user_login = 'john_test_1'")) {
                statement1.execute();
            }
            try (PreparedStatement statement2 = connection.prepareStatement(
                    "DELETE FROM recipe WHERE recipe_id = '1'")) {
                statement2.execute();
            }
            try (PreparedStatement statement3 = connection.prepareStatement(
                    "DELETE FROM person WHERE login = 'john_test_1'")) {
                statement3.execute();
            }
        } catch (SQLException e) {
        }
    }
}
