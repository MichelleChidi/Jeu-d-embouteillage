package source;

import util.Contract;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StdBoard implements Board {
  
  // ATTRIBUTS
  
  /**
  * La pile qui enregistre les actions effectuÃ©es
  */
  private Deque<Duo> u;
  
  /**
  * La pile qui enregistre les actions refaites
  */
  private Deque<Duo> r;
  
  /**
   * Le nombre de lignes sur le plateau.
   */
  private int rowNb; 
  
  /**
   * Le nombre de colonnes sur le plateau.
   */
  private int columnNb;
  
  /**
   * La coordonnÃ©e de la sortie.
   */
  private Coord exit;
  
  /**
   * maps de tous les vÃ©hicules et ses coordonnÃ©es
   * dans le plateau.
   */
  private Map<Vehicle, List<Coord>> vehicleMap; 
  private Map<Coord, Vehicle> coordMap; 
  
  // CONSTRUCTEURS
  
  /**
   * CrÃ©er un plateau de jeu par dÃ©faut.
   */
  public StdBoard() {
    columnNb = DEFAULT;
    rowNb = DEFAULT;
    exit = EXIT; 
  }
  
  /**
   * ModÃ©liser un plateau de jeu.
   */
  public StdBoard(int colNb, int rowNb, Coord exit) {
    Contract.checkCondition(DEFAULT <= colNb && colNb <= MAX,
        "Nb de colonne invalid");
    Contract.checkCondition(DEFAULT <= rowNb && rowNb <= MAX,
        "Nb de ligne invalid");
    Contract.checkCondition(isValidCol(exit.getCol()) 
        && isValidRow(exit.getCol()), "sortie invalide");
    
    columnNb = colNb;
    this.rowNb = rowNb;
    this.exit = exit;
    vehicleMap = new HashMap<Vehicle, List<Coord>>();
    coordMap = new HashMap<Coord, Vehicle>();
    u = new ArrayDeque<Duo>();
    r = new ArrayDeque<Duo>(); 	  
  }
  
  @Override
  public Coord getExit() {
    return exit;
  }
  
  public int getRowNb() {
    return rowNb;
  }
  
  public int getColNb() {
    return columnNb;
  }

  @Override
  public Set<Vehicle> getAllVehicles() {
    return vehicleMap.keySet();
  }

  @Override
  public Vehicle getVehicle(Coord coord) {
    return coordMap.get(coord);
  }

  @Override
  public List<Coord> getCoord(Vehicle vehicle) {
    return vehicleMap.get(vehicle);
  }
  
  @Override
  public boolean isFree(Coord coord) {
    return coordMap.get(coord) == null; 
  }

  @Override
  public boolean canMoveTo(Vehicle vehicle, Coord coord) {
    Contract.checkCondition(vehicle != null, "Ce vÃ©hicule n'existe pas");
    Contract.checkCondition(coord != null, "Cette coordonnÃ©e est nulle");
    return coord.isAlignedWith(vehicleMap.get(vehicle).get(0), vehicle.getDirection()) 
        && isFree(coord);
  }

  // COMMANDES
  
  @Override
  public void move(Vehicle vehicle, Coord coord) {
    moveWithoutClear(vehicle, coord);
    // Ajoute le mouvement au deque des undo, peut provoquer un problème 
    // à cause des coordonnées du véhicule
    u.push(new Duo(getCoord(vehicle).get(0), coord));
    r.clear();
  }
  
  @Override
  public void move(Coord c1, Coord c2) {
    move(getVehicle(c1), c2);
  }
  
  @Override
  public void placeVehicles(Card card) {
	// est-ce bon ?
    vehicleMap.clear();
    vehicleMap.putAll(card.getPlace());
    coordMap.clear();
    for (Vehicle vehicle : vehicleMap.keySet()) {
      for (Coord coord : vehicleMap.get(vehicle)) {
        coordMap.put(coord, vehicle);
      }
    }
    
  }
  
  public void undo() {
    // mettre les tests sur ???
    Duo d = u.pop();
    r.push(d);
    moveWithoutClear(getVehicle(d.getSecondCoord()), d.getFirstCoord());
  }
  
  public void redo() {
    Duo d = r.pop();
    u.push(d);
    moveWithoutClear(getVehicle(d.getFirstCoord()), d.getSecondCoord());
  }
  
  public boolean isValidCol(int col) {
    return 0 <= col && col <= getColNb();
  }
    
  public boolean isValidRow(int row) {
    return 0 <= row && row <= getRowNb(); 
  }
  
  // OUTILS
  
  private class Duo {

	  private Coord first;
	  private Coord second;
	
	  public Duo(Coord c1, Coord c2) {
		  first = c1;
		  second = c2;
	  }

	  public Coord getFirstCoord() {
		  return first;
	  }

	  public Coord getSecondCoord() {
		  return second;
	  }

  }
  
  private void moveWithoutClear(Vehicle vehicle, Coord coord) {
    Contract.checkCondition(canMoveTo(vehicle, coord),
        "cette action n'est pas possible");
    Contract.checkCondition(vehicleMap.get(vehicle) != null,
        "ce vehicule n'existe pas");
    Contract.checkCondition(coord.isValidCol(coord.getCol()) 
        && isValidRow(coord.getRow()),
        "cette coordonÃ©e n'est pas valide");
    List<Coord> oldCoord = vehicleMap.get(vehicle); 
    // Ancienne premiÃ¨re coordonnÃ©e du vÃ©hicule
    Coord c3 = oldCoord.get(0);
    // Ancienne derniÃ¨re coordonnÃ©e du vÃ©hicule
    Coord c4 = oldCoord.get(vehicle.getSize() - 1);
    
    // Calculer laquelle des 2 anciennes coordonnÃ©es 
    // est la plus proche de la coordonnÃ©e indiquÃ©e
    List<Coord> newCoords = new LinkedList<Coord>(); 
    if (vehicle.getDirection() == Direction.HORIZONTAL) {
      if (Math.abs(c3.getCol() - coord.getCol()) < Math.abs(c4.getCol() - coord.getCol())) {
        newCoords.add(coord);
        for (int i = 1; i < vehicle.getSize(); i++) {
          newCoords.add(new StdCoord(coord.getRow(), coord.getCol() + 1));
        }
      } else {
        for (int i = 1; i < vehicle.getSize(); i++) {
          newCoords.add(new StdCoord(coord.getRow(), coord.getCol() - 1));
        }
        newCoords.add(coord);
      }  
    } else {
      if (Math.abs(c3.getRow() - coord.getRow()) < Math.abs(c4.getRow() - coord.getRow())) {
        newCoords.add(coord);
        for (int i = 1; i < vehicle.getSize(); i++) {
          newCoords.add(new StdCoord(coord.getRow()  + 1, coord.getCol()));
        }
      } else {
        for (int i = 1; i < vehicle.getSize(); i++) {
          newCoords.add(new StdCoord(coord.getRow() - 1, coord.getCol()));
        }
        newCoords.add(coord);
      }
    }
    vehicleMap.remove(vehicle);
    vehicleMap.put(vehicle, newCoords); 
    
    for (int i = 0; i < oldCoord.size(); i++) {
      coordMap.remove(oldCoord.get(i)); 
    }
    for (int i = 0; i < newCoords.size(); i++) {
      coordMap.put(newCoords.get(i), vehicle);  
    }
  }
  
}
