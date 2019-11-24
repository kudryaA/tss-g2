package tss.g2.fyre.models.entity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Type class.
 */
public class Type implements Comparable<Type>{
  private String typeName;
  private String description;
  private String image;

  /**
   * Constructor.
   *
   * @param typeName name of type
   * @param description type description
   * @param image path to image
   */
  public Type(String typeName, String description, String image) {
    this.typeName = typeName;
    this.description = description;
    this.image = image;
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

  /**
   * Method for get path to image.
   * @return path to image
   */
  public String getImage() {
    return image;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Type type = (Type) o;
    return Objects.equals(typeName, type.typeName)
            && Objects.equals(description, type.description)
            && Objects.equals(image, type.image);
  }

  @Override
  public int hashCode() {
    return Objects.hash(typeName, description, image);
  }

  @Override
  public String toString() {
    return "Type{"
            + "typeName='" + typeName + '\''
            + ", description='" + description + '\''
            + ", image='" + image + '\''
            + '}';
  }

  @Override
  public int compareTo(@NotNull Type o) {
    return this.getTypeName().compareTo(o.getTypeName());
  }
}
