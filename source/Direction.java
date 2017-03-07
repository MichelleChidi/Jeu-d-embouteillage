package source;

import util.Contract;

/**
 * La classe de directions.
 *
 */
public enum Direction {
  HORIZONTAL("horizontal"),
  VERTICAL("vertical"); 

  private String direction;
  
  Direction(String dir) {
    direction = dir;
  }
  
  @Override
  public String toString() {
    return direction;
  }
  
  /**
   * Méthode pour transformer une chaîne en Type.
   */
  public static Direction fromString(String text) {
    Contract.checkCondition(text != null);
    for (Direction dir : Direction.values()) {
      if (dir.direction.equalsIgnoreCase(text)) {
        return dir;
      }
    }
    return null;
  }
}
