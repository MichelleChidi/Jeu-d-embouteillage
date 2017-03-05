package master;


/**
 * ModÃ©lise les vÃ©hicules.
 * 
 * @inv  
 *      CAR_SIZE <= getSize() <= TRUCK_SIZE
 *      0 <= id() < 16
 *      getDirection().equals(HORIZONTAL) ||
 *      getDirection().equals(VERTICAL)
 *      isRed() => (getId() == 0)
 * 
 * @cons 
 *      $ARGS$ int size, Direction direction, Board board
 *      $PRE$ CAR_SIZE <= size <= TRUCK_SIZE
 *      getDirection().equals(HORIZONTAL) ||
 *      getDirection().equals(VERTICAL)
 */

public interface Vehicle extends Comparable<Vehicle> {
  
  //CONSTANTES
  static final int TRUCK_SIZE = 3;
  static final int CAR_SIZE = 2;
  
  // REQUETES
  /**
   * retourne la taille de vÃ©hicule.
   */
  int getSize(); 

  /**
   * retourne la direction de vÃ©hicle.
   */
  Direction getDirection(); 

  /**
   * Indique si le vÃ©hicule est rouge.
   */
  boolean isRed(); 

  /**
   * Identification de vÃ©hicule. 
   */
  int getId();
  
  /**
   * Indique si cette coordonnÃ©e est similaire Ã  coord. 
   */
  boolean equals(Object vehicle);
  
  int compareTo(Vehicle v);
}
