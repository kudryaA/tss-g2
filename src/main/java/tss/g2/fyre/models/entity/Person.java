package tss.g2.fyre.models.entity;

import java.util.Objects;

/**
 * Class authorization.
 *
 * @author Anton Kudryavtsev
 */
public class Person {
  private String login;
  private String password;
  private String name;
  private String surname;
  private String bannedStatus;
  private String email;

  /**
   * Constructor.
   *
   * @param login user login
   * @param password user password
   * @param name user name
   * @param surname user surname
   * @param bannedStatus user banned status
   * @param email user email
   */
  public Person(String login, String password, String name,
                String surname, String bannedStatus, String email) {
    this.login = login;
    this.password = password;
    this.name = name;
    this.surname = surname;
    this.bannedStatus = bannedStatus;
    this.email = email;
  }

  /**
   * Method for get login of user.
   * @return login of user
   */
  public String getLogin() {
    return login;
  }

  /**
   * Method for get password of user.
   * @return password of user
   */
  public String getPassword() {
    return password;
  }

  /**
   * Method for get user name.
   * @return name of user
   */
  public String getName() {
    return name;
  }

  /**
   * Method for get user surname.
   * @return name of user
   */
  public String getSurname() {
    return surname;
  }

  /**
   * Method for get user banned status.
   * @return name of user
   */
  public String getBannedStatus() {
    return bannedStatus;
  }

  /**
   * Method for get user email.
   * @return name of user
   */
  public String getEmail() {
    return email;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Person that = (Person) o;
    return Objects.equals(login, that.login)
            && Objects.equals(password, that.password)
            && Objects.equals(name, that.name)
            && Objects.equals(surname, that.surname)
            && Objects.equals(bannedStatus, that.bannedStatus)
            && Objects.equals(email, that.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(login, password, name, surname, bannedStatus, email);
  }

  @Override
  public String toString() {
    return "Person{"
            + "login='" + login + '\''
            + ", password='" + password + '\''
            + ", name='" + name + '\''
            + ", surname='" + surname + '\''
            + ", bannedStatus='" + bannedStatus + '\''
            + ", email='" + email + '\''
            + '}';
  }
}