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
   * Generate random string.
   * @return random string
   */
  public String generate() {
    int leftLimit = 97; // letter 'a'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = length;
    Random random = new Random();
    StringBuilder buffer = new StringBuilder(targetStringLength);
    for (int i = 0; i < targetStringLength; i++) {
      int randomLimitedInt = leftLimit + (int)
          (random.nextFloat() * (rightLimit - leftLimit + 1));
      buffer.append((char) randomLimitedInt);
    }
    return buffer.toString();
  }
}
