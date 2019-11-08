package tss.g2.fyre.utils;

import com.google.common.io.Files;
import io.javalin.http.UploadedFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class StoreImage {
  private UploadedFile uploadedFile;

  public StoreImage(UploadedFile uploadedFile) {
    this.uploadedFile = uploadedFile;
  }

  /**
   * Method for saving image to internal folder.
   * @return path to image
   */
  public String store() {
    String path = "unnamed";

    try {
      InputStream initialStream = uploadedFile.getContent();
      byte[] buffer = new byte[initialStream.available()];
      initialStream.read(buffer);
      path = generatePath();
      File targetFile = new File("images/" + path);
      Files.write(buffer, targetFile);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return path;
  }

  private static String generatePath() {
    return new RandomString(100).generate();
  }
}
