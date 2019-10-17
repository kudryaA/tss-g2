package tss.g2.fyre.models.entity;

public class Type {
  private String typeName;
  private String description;

  /**
   * Constructor.
   *
   * @param typeName name of type
   * @param description type description
   */
  public Type(String typeName, String description) {
    this.typeName = typeName;
    this.description = description;
  }

  /**
   * Method for get name of type.
   * @return name of type
   */
  public String getTypeName() {
    return typeName;
  }

  /**
   * Method for get type description.
   * @return type description
   */
  public String getDescription() {
    return description;
  }
}
