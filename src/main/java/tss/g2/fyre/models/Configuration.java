package tss.g2.fyre.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    try {
      FileInputStream inputStream = new FileInputStream(new File(path));
      Map<String, Object> obj = yaml.load(inputStream);
      setProperty("database_host", obj.get("database_host"));
      setProperty("database_port", obj.get("database_port"));
      setProperty("database_database", obj.get("database_database"));
      setProperty("database_user", obj.get("database_user"));
      setProperty("database_password", obj.get("database_password"));
      setProperty("port", obj.get("port"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return properties;
  }
}
