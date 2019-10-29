package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;

/**
 * Action for add type.
 */
public class AddType implements Action {
  private DataStorage dataStorage;
  private String typeName;
  private String description;

  /**
   * Construction.
   *
   * @param dataStorage data storage object
   * @param typeName name of type
   * @param description type description
   */
  public AddType(DataStorage dataStorage, String typeName, String description) {
    this.dataStorage = dataStorage;
    this.typeName = typeName;
    this.description = description;
  }

  @Override
  public Answer getAnswer(String user) {
    return new Answer<>(true, dataStorage.addType(typeName, description));
  }
}
