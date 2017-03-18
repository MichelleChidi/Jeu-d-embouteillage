package source;

import util.Contract;

import java.util.ArrayDeque;
import java.util.Deque;

public class StdHistory<E> implements History<E> {
    
  // ATTRIBUTS
    
  private int currentPosition;
  private int endPosition;
  private Deque<E> undo;    
  private Deque<E> doIt;
      
  // CONSTRUCTEUR 
  
  /**
   * .
   */
  public StdHistory() {
    currentPosition = 0;
    undo = new ArrayDeque<E>();
    doIt = new ArrayDeque<E>();
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
    undo = new ArrayDeque<E>();
    currentPosition = currentPosition + 1; 
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
