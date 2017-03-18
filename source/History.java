package source;

/**
   * Une interface spécifiant les fonctionnalités d'un outil de gestion
   *  d'historique.
   * Un historique est une sorte de pile chronologique bornée.
   * On peut toujours ajouter des éléments à un historique : lorsque 
   *  l'historique est plein, rajouter un nouvel élément fait disparaître
   *  le plus ancien.
   * On peut avancer et reculer le curseur repérant l'élément courant à
   *  loisir dans l'historique, mais si le curseur n'est pas sur l'élément le 
   *  plus récent, ajouter un élément dans l'historique à cet instant fait 
   *  disparaître les éléments postérieurs au curseur. 
   * @inv <pre>
   *     getMaxHeight() > 0
   *     0 <= getCurrentPosition() <= getEndPosition() 
   *     getCurrentElement() != null
   *     getCurrentElement() == l'élément à la position getCurrentPosition()
   * </pre>
   * @cons <pre>
   *     $DESC$ Un historique vide.
   *     $POST$
   *         getCurrentPosition() == 0
   *         getEndPosition() == 0 </pre>
   */

public interface History<E> {
      
  // REQUETES
        
  /**
   * La position courante dans l'historique.
   */
  int getCurrentPosition();
      
  /**
   * L'élément désigné par la position courante.
   * @pre <pre>
   *     getCurrentPosition() > 0 </pre>
   */
  E getCurrentElement();
      
  /**
   * La dernière position de l'historique.
   */
  int getEndPosition();

  // COMMANDES
      
  /**
   * Ajoute l'élément <code>e</code> à la suite de l'élément courant
   *  et supprime les éléments postérieurs à cet élément courant.
   * S'il n'y a pas d'élément courant, ajoute simplement <code>e</code>
   *  comme premier élément.
   * L'élément <code>e</code> devient le nouvel élément courant.
   * @pre <pre>
   *     elem != null </pre>
   * @post <pre>
   *     getCurrentPosition() == old getCurrentPosition() + 1
   *     getCurrentElement() == elem
   *     getEndPosition() == getCurrentPosition()
   *     si l'historique était plein, le plus ancien élément a disparu </pre>
   */
  void add(E elem);
        
  /**
   * Avance le curseur dans la direction du plus récent élément.
   * @pre <pre>
   *     getCurrentPosition() < getEndPosition() </pre>
   * @post <pre>
   *     getCurrentPosition() == old getCurrentPosition() + 1 </pre>
   */
  void goForward();
      
  /**
   * Recule le curseur dans la direction du plus ancien élément.
   * @pre <pre>
   *     getCurrentPosition() > 0 </pre>
   * @post <pre>
   *     getCurrentPosition() == old getCurrentPosition() - 1 </pre>
   */
  void goBackward();
}
