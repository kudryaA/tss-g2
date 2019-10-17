package tss.g2.fyre.models.actions;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;

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
  public Answer getAnswer() {
    return new Answer<>(true, dataStorage.addType(typeName, description));
  }
}
