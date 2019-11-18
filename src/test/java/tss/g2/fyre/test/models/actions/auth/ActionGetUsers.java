package tss.g2.fyre.test.models.actions.auth;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.AnswerWithComment;
import tss.g2.fyre.models.actions.auth.AddComment;
import tss.g2.fyre.models.actions.auth.GetUsers;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Person;
import tss.g2.fyre.models.entity.Roles;
import tss.g2.fyre.utils.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ActionGetUsers {
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
                            "('john_test_admin', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'john', " +
                            "'doe', false, 'john@doe.com', 'admin'), " +
                            "('john_test_moderator', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'john', " +
                            "'doe', false, 'john@doe.com', 'moderator')")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
        }
    }

    @Test
    public void actionGetUsers() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        GetUsers getUsers = new GetUsers(dataStorage);

        Person admin = new Person("john_test_admin", null,
                "john", "doe", false, "john@doe.com", "admin");
        Person moderator = new Person("john_test_admin", null,
                "john", "doe", false, "john@doe.com", "admin");

        Answer answer = getUsers.getAnswer("asd", Roles.admin.toString());
        List<Person> list = (List<Person>) answer.getObj();
        int k = 0;
        if (list.contains(admin))
            k++;
        if (list.contains(moderator))
            k++;

        Assert.assertEquals(2, k);

        Assert.assertEquals(new AnswerWithComment(true, false,
                "You do not have permission to perform this operation."),
                getUsers.getAnswer("qwe", Roles.user.toString()));
        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM person WHERE login in ('john_test_admin', 'john_test_moderator')")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
