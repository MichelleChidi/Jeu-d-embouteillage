package source;

import util.Contract;

public final class StdCoord implements Coord {
   
  // ATTRIBUTS

  private final int col;
  private final int row; 
    
  // CONSTRUCTEURS
  
  /**
   * Constructeur de la classe StdCoord.
   */
  public StdCoord(int row, int col) {
    Contract.checkCondition(isValidCol(col) || isValidRow(row),
        "Coordonnée invalide"); 
    this.col = col;
    this.row = row;
  }
 
  // REQUETES
   
  public int getCol() {
    return col;
  }
    
  public int getRow() {
    return row; 
  }
  
  @Override
  public boolean equals(Object coord) {  
    return coord != null
          && coord.getClass() == getClass()
          && ((Coord) coord).getCol() == col
          && ((Coord) coord).getRow() == row;
  }
    
  @Override
  public boolean isAlignedWith(Coord coord, Direction dir) {
    Contract.checkCondition(coord == null,
        "coordonnée est nul");
    Contract.checkCondition(dir == null,
        "coordonnée est nul");
    // False
    int delta = 1;
    if (dir == Direction.VERTICAL) {
      delta = Math.abs(coord.getCol() - getCol());
    }
    if (dir == Direction.HORIZONTAL) {
      delta = Math.abs(coord.getRow() - getRow());
    } 
    return delta == 0;
  }
        
  public boolean isValidCol(int col) {
    return FST_INDEX <= col && col <= LAST_INDEX;
  }
    
  public boolean isValidRow(int row) {
    return FST_INDEX <= row && row <= LAST_INDEX; 
  }
  
  //OUTILS
    
  public int hashCode() {
    return getCol();
  }
}

