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
  private boolean bannedStatus;
  private String email;
  private String role;

  /**
   * Constructor.
   *
   * @param login user login
   * @param name user name
   * @param surname user surname
   * @param bannedStatus user banned status
   * @param email user email
   * @param role user role
   */
  public Person(String login, String name, String surname,
                boolean bannedStatus, String email, String role) {
    this.login = login;
    this.name = name;
    this.surname = surname;
    this.bannedStatus = bannedStatus;
    this.email = email;
    this.role = role;
  }

  /**
   * Constructor.
   *
   * @param login user login
   * @param password user password
   * @param name user name
   * @param surname user surname
   * @param bannedStatus user banned status
   * @param email user email
   * @param role user role
   */
  public Person(String login, String password, String name,
                String surname, boolean bannedStatus, String email, String role) {
    this.login = login;
    this.password = password;
    this.name = name;
    this.surname = surname;
    this.bannedStatus = bannedStatus;
    this.email = email;
    this.role = role;
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
  public boolean getBannedStatus() {
    return bannedStatus;
  }

  /**
   * Method for get user email.
   * @return name of user
   */
  public String getEmail() {
    return email;
  }

  /**
   * Method for get user role.
   * @return user role
   */
  public String getRole() {
    return role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Person person = (Person) o;
    return bannedStatus == person.bannedStatus
            && Objects.equals(login, person.login)
            && Objects.equals(password, person.password)
            && Objects.equals(name, person.name)
            && Objects.equals(surname, person.surname)
            && Objects.equals(email, person.email)
            && Objects.equals(role, person.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(login, password, name, surname, bannedStatus, email, role);
  }

  @Override
  public String toString() {
    return "Person{"
            + "login='" + login + '\''
            + ", password='" + password + '\''
            + ", name='" + name + '\''
            + ", surname='" + surname + '\''
            + ", bannedStatus=" + bannedStatus
            + ", email='" + email + '\''
            + ", role='" + role + '\''
            + '}';
  }
}