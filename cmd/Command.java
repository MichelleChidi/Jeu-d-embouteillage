package cmd;

import source.Board;

public interface Command {
  
  //REQUETES
  /**
   * Le plateau sur lequel le commande agit.
   */
  Board getBoard();
  
  /**
   * L'état interne de la commande.
   */
  State getState(); 
  
  /**
   * Indique que la commande et son environnement sont dans un état
   *  permettant de faire la commande.
   */
  boolean canDo();
  
  /**
   * Indique que la commande et son environnement sont dans un état
   *  permettant de défaire la commande.
   */
  boolean canUndo(); 
  
  // COMMANDES
  /**
   * Définit l'action qu'effectue la commande sur le plateau associé.
   * @pre <pre>
   *     canDo() || canUndo() </pre>
   * @post <pre>
   *     getState() != old getState()
   *     old canDo()
   *         ==> la commande a fait son action sur son plateau
   *     old canUndo()
   *         ==> la commande a défait son action sur son plateau </pre>
   */
  void act(); 
}
