package source;

import util.Contract;

import java.util.ArrayDeque;
import java.util.Deque;

public class StdHistory<E> implements History<E> {
    
  // ATTRIBUTS
    
  private final int maximumHeight; 
  private int currentPosition;
  private int endPosition;
  private Deque<E> undo;    
  private Deque<E> doIt;
      
  // CONSTRUCTEUR 
  
  /**
   * .
   */
  public StdHistory(int maxHeight) {
    Contract.checkCondition(maxHeight > 0);
    maximumHeight = maxHeight;
    currentPosition = 0;
    undo = new ArrayDeque<E>(maximumHeight);
    doIt = new ArrayDeque<E>(maximumHeight);
  }
  
  @Override
  public int getMaxHeight() {
    return maximumHeight;
  }

  @Override
  public int getCurrentPosition() {
    return currentPosition;
  }

  @Override
  public E getCurrentElement() {
    return doIt.getFirst(); 
  }

  @Override
  public int getEndPosition() {
    return endPosition;
  }

  @Override
  public void add(E elem) {
    Contract.checkCondition(elem != null);
    if (currentPosition == maximumHeight) {
      doIt.removeLast();
    }
    undo = new ArrayDeque<E>(maximumHeight);
    currentPosition = Math.min(currentPosition + 1, maximumHeight); 
    endPosition = currentPosition;
    doIt.addFirst(elem);   
  }

  @Override
  public void goForward() {
    Contract.checkCondition(getCurrentPosition() < getEndPosition());
    doIt.addFirst(undo.getFirst());
    undo.removeFirst();
    currentPosition += 1;
  }

  @Override
  public void goBackward() {
    Contract.checkCondition(getCurrentPosition() > 0);
    undo.add(doIt.getFirst());
    doIt.removeFirst();
    currentPosition -= 1;
  }    
}