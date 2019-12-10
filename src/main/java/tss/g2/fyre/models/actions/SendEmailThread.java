package tss.g2.fyre.models.actions;

import java.util.List;
import java.util.Properties;

import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.notification.EmailNotification;
import tss.g2.fyre.utils.Configuration;

public class SendEmailThread implements Runnable {

  private static Properties properties =
        new Configuration("config/configuration.yml").getProperties();
  private DataStorage dataStorage;
  private String user;
  private String recipeId;
  private String recipeName;

  /**
   * Constructor.
   * @param dataStorage data storage object
   * @param user user login
   * @param recipeId recipe id
   * @param recipeName recipe name
   */
  public SendEmailThread(DataStorage dataStorage, String user, String recipeId, String recipeName) {
    this.dataStorage = dataStorage;
    this.user = user;
    this.recipeId = recipeId;
    this.recipeName = recipeName;
  }

  @Override
  public void run() {
    List<String> emailList = dataStorage.selectSubscribers(user);
    StringBuilder emails = new StringBuilder();
    emailList.forEach(email -> emails.append(email).append(","));
    if ("".equals(emailList.toString())) {
      String emailsList = emails.toString().substring(0, emails.toString().length() - 1);
      String text = "Author " + user + " add new recipe.\nRecipe name is "
              + recipeName + ".\nLink: " + properties.getProperty("external_url")
              + "/recipe?recipeId=" + recipeId + ".";

      new EmailNotification(properties, "New recipe notification", text, emailsList).send();
    }
  }
}
