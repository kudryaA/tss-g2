package tss.g2.fyre.test.models.datastorage.postgress;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.utils.Configuration;

import java.sql.*;
import java.util.Properties;

public class AddTypeTestPostgres {
    private static Properties properties = new Configuration("config/configuration.yml").getProperties();
    private static String host = properties.getProperty("database_host");
    private static String port = properties.getProperty("database_port");
    private static String database = properties.getProperty("database_database");
    private static String user = properties.getProperty("database_user");
    private static String password = properties.getProperty("database_password");

    @Test
    public void testAddType() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        boolean result = dataStorage.addType("new_type", "new_type", "new_type");
        try(Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM type WHERE name = 'new_type'")){
                try (ResultSet resultSet = statement.executeQuery()){
                    if (resultSet.next()) {
                        String name = resultSet.getString("name");
                        String description = resultSet.getString("description");
                        String image = resultSet.getString("image");
                        Assert.assertEquals("new_type", name);
                        Assert.assertEquals("new_type", description);
                        Assert.assertEquals("new_type", image);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(true, result);
        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM type WHERE name = 'new_type'")) {
                statement.executeQuery();
            }
        } catch (SQLException e) {
        }
    }
}
