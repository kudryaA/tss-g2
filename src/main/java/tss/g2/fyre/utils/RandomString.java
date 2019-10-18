package tss.g2.fyre.utils;

import java.nio.charset.Charset;
import java.util.Random;

/**
 * Generate random string.
 * @author Anton Kudryavtsev
 */
public class RandomString {
  private int length;

  /**
   * Constructor.
   * @param length length of string
   */
  public RandomString(int length) {
    this.length = length;
  }

  /**
   * Generate random string
   * @return random string
   */
  public String generate() {
    byte[] array = new byte[length];
    new Random().nextBytes(array);
    return new String(array, Charset.forName("UTF-8"));
  }
}
