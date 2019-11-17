package tss.g2.fyre.test.models.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.utils.Configuration;
import tss.g2.fyre.utils.DateConverter;
import tss.g2.fyre.utils.ToHash;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class UtilsTest {
    @Test
    public void testDate() throws ParseException {
        Date dateConverter = new DateConverter("29/03/1999 22:17:43").date();
       // Assert.assertEquals(new DateConverter("29/03/1999 22:17:43"), dateConverter);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date date = dateFormat.parse("29/03/1999 22:17:43");
        date.setTime(date.getTime() + date.getTimezoneOffset() * 60000);
        Assert.assertEquals(dateConverter, date);
    }

    @Test
    public void toHashTest() {
        String toHash = new ToHash("asd").getHash();
        Assert.assertEquals("688787d8ff144c502c7f5cffaafe2cc588d86079f9de88304c26b0cb99ce91c6", toHash);
    }

    @Before
    public void confTestStart() {
        //записать в файл конфигурацию, файл с паролем

    }
    @After
    public void confTestEnd() {
         new File("configuration.yml").delete();
         new File("password").delete();
    }
    @Test
    public void testExistConf() {
         Properties properties = new Configuration("configuration.yml").getProperties();
         String host = properties.getProperty("database_host");
         String port = properties.getProperty("database_port");
         String database = properties.getProperty("database_database");
         String user = properties.getProperty("database_user");
         String password = properties.getProperty("database_password");

    }
}
