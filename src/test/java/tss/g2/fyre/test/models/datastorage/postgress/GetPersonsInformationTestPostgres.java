package tss.g2.fyre.test.models.datastorage.postgress;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Person;
import tss.g2.fyre.utils.Configuration;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class GetPersonsInformationTestPostgres {
    private static List<Person> persons = new ArrayList<>();
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
            Person person1 = new Person("john_test_1", null,
                    "john", "doe", false, "john@doe.com", "admin");
            Person person2 = new Person("john_test_2", null,
                    "john2", "doe2", false, "john@doe.com", "user");
            persons.add(person1);
            persons.add(person2);
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO person (login, password, name, surname, bannedstatus, email, role) " +
                            "VALUES ('john_test_1', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'john', " +
                            "'doe', false, 'john@doe.com', 'admin')," +
                            "('john_test_2'," +
                            "'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'john2', 'doe2', false," +
                            "'john@doe.com', 'user')")) {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetPersonsInformation() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        List <Person> result = dataStorage.getPersonsInformation().stream().
                filter(i -> persons.contains(i)).collect(Collectors.toList());
        Collections.sort(result);
        Collections.sort(persons);
        Assert.assertEquals(persons, result);
        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM person WHERE login in ('john_test_1', 'john_test_2')")) {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
