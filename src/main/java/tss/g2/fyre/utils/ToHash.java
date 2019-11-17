package tss.g2.fyre.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Class for hashing string.
 * @author Anton Kudryavtsev
 */
public class ToHash {
  private String value;

  /**
   * Constructor.
   * @param value string for getting hash
   */
  public ToHash(String value) {
    this.value = value;
  }


  /**
   * Method for get hash of string.
   * @return hash
   */
  public String getHash()  {
    return Hashing.sha256()
        .hashString(value, StandardCharsets.UTF_8)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ToHash toHash = (ToHash) o;
    return Objects.equals(value, toHash.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
