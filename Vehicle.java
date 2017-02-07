package source;


/**
 * Modélise les véhicules.
 * 
 * @inv  
 *      CAR_SIZE <= getSize() <= TRUCK_SIZE
 *      getBoard() != null
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
 *      board != null
 *
 */

public interface Vehicle {
  
  //CONSTANTES
  static final int TRUCK_SIZE = 3;
  static final int CAR_SIZE = 2;
  
  // REQUETES
  /**
   * retourne la taille de véhicule.
   */
  int getSize(); 

  /**
   * retourne la direction de véhicle.
   */
  Direction getDirection(); 

  /**
   * Indique si le véhicule est rouge.
   */
  boolean isRed(); 

  /**
   * Identification de véhicule. 
   */
  int getId();
  
  /**
   * retourne le plateu où ce véhicule se situe.
   */
  Board getBoard(); 
  
  /**
   * retourne la position de ce véhicule.
   */
  //Coord getPosition(); 
  
  /**
   * Indique si cette coordonnée est similaire à coord. 
   */
  boolean equals(Object vechile);
}

