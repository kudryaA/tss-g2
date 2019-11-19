package tss.g2.fyre.test.models.datastorage.postgress;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.utils.Configuration;

import java.sql.*;
import java.util.Properties;

public class AddTimeApiTestPostgres {
    private static Properties properties = new Configuration("config/configuration.yml").getProperties();
    private static String host = properties.getProperty("database_host");
    private static String port = properties.getProperty("database_port");
    private static String database = properties.getProperty("database_database");
    private static String user = properties.getProperty("database_user");
    private static String password = properties.getProperty("database_password");

    @Test
    public void testAddTimeApi() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        boolean result = dataStorage.addTimeApi("test/api", 123);
        try(Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement("SELECT count(*) n FROM api_time WHERE " +
                    "api = 'test/api' and time_execution = '123'")){
                try (ResultSet resultSet = statement.executeQuery()){
                    if (resultSet.next()) {
                        Integer n = resultSet.getInt("n");
                        boolean res = false;
                        if(n > 0){
                            res = true;
                        }
                        Assert.assertEquals(true, res);
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
                    "DELETE FROM api_time WHERE api = 'test/api' and time_execution = '123'")) {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
