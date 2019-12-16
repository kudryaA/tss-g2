package tss.g2.fyre.models.actions.auth;

import io.javalin.http.UploadedFile;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.AnswerWithComment;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Roles;
import tss.g2.fyre.utils.StoreImage;

/**
 * Action for add type.
 */
public class AddType implements ActionAuth {
  private DataStorage dataStorage;
  private String typeName;
  private String description;
  private String image;

  /**
   * Construction.
   *
   * @param dataStorage data storage object
   * @param typeName name of type
   * @param description type description
   */
  public AddType(DataStorage dataStorage, String typeName, String description, UploadedFile image) {
    this.dataStorage = dataStorage;
    this.typeName = typeName.replace("<", "&lt");
    this.description = description.replace("<", "&lt");
    this.image = new StoreImage(image).store();
  }

  @Override
  public Answer getAnswer(String user, String role) {
    if (Roles.moderator.toString().equals(role) || Roles.admin.toString().equals(role)) {
      if ("".equals(typeName)) {
        return new AnswerWithComment(true, false,
                "The type name field must not be empty.");
      }
      if ("".equals(description)) {
        return new AnswerWithComment(true, false,
                "The description field must not be empty.");
      }

      return new Answer<>(true, dataStorage.addType(typeName, description, image));
    } else {
      return new AnswerWithComment(true, false,
              "You do not have permission to perform this operation.");
    }
  }
}
