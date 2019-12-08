package tss.g2.fyre.models.datastorage.postgress.utils.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tss.g2.fyre.models.datastorage.postgress.utils.authorization.GetUserInformation;
import tss.g2.fyre.models.entity.UserRating;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for select users rating information.
 */

public class SelectUsersRating {
    private static Logger logger = LoggerFactory.getLogger(GetUserInformation.class);

    private Connection connection;

    /**
     * Constructor.
     *
     * @param connection connection to database
     */
    public SelectUsersRating(Connection connection) {
        this.connection = connection;
    }

    /**
     * Method for select users rating information.
     * @return list with users info
     */
    public List<UserRating> selectUsersRating() {
        List<UserRating> usersInfo = new ArrayList<>();
        try (Statement selectStatement = connection.createStatement()) {
            try (ResultSet resultSet = selectStatement
                    .executeQuery("select user_login, rating from users_rating order by rating DESC")) {
                logger.info(selectStatement.toString());
                while (resultSet.next()) {
                    String login = resultSet.getString(1);
                    Long rating = resultSet.getLong(2);
                    UserRating user = new UserRating(login, rating);
                    usersInfo.add(user);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return usersInfo;
    }
}
