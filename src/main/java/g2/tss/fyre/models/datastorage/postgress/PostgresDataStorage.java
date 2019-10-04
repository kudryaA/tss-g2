package g2.tss.fyre.models.datastorage.postgress;

import g2.tss.fyre.models.datastorage.DataStorage;
import g2.tss.fyre.models.entity.Authorization;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Postgres data storage
 * @author Anton Kudryavtsev
 */
public class PostgresDataStorage implements DataStorage {

    private Connection connection = null;

    public PostgresDataStorage() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://127.0.0.1:5432/test", "postgres", "password");
    }

    @Override
    public Authorization getAuthorization(String login) {
        return new PostgresGetAuthorization(connection, login).getAuthorization();
    }
}
