package tss.g2.fyre.utils;

import com.google.common.hash.Hashing;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
}
