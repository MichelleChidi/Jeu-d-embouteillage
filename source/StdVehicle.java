package source;

import util.Contract;

public class StdVehicle implements Vehicle {
  
  
  //ATTRIBUTS 
  
  /**
   * Direction de la voiture.
   */
  private Direction direction; 
  
  /**
   * numero d'identification de la voiture.
   */
  private int id = 0; 
  /**
   * Type de voiture.
   */
  private Type type; 
  
  /**
   * Compteur de instance de classe.
   */
  private static int count = 0; 
  
  // CONSTRUCTEUR
  
  /**
   * Construire un véhicule.
   */

  public StdVehicle(Type type, Direction dir) {
    Contract.checkCondition(TRUCK_SIZE == type.getSize() 
        || type.getSize() == CAR_SIZE, "Taille invalide");
    Contract.checkCondition(dir != null,
        "direction invalide");
    direction = dir; 
    this.type = type; 
    id = count++;
  }
  
  @Override
  public int getSize() {
    return type.getSize();
  }

  @Override
  public Direction getDirection() {
    return direction;
  }

  @Override
  public boolean isRed() {
    return type == Type.RED;
  }

  @Override
  public int getId() {
    return id;
  }
  
  @Override
  public Type getType() {
    return type;
  }
  
  @Override
  public boolean equals(Object vehicle) {  
    if (vehicle != null && vehicle.getClass() == getClass()) {
      Vehicle newVech = (Vehicle) vehicle;
      return compareTo(newVech) == 0; 
    }
    return false;
  }
  
  public int hashCode() {
    return getId();
  }
  
  @Override
  public int compareTo(Vehicle vehicle) {
    // ordre croissant
    return this.getId() - vehicle.getId();
  }
}

