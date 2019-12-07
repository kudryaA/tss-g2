package tss.g2.fyre.test.models;

import org.junit.Assert;
import org.junit.Test;
import tss.g2.fyre.models.notification.EmailNotification;
import tss.g2.fyre.utils.Configuration;

import java.util.Properties;

public class NotificationTest {

  private static Properties properties = new Configuration("config/configuration.yml").getProperties();

  @Test
  public void emailTest() {
    EmailNotification emailNotification = new EmailNotification(properties, "Test",
        "Test","andreydrus2@gmail.com,kam123ua@gmail.com");
    Assert.assertTrue(emailNotification.send());
  }
}
