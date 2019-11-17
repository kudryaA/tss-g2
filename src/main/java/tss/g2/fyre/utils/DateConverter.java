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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DateConverter that = (DateConverter) o;
    return Objects.equals(stringDate, that.stringDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stringDate);
  }
}