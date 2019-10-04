package g2.tss.fyre.models.entity;

import java.util.Objects;

/**
 * Class authorization
 * @author Anton Kudryavtsev
 */
public class Authorization {
   private String login;
   private String password;

    /**
     * Constructor
     * @param login login of user
     * @param password password of user
     */
    public Authorization(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     *
     * @return login of user
     */
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authorization that = (Authorization) o;
        return Objects.equals(login, that.login) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }

    @Override
    public String toString() {
        return "Authorization{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
