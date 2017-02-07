package source;

import util.Contract;

public class StdVehicles implements Vehicles {
  
  
  //ATTRIBUTS
  private int size;
  private Direction direction; 
  private Board board; 
  private int id; 
  
  // CONSTRUCTEUR
  
  /**
   * Construire un vechicle.
   */
  public StdVehicles(int size, Direction dir, Board board) {
    Contract.checkCondition(TRUCK_SIZE == size || size == CAR_SIZE,
        "Taille invalide");
    Contract.checkCondition(dir != null,
        "direction invalide");
    Contract.checkCondition(board != null,
        "plateau invalid");
    this.size = size;
    direction = dir; 
    this.board = board;
  }
  
  @Override
  public int getSize() {
    return size;
  }

  @Override
  public Direction getDirection() {
    return direction;
  }

  @Override
  public boolean isRed() {
    return id == 0;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public Board getBoard() {
    return board;
  }
  
  @Override
  public boolean equals(Object vehicle) {  
    if (vehicle != null && vehicle.getClass() == getClass()) {
      Vehicles newVech = (Vehicles) vehicle;
      return newVech.getId() == this.getId(); 
    }
    return false;
  }
  
  public int hashCode() {
    return getId();
  }
}
