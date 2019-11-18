package tss.g2.fyre.test.models.actions.simple;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.simple.GetTypes;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Type;
import tss.g2.fyre.utils.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class GetTypesTest {
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
                    "INSERT INTO type (name, description, image) " +
                            "VALUES ('testname1', 'testdescription1', 'testimage1')")) {
                statement1.execute();
            }
            try (PreparedStatement statement2 = connection.prepareStatement(
                    "INSERT INTO type (name, description, image) " +
                            "VALUES ('testname2', 'testdescription2', 'testimage2')"
            )){
                statement2.execute();
            }
            try (PreparedStatement statement3 = connection.prepareStatement(
                    "INSERT INTO type (name, description, image) " +
                            "VALUES ('testname3', 'testdescription3', 'testimage3')"
            )){
                statement3.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetTypesAction() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        //Assert.assertEquals(testPerson, result);
        GetTypes getType = new GetTypes(dataStorage);
        Answer answer = getType.getAnswer();
        System.out.println(getType.getAnswer().toJson());
        Type testtype1 = new Type("testname1", "testdescription1", "testimage1");
        Type testtype2 = new Type("testname2", "testdescription2", "testimage2");
        Type testtype3 = new Type("testname3", "testdescription3", "testimage3");
        ArrayList<Type> list = (ArrayList<Type>) answer.getObj();

        boolean result = list.contains(testtype1)
                && list.contains(testtype2)
                && list.contains(testtype3);

        Assert.assertTrue(result);
        dataStorage.close();
        //Assert.assertEquals(true, answer.getObj());
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement1 = connection.prepareStatement(
                    "DELETE FROM type WHERE name = 'testname1'")) {
                statement1.execute();
            }
            try (PreparedStatement statement2 = connection.prepareStatement(
                    "DELETE FROM type WHERE name = 'testname2'")) {
                statement2.execute();
            }
            try (PreparedStatement statement3 = connection.prepareStatement(
                    "DELETE FROM type WHERE name = 'testname3'")) {
                statement3.execute();
            }
        } catch (SQLException e) {
        }
    }
}
