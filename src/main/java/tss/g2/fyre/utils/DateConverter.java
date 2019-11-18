package tss.g2.fyre.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
  private static Logger logger = LoggerFactory.getLogger(DateConverter.class);
  private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
  private String stringDate;

  public DateConverter(String stringDate) {
    this.stringDate = stringDate;
  }

  /**
   * Method for convert string to date.
   * @return date
   * @throws ParseException problem with convert to date
   */
  public Date date() {
    Date date = null;
    try {
      date = dateFormat.parse(stringDate);
    } catch (ParseException e) {
      logger.error("Error in parsing date {} with message {}", stringDate, e.getMessage());
      date = new Date();
    }
    date.setTime(date.getTime() + date.getTimezoneOffset() * 60000);
    return date;
  }
}