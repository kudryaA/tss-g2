package tss.g2.fyre.models.entity;

import java.util.Objects;

/**
 * UserRating class.
 */

public class UserRating {
    private String login;
    private Long rating;
    /**
     * Constructor.
     *
     * @param login user login
     * @param rating user rating
     */
    public UserRating(String login, Long rating) {
        this.login = login;
        this.rating = rating;
    }


    /**
     * Method for get login of user.
     * @return login of user
     */
    public String getLogin() {
        return login;
    }

    /**
     * Method for get rating of user.
     * @return rating of user
     */
    public Long getRating() {
        return rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRating that = (UserRating) o;
        return Objects.equals(login, that.login) &&
                Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, rating);
    }

    @Override
    public String toString() {
        return "UserRating{" +
                "login='" + login + '\'' +
                ", rating=" + rating +
                '}';
    }
}
