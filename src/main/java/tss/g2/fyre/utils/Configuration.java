package tss.g2.fyre.utils;

import com.google.common.io.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

import org.yaml.snakeyaml.Yaml;

/**
 * Class for read properties.
 *
 * @author Anton Kudryavysev
 */
public class Configuration {
  private String path;
  private Properties properties;

  /**
   * Constructor.
   *
   * @param path path to file with configuration
   */
  public Configuration(String path) {
    this.path = path;
  }

  private void setProperty(String key, Object value) {
    if (value != null) {
      properties.setProperty(key, value.toString());
    }
  }

  /**
   * Get properties.
   * @return properties
   */
  public Properties getProperties() {
    properties = new Properties();
    Yaml yaml = new Yaml();
    properties.setProperty("database_host", "127.0.0.1");
    properties.setProperty("database_port", "5432");
    properties.setProperty("database_database", "postgres");
    properties.setProperty("database_user", "postgres");
    properties.setProperty("database_password", "postgress");
    properties.setProperty("port", "7000");
    properties.setProperty("email", "test@gmail.com");
    properties.setProperty("email_password", "password");
    properties.setProperty("external_url", "https://g2.sumdu-tss.site");
    try {
      FileInputStream inputStream = new FileInputStream(new File(path));
      Map<String, Object> obj = yaml.load(inputStream);
      setProperty("database_host", obj.get("database_host"));
      setProperty("database_port", obj.get("database_port"));
      setProperty("database_database", obj.get("database_database"));
      setProperty("database_user", obj.get("database_user"));
      setProperty("email", obj.get("email"));
      setProperty("email_password", obj.get("email_password"));
      String passwordPath = obj.get("database_password").toString();
      String password = Files.readLines(new File(passwordPath), StandardCharsets.UTF_8).get(0);
      setProperty("database_password", password);
      setProperty("port", obj.get("port"));
      setProperty("external_url", obj.get("external_url"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return properties;
  }
}
