package tss.g2.fyre.test.models.datastorage.postgress;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Type;
import tss.g2.fyre.utils.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class GetTypesInformationTestPostgres {
    private static List <Type> types = new ArrayList<>();
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
            Type type1 = new Type("type1", "type1", "type1");
            Type type2 = new Type("type2", "type2", "type2");
            Type type3 = new Type("type3", "type3", "type3");
            types.add(type1);
            types.add(type2);
            types.add(type3);
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO type (name, description, image) " +
                            "VALUES ('type1', 'type1', 'type1')," +
                            "('type2', 'type2', 'type2')," +
                            "('type3', 'type3', 'type3')")) {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetTypesInformation() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        List<Type> result = dataStorage.getTypesInformation().stream().
                filter(i -> types.contains(i)).collect(Collectors.toList());
        Collections.sort(result);
        Collections.sort(types);
        Assert.assertEquals(types.toString(), result.toString());
        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM type WHERE name in ('test1', 'test2', 'test3')")) {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
