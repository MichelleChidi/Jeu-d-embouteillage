package source;

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
}
