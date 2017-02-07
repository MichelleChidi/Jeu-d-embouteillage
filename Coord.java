package source;

/**
 * Type des coordonnées (non mutables).
 * 
 * <pre>
 * Par défaut: 
 *     0   1   2   3   4   5     
 *   +---+---+---+---+---+---+
 * 5 |   |   |   |   |   |   | 
 *   +---+---+---+---+---+---+
 * 4 |   |   |   |   |   |   |   
 *   +---+---+---+---+---+---+
 * 3 |   |   |   |   |   |   | 
 *   +---+---+---+---+---+---+
 * 2 |   |   |   |   |   |   | 
 *   +---+---+---+---+---+---+
 * 1 |   |   |   |   |   |   | 
 *   +---+---+---+---+---+---+
 * 0 |   |   |   |   |   |   |   
 *   +---+---+---+---+---+---+
 * </pre>
 * 
 * @inv <pre>
 *     equals(c) <==>
 *         c != null
 *         && c.getClass() == getClass()
 *         && c.getCol() == getCol()
 *         && c.getRow() == getRow()
 *     FST_INDEX <= getCol() <= LAST_INDEX
 *     FST_INDEX <= getRow() <= LAST_INDEX
 *     Soit d1 ::= abs(c.getCol() - getCol())
 *          d2 ::= abs(c.getRow() - getRow())
 *     isAlignedWith(c, dir) <==> d1 == 0 || d2 == 0
 * </pre>
 */

public interface Coord {

  // CONSTANTES
  static final int FST_INDEX = 0;
  static final int LAST_INDEX = 11;
     
  // REQUTES
  
  /**
   * Indique si cette coordonnée est similaire à coord. 
   */
  boolean equals(Object  coord); 
  
  /**
   * retourne la colonne.
   */
  int getCol();
  
  /**
   * retourne la ligne.
   */
  int getRow();
  
  /**
   * Indique si coord est alignée avec cette coordonnée (horizontalement et
   *  verticalement).
   */
  boolean isAlignedWith(Coord coord, Direction dir);
  
  /**
   * Indique si la colonne col existe.
   */
  boolean isValidCol(int col);
  
  /**
   * Indique si la ligne row existe.
   */
  boolean isValidRow(int row);
}
