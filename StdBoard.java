package source;

import util.Contract;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StdBoard implements Board {
  
  // ATTRIBUTS
  
  /**
   * Le nombre de lignes sur le plateau.
   */
  private int rowNb; 
  
  /**
   * Le nombre de colonnes sur le plateau.
   */
  private int columnNb;
  
  /**
   * La coordonnée de la sortie.
   */
  private Coord exit;
  
  /**
   * maps de tous les véhicules et ses coordonnées
   * dans le plateau.
   */
  private Map<Vehicle, List<Coord>> vehicleMap; 
  private Map<Coord, Vehicle> coordMap; 
  
  // CONSTRUCTEURS
  
  /**
   * Créer un plateau de jeu par défaut.
   */
  public StdBoard() {
    columnNb = DEFAULT;
    rowNb = DEFAULT;
    exit = EXIT; 
  }
  
  /**
   * Modéliser un plateau de jeu.
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
    Contract.checkCondition(vehicle != null, "Ce véhicule n'existe pas");
    Contract.checkCondition(coord != null, "Cette coordonnée est nulle");
    return coord.isAlignedWith(vehicleMap.get(vehicle).get(0), vehicle.getDirection()) 
        && isFree(coord);
  }

  // COMMANDES
  
  @Override
  public void move(Vehicle vehicle, Coord coord) {
    Contract.checkCondition(canMoveTo(vehicle, coord),
        "cette action n'est pas possible");
    Contract.checkCondition(vehicleMap.get(vehicle) != null,
        "ce vehicule n'existe pas");
    Contract.checkCondition(coord.isValidCol(coord.getCol()) 
        && isValidRow(coord.getRow()),
        "cette coordonée n'est pas valide");
    List<Coord> oldCoord = vehicleMap.get(vehicle); 
    // Ancienne première coordonnée du véhicule
    Coord c3 = oldCoord.get(0);
    // Ancienne dernière coordonnée du véhicule
    Coord c4 = oldCoord.get(vehicle.getSize() - 1);
    
    // Calculer laquelle des 2 anciennes coordonnées 
    // est la plus proche de la coordonnée indiquée
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
  
  @Override
  public void move(Coord c1, Coord c2) {
    move(getVehicle(c1), c2);
  }
  
  @Override
  public void placeVehicles(Card card) {
    //vehicleMap.clear()
    //vehicleMap.putAll(card.getPlace());
    coordMap.clear();
    for (Vehicle vehicle : vehicleMap.keySet()) {
      for (Coord coord : vehicleMap.get(vehicle)) {
        coordMap.put(coord, vehicle);
      }
    }
    
  }
  
  public boolean isValidCol(int col) {
    return 0 <= col && col <= getColNb();
  }
    
  public boolean isValidRow(int row) {
    return 0 <= row && row <= getRowNb(); 
  }
}
