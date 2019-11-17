package tss.g2.fyre.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DateConverter {
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
  public Date date() throws ParseException {
    Date date = dateFormat.parse(stringDate);
    date.setTime(date.getTime() + date.getTimezoneOffset() * 60000);
    return date;
  }
}