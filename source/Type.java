package source;

import util.Contract;

public enum Type {
  RED(2, "red"),
  CAR(2, "car"),
  TRUCK(3, "truck");
  
  private int size;
  private String name;
  
  Type(int size, String name) {
    this.size = size; 
    this.name = name;
  }
  
  public int getSize() {
    return size; 
  }
  
  public String getName() {
    return name; 
  }
  
  /**
   * Méthode pour transformer une chaîne en Type.
   */
  public static Type fromString(String text) {
    Contract.checkCondition(text != null);
    for (Type type : Type.values()) {
      if (type.name.equalsIgnoreCase(text)) {
        return type;
      }
    }
    return null;
  }
}
