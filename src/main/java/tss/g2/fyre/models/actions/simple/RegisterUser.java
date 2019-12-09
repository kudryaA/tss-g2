package tss.g2.fyre.models.actions.simple;

import java.util.Date;
import java.util.Properties;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.AnswerWithComment;
import tss.g2.fyre.models.actions.Action;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.notification.EmailNotification;
import tss.g2.fyre.utils.Configuration;
import tss.g2.fyre.utils.RandomString;
import tss.g2.fyre.utils.ToHash;

/**
 * Action class for check user registration.
 */
public class RegisterUser implements Action {
  private static Properties properties =
          new Configuration("config/configuration.yml").getProperties();
  private DataStorage dataStorage;
  private String login;
  private String password;
  private String name;
  private String surname;
  private String email;

  /**
   * Constructor.
   *
   * @param dataStorage data storage object
   * @param login user login
   * @param password user password
   * @param name user name
   * @param surname user surname
   * @param email user email
   */
  public RegisterUser(DataStorage dataStorage, String login, String password,
                      String name, String surname, String email) {
    this.dataStorage = dataStorage;
    this.login = login;
    this.password = new ToHash(password).getHash();
    this.name = name;
    this.surname = surname;
    this.email = email.replace("<", "&lt");
  }

  @Override
  public Answer getAnswer() {
    if (!login.matches("\\w{" + login.length() + "}")) {
      return new AnswerWithComment(true, false, "Login must contain only [a-z0-9_-].");
    }
    if (!name.matches("\\w{" + name.length() + "}")) {
      return new AnswerWithComment(true, false, "Name must contain only [a-z0-9_-].");
    }
    if (!surname.matches("\\w{" + surname.length() + "}")) {
      return new AnswerWithComment(true, false, "Name must contain only [a-z0-9_-].");
    }

    String key = new RandomString(20).generate() + new Date().getTime();
    boolean result = dataStorage.createUser(login, password, name, surname, email, key);

    if (result) {
      new Thread(() -> {
        String text = "Hello " + name + "\nFor confirm mail go to link: "
                + properties.getProperty("external_url") + "/confirm/mail?key=" + key;
        new EmailNotification(properties, "Mail confirm", text, email).send();
      }).start();
    }
    return new Answer<>(true, result);
  }
}
