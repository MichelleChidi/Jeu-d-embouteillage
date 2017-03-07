package cmd;

import source.Board;
import source.Coord;
import source.Vehicle;
import util.Contract;

public class GoBackwards extends AbstractCommand {
 
  //ATTRIBUTS
  private Vehicle vehicle; 
  private Coord newCoord;
  private Coord oldCoord;
  private Board board; 
 
  // CONSTRUCTEUR
  
  /**
   * Création de la commande GoBackwards.
   */
  public GoBackwards(Board board, Vehicle vehicle, Coord newCoord, Coord oldCoord) {
    super(board); 
    Contract.checkCondition(vehicle != null && newCoord != null
        && oldCoord != null);
    this.board = board;
    this.vehicle = vehicle;
    this.newCoord = newCoord; 
    this.oldCoord = oldCoord;
  }
 
  public Vehicle getVehicle() {
    return vehicle;
  }
 
  public Coord getNewCoord() {
    return newCoord; 
  }
  
  public Coord getOldCoord() {
    return oldCoord; 
  }
 
  @Override
  public Board getBoard() {
    return board; 
  }
 
  @Override
  public boolean canDo() {
    return super.canDo()     
        && board.canMoveTo(vehicle, newCoord)
        && (board.getCoord(vehicle).get(0).getCol() >= newCoord.getCol() 
        || board.getCoord(vehicle).get(0).getRow() <= newCoord.getRow());
  }
 
  
  @Override
  public boolean canUndo() {
    return super.canUndo() 
        && board.canMoveTo(vehicle, oldCoord)
        && (board.getCoord(vehicle).get(0).getCol() - oldCoord.getCol() <= 0 
        || board.getCoord(vehicle).get(0).getRow() - oldCoord.getRow() <= 0);
  }
 
  @Override
  public void doIt() {
    Contract.checkCondition(canDo());
    board.goBackwardsAux(vehicle, newCoord);
  }
 
  @Override
  public void undoIt() {
    Contract.checkCondition(canUndo());
    board.goForwardAux(vehicle, oldCoord);  
  } 
 
}
