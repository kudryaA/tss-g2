package tss.g2.fyre.models.notification;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Email notification.
 */
public class EmailNotification implements Notification {

  private static Logger logger = LoggerFactory.getLogger(EmailNotification.class);
  private String username;
  private String password;
  private String subject;
  private String text;
  private String to;

  /**
   * Constructor.
   * @param properties email configs
   * @param subject subject for email
   * @param text text for email
   * @param to list of emails tp send deta(email1@a.com, email2@a.com)
   */
  public EmailNotification(Properties properties, String subject, String text, String to) {
    this.subject = subject;
    this.text = text;
    this.to = to;
    this.username = properties.getProperty("email");
    this.password = properties.getProperty("email_password");
  }

  @Override
  public boolean send() {
    boolean result = false;
    logger.info("Sending email to {} was starting", to);
    Properties prop = new Properties();
    prop.put("mail.smtp.host", "smtp.gmail.com");
    prop.put("mail.smtp.port", "587");
    prop.put("mail.smtp.auth", "true");
    prop.put("mail.smtp.starttls.enable", "true");
    Session session = Session.getInstance(prop,
        new Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
          }
        });
    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(username));
      message.setRecipients(
          Message.RecipientType.TO,
          InternetAddress.parse(to)
      );
      message.setSubject(subject);
      message.setText(text);
      Transport.send(message);
      logger.info("Sending email to {} was finishing", to);
      result = true;
    } catch (MessagingException e) {
      e.printStackTrace();
      logger.error("Sending email to {} was finishing with error {}", to, e.getMessage());
    }
    return result;
  }
}
