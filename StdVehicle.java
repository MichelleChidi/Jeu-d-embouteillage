package master;

import util.Contract;

public class StdVehicle implements Vehicle {
  
  
  //ATTRIBUTS
  private int size;
  private Direction direction;  
  private int id; 
  
  // CONSTRUCTEUR
  
  /**
   * Construire un vÃ©hicule.
   */
  public StdVehicle(int id ,int size, Direction dir) {
    Contract.checkCondition(TRUCK_SIZE == size || size == CAR_SIZE,
        "Taille invalide");
    Contract.checkCondition(dir != null,
        "direction invalide");    
    this.size = size;
    direction = dir; 
    this.id = id;
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
  public boolean equals(Object vehicle) {  
    if (vehicle != null && vehicle.getClass() == getClass()) {
      Vehicle newVech = (Vehicle) vehicle;
      return newVech.getId() == this.getId(); 
    }
    return false;
  }
  
  public int hashCode() {
    return getId();
  }

	@Override
	public int compareTo(Vehicle v) {
		// ascending order
		return this.getId() - v.getId();
	}
}

