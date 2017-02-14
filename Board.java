package source;

import java.util.List;
import java.util.Set;

/**
 * Modélise le plateau de jeu, qui permet de déplacer les véhicules
 * 
 * @inv
 *      0 <= row <= DEFAULT &&  0 <= col <= DEFAULT
 *      || 0 <= row <= MAX &&  0 <= col <= MAX 
 * 
 * @cons <pre>
 * $ARGS$ int row, int column, Coord coord
 * $PRE$ 
 *  0 <= row <= DEFAULT
 *  0 <= col <= DEFAULT
 *  exit.equals(EXIT)
 * $POST$ 
 *      getExit().equals(EXIT)
 *      getAllVehicles().isEmpty()
 * </pre>
 * @cons <pre>
 * $ARGS$ int row, int col, Coord coord
 * $PRE$ 
 *  0 <= row <= MAX
 *  0 <= col <= MAX
 *  exit.equals(coord)
 * $POST$ 
 *      getExit().equals(coord)
 *      getAllVehicles().isEmpty()
 * </pre>
 */
public interface Board {
  
  // CONSTANTES
  
  /**
   * Le nombre maximal de lignes et de colonnes.
   */
  int MAX = 12;
  
  /**
   * Le nombre de lignes et de colonnes par défaut.
   */
  int DEFAULT = 6;
  
  /**
   * les coordonnées pour la sortie par défaut.
   */
  Coord EXIT = new StdCoord(2, 5); 
  
  // REQUETES
  
  /**
   * Indique la sortie de l'embouteillage.
   */
  Coord getExit();
  
  /**
   * retourne le nombre de lignes sur le plateau.
   */
  int getRowNb();
  
  /**
   * retourne le nombre de colonnes sur le plateau.
   */
  int getColNb();
  
  /**
   * Renvoie l'ensemble de tous les véhicules 
   * qui se trouvent sur le plateau.
   */
  Set<Vehicle> getAllVehicles();
  
  /**
   * Renvoie le vehicule dont une partie se trouve 
   * à cette coordonnée coord.
   * 
   * @pre
   *    coord != null
   */
  Vehicle getVehicle(Coord coord);
  
  /**
   * Renvoie les coordonnées associées au véhicule vehicle.
   * 
   * @pre
   *      vehicle != null
   *      getAllVehicles().contains(vehicle)
   */
  List<Coord> getCoord(Vehicle vehicle);
  
  /**
   * Indique s'il y a un véhicule les coordonnées coord est vide.
   */
  boolean isFree(Coord coord);
  
  /**
   * Indique si le véhicule v 
   * peut se déplacer sur cette coordonnée coord.
   * 
   * @pre
   *      coord != null
   *      vehicle != null
   *      getAllVehicles().contains(vehicle)
   *    
   */
  boolean canMoveTo(Vehicle vehicle, Coord coord);
  
  // COMMANDES
  
  /**
   * Déplace le véhicule vechicle vers 
   * cette coordonnée coord.
   * 
   * @pre
   *      vehicle != null
   *      coord != null
   *      canMoveTo(vehicle, coord)
   * @post
   *      getVehicle(coord) == v
   *      getCoord(vehicle) == c
   */
  void move(Vehicle vehicle, Coord coord);
  
  /**
   * Déplace le véhicule à la coordonnée c1
   *  vers la coordonnée c2
   * 
   * @pre
   *    c1 != null
   *    c2 != null
   *    getVehicle(c2) == null
   * @post
   *    getVehicle(c2) == v
   */
  void move(Coord c1, Coord c2);
  
  /**
   * Place tous les véhicules de la carte card sur le plateau.
   * 
   * @pre
   *    getAllVehicles().isEmpty()
   * @post
   *      for all vehicle in Card
   *        getCoord(vehicle) != null 
   *        && ((0,0) <= getCoord(vehicle) <= DEFAULT 
   *        ||  (0,0) <= getCoord(vehicle) <= MAX) 
   *
   */
  void placeVehicles(Card card);
  
  void undo();
  
  void redo();
}
