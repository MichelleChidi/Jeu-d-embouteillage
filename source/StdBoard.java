package source;

import cmd.Command;
import cmd.GoBackwards;
import cmd.GoForward;
import util.Contract;

import java.io.File;
import java.io.IOException;
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
   * La historique.
   */
  private History<Command> history; 
  
  /**
   * Nombre des annuls possibles.
   */
  private int nbOfPossibleUndo;
  
  /**
   * Nombre des réetablissement possibles.
   */
  private int nbOfPossibleRedo;
  
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
    history = new StdHistory<Command>();
    vehicleMap = new HashMap<Vehicle, List<Coord>>();
    coordMap = new HashMap<Coord, Vehicle>();
    nbOfPossibleUndo = 0;
    nbOfPossibleRedo = 0;
  }
  
  @Override
  public Coord getExit() {
    return exit;
  }

  @Override
  public boolean hasWon() {
    if (coordMap.get(getExit()).isRed()) {
      return true;
    }
    return false;
  }
  
  @Override
  public int getRowNb() {
    return rowNb;
  }
  
  @Override
  public int getColNb() {
    return columnNb;
  }

  @Override
  public History<Command> getHistory() {
    return history; 
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
  public int nbOfPossibleUndo() {
    return nbOfPossibleUndo;
  }

  @Override
  public int nbOfPossibleRedo() {
    return nbOfPossibleRedo;
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
        && checkAllFreeCoords(vehicle, coord);
  }
  
  @Override
  public boolean isValidCol(int col) {
    return 0 <= col && col <= getColNb();
  }
   
  @Override
  public boolean isValidRow(int row) {
    return 0 <= row && row <= getRowNb(); 
  }

  // COMMANDES
  
  @Override
  public void goForward(Vehicle vehicle, Coord coord) {
    Contract.checkCondition(canMoveTo(vehicle, coord),
        "cette action n'est pas possible");
    Contract.checkCondition(vehicleMap.get(vehicle) != null,
        "ce vehicule n'existe pas");
    Contract.checkCondition(coord.isValidCol(coord.getCol()) 
        && isValidRow(coord.getRow()),
        "cette coordonée n'est pas valide");
    Contract.checkCondition(checkForwardDirection(vehicle, coord), 
        "action impossible");
    
    Command cmd = new GoForward(this, vehicle, coord, vehicleMap.get(vehicle).get(0));
    history.add(cmd);
    cmd.act();
    nbOfPossibleUndo += 1;
  }
  
  @Override
  public void goBackwards(Vehicle vehicle, Coord coord) {
    Contract.checkCondition(canMoveTo(vehicle, coord),
        "cette action n'est pas possible");
    Contract.checkCondition(vehicleMap.get(vehicle) != null,
        "ce vehicule n'existe pas");
    Contract.checkCondition(coord.isValidCol(coord.getCol()) 
        && isValidRow(coord.getRow()),
        "cette coordonée n'est pas valide");    
    Contract.checkCondition(checkBackwardsDirection(vehicle, coord), 
        "action impossible"); 
    
 
    Command cmd = new GoBackwards(this, vehicle, coord, vehicleMap.get(vehicle).get(0));
    history.add(cmd);
    cmd.act();
    nbOfPossibleUndo += 1;
  }
  
  @Override
  public void placeVehicles(Card card) {
    Contract.checkCondition(card != null);
    vehicleMap.clear();
    vehicleMap.putAll(card.getPlaces());
    coordMap.clear();
    for (Vehicle vehicle : vehicleMap.keySet()) {
      for (Coord coord : vehicleMap.get(vehicle)) {
        coordMap.put(coord, vehicle);
      }
    }
    
  }
  
  @Override
  public void writeInFile(File file, Card card) {
    Contract.checkCondition(file != null && card != null);
    card.setFile(file);
    try {
      card.save(vehicleMap);
    } catch (IOException exception) {
      // TODO Auto-generated catch block
      exception.printStackTrace();
    }
  }
  
  @Override
  public void redo() {
    Contract.checkCondition(nbOfPossibleRedo > 0);
    nbOfPossibleUndo += 1;
    nbOfPossibleRedo -= 1;
    history.getCurrentElement().act();
    history.goForward();
  }

  @Override
   public void undo() {
    Contract.checkCondition(nbOfPossibleUndo > 0);
    nbOfPossibleUndo -= 1;
    nbOfPossibleRedo += 1;
    history.getCurrentElement().act();
    history.goBackward();
  }
  
  @Override
  public void goForwardAux(Vehicle vehicle, Coord coord) {
    Contract.checkCondition(canMoveTo(vehicle, coord),
        "cette action n'est pas possible");
    Contract.checkCondition(vehicleMap.get(vehicle) != null,
        "ce vehicule n'existe pas");
    Contract.checkCondition(coord.isValidCol(coord.getCol()) 
        && isValidRow(coord.getRow()),
        "cette coordonée n'est pas valide");
    Contract.checkCondition(checkForwardDirection(vehicle, coord), 
        "action impossible");
    
    // Calculer laquelle des 2 anciennes coordonnées 
    // est la plus proche de la coordonnée indiquée 
    List<Coord> newCoords = new LinkedList<Coord>(); 
    if (vehicle.getDirection() == Direction.HORIZONTAL) {
      for (int i = 1; i < vehicle.getSize(); i++) {
        newCoords.add(new StdCoord(coord.getRow(), coord.getCol() - 1));
      }
      newCoords.add(coord);
    } else {
      newCoords.add(coord);
      for (int i = 1; i < vehicle.getSize(); i++) {
        newCoords.add(new StdCoord(coord.getRow() + 1, coord.getCol()));
      }
    }
    List<Coord> oldCoords = vehicleMap.get(vehicle); 
    
    vehicleMap.remove(vehicle);
    vehicleMap.put(vehicle, newCoords); 
    
    
    for (int i = 0; i < oldCoords.size(); i++) {
      coordMap.remove(oldCoords.get(i)); 
    }
    
    for (int i = 0; i < newCoords.size(); i++) {
      coordMap.put(newCoords.get(i), vehicle);  
    }
  }
  
  @Override
  public void goBackwardsAux(Vehicle vehicle, Coord coord) {
    Contract.checkCondition(canMoveTo(vehicle, coord),
        "cette action n'est pas possible");
    Contract.checkCondition(vehicleMap.get(vehicle) != null,
        "ce vehicule n'existe pas");
    Contract.checkCondition(coord.isValidCol(coord.getCol()) 
        && isValidRow(coord.getRow()),
        "cette coordonée n'est pas valide");   
    Contract.checkCondition(checkBackwardsDirection(vehicle, coord), 
        "action impossible"); 
    
    // Calculer laquelle des 2 anciennes coordonnées 
    // est la plus proche de la coordonnée indiquée
    List<Coord> newCoords = new LinkedList<Coord>();
    
    if (vehicle.getDirection() == Direction.HORIZONTAL) {
      newCoords.add(coord);
      for (int i = 1; i < vehicle.getSize(); i++) {
        newCoords.add(new StdCoord(coord.getRow(), coord.getCol() + 1));
      }
    } else {
      for (int i = 1; i < vehicle.getSize(); i++) {
        newCoords.add(new StdCoord(coord.getRow() - 1, coord.getCol()));
      }
      newCoords.add(coord);
    }
    
    List<Coord> oldCoords = vehicleMap.get(vehicle); 
    vehicleMap.remove(vehicle);
    vehicleMap.put(vehicle, newCoords); 
    
    for (int i = 0; i < oldCoords.size(); i++) {
      coordMap.remove(oldCoords.get(i)); 
    }
    
    for (int i = 0; i < newCoords.size(); i++) {
      coordMap.put(newCoords.get(i), vehicle);  
    }
  }
  
  // OUTILS
  /**
   * Indique si le mouvement goBackwards (reculer) est possible.
   */
  private boolean checkBackwardsDirection(Vehicle vehicle, Coord newCoord) {
    assert vehicle != null && newCoord != null;
    if (vehicle.getDirection() == Direction.HORIZONTAL) {
      return vehicleMap.get(vehicle).get(0).getRow() == newCoord.getRow() 
          && vehicleMap.get(vehicle).get(0).getCol() >= newCoord.getCol();
    } else {
      return vehicleMap.get(vehicle).get(0).getRow() <= newCoord.getRow() 
          && vehicleMap.get(vehicle).get(0).getCol() == newCoord.getCol();
    }
  }
  
  /**
   * Indique si le mouvement goForward (avancer) est possible.
   */
  private boolean checkForwardDirection(Vehicle vehicle, Coord newCoord) {
    assert vehicle != null && newCoord != null;
    if (vehicle.getDirection() == Direction.HORIZONTAL) {
      return vehicleMap.get(vehicle).get(0).getRow() == newCoord.getRow() 
          && vehicleMap.get(vehicle).get(0).getCol() <= newCoord.getCol();
    } else {
      return vehicleMap.get(vehicle).get(0).getRow() >= newCoord.getRow() 
          && vehicleMap.get(vehicle).get(0).getCol() == newCoord.getCol();
    }
  }
  
  private boolean checkAllFreeCoords(Vehicle vehicle, Coord coord) {
    int difference; 
    if (vehicleMap.get(vehicle).get(0).getRow() != coord.getRow()) {
      difference = (vehicleMap.get(vehicle).get(0).getRow() - coord.getRow()) - 1;
    } else {
      difference = (vehicleMap.get(vehicle).get(0).getCol() - coord.getCol()) - 1;
    }
    if (checkForwardDirection(vehicle, coord)) {
      for (int i = 0; i < difference; i++) {
        if (vehicle.getDirection() == Direction.VERTICAL) {
          if (!isFree(new StdCoord(coord.getRow() + 1, coord.getCol()))) {
            return false; 
          }
        } else {
          if (!isFree(new StdCoord(coord.getRow(), coord.getCol() - 1))) {
            return false; 
          }
        }
      }
    } else {
      for (int i = 0; i < difference; i++) {
        if (vehicle.getDirection() == Direction.VERTICAL) {
          if (!isFree(new StdCoord(coord.getRow() - 1, coord.getCol()))) {
            return false; 
          }
        } else {
          if (!isFree(new StdCoord(coord.getRow(), coord.getCol() + 1))) {
            return false; 
          }
        }
      }
    }
    return true;
  }
}
