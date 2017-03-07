package cmd;

import source.Board;
import util.Contract;

public abstract class AbstractCommand implements Command {
  
  // ATTRIBUTS
  
  private State state; 
  private final Board board; 
  
  // CONSTRUCTEUR
  
  protected AbstractCommand(Board board) {
    Contract.checkCondition(board != null, "Le plateau est null");
    this.board = board; 
    state = State.DO; 
  }
  
  // REQUETES
  
  @Override
  public Board getBoard() {
    return board;
  }

  @Override
  public State getState() {
    return state;
  }

  @Override
  public boolean canDo() {
    return state == State.DO;
  }

  @Override
  public boolean canUndo() {
    return state == State.UNDO;
  }

  // COMMANDES
  
  @Override
  public void act() {
    Contract.checkCondition(canDo() || canUndo(), "Action pas possible");
    if (canDo()) {      
      doIt();
      state = State.UNDO;
    } else { 
      undoIt();
      state = State.DO;
    }
  }
  
  /**
   * Cette méthode doit être redéfinie dans les sous-classes, de sorte
   *  qu'elle implante l'action à réaliser pour exécuter la commande.
   * Elle est appelée par act() et ne doit pas être appelée directement.
   * @pre
   *     canDo()
   * @post
   *     La commande a été exécutée
   */
  protected abstract void doIt();
  
  /**
   * Cette méthode doit être redéfinie dans les sous-classes, de sorte
   *  qu'elle implante l'action à réaliser pour annuler la commande.
   * Si l'état du texte correspond à celui dans lequel il était après doIt,
   *  alors undoIt rétablit le texte dans l'état où il était avant
   *  l'exécution de doIt.
   * Elle est appelée par act() et ne doit pas être appelée directement.
   * @pre
   *     canUndo()
   * @post
   *     La commande a été annulée
   */
  protected abstract void undoIt();
  
  

}
