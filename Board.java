package model;

import cmd.Command;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Modélise le plateau de jeu, qui permet de déplacer les véhicules
 * 
 * @inv 0 <= row <= DEFAULT && 0 <= col <= DEFAULT
 * 
 * @cons
 * 
 * 		<pre>
 * $ARGS$ int row, int column, Coord coord
 * $PRE$ 
 *  0 <= row <= DEFAULT
 *  0 <= col <= DEFAULT
 *  exit.equals(EXIT)
 * $POST$ 
 *      getExit().equals(EXIT)
 *      getAllVehicles().isEmpty()
 *       </pre>
 * 
 * @cons
 * 
 * 		<pre>
 * $ARGS$ int row, int col, Coord coord
 * $PRE$ 
 *  0 <= row <= DEFAULT
 *  0 <= col <= DEFAULT
 *  exit.equals(coord)
 * $POST$ 
 *      getExit().equals(coord)
 *      getAllVehicles().isEmpty()
 *       </pre>
 */
public interface Board {

	// CONSTANTES

	/**
	 * Le nombre de lignes et de colonnes par défaut.
	 */
	int DEFAULT = 6;

	/**
	 * les coordonnées pour la sortie par défaut.
	 */
	Coord EXIT = new StdCoord(2, 5);

	// REQUETES

	/**
	 * Indique la sortie de l'embouteillage.
	 */
	Coord getExit();

	/**
	 * Indique si le jeu est gagné c-a-d si la voiture rouge est sur la sortie.
	 */
	boolean hasWon();

	/**
	 * retourne le nombre de lignes sur le plateau.
	 */
	int getRowNb();

	/**
	 * retourne le nombre de colonnes sur le plateau.
	 */
	int getColNb();

	/**
	 * Renvoie l'historique du plateau.
	 */
	History<Command> getHistory();

	/**
	 * Retourne de nombre de annuls possibles.
	 */
	int nbOfPossibleUndo();

	/**
	 * Retourne de nombre de réetablissements possibles.
	 */
	int nbOfPossibleRedo();

	/**
	 * Renvoie l'ensemble de tous les véhicules qui se trouvent sur le plateau.
	 */
	Set<Vehicle> getAllVehicles();

	/**
	 * Renvoie le vehicule dont une partie se trouve à cette coordonnée coord.
	 * 
	 * @pre coord != null
	 */
	Vehicle getVehicle(Coord coord);

	/**
	 * Renvoie les coordonnées associées au véhicule vehicle.
	 * 
	 * @pre vehicle != null getAllVehicles().contains(vehicle)
	 */
	Coord getCoord(Vehicle vehicle);

	/**
	 * Indique s'il y a un véhicule les coordonnées coord est vide.
	 */
	boolean isFree(Coord coord);
	
	/**
	 * Indique si le v�hicule peut �tre d�poser
	 * @param v
	 * @param coord
	 * @return
	 */
	boolean isFree(Vehicle v, Coord coord);
	
	

	/**
	 * Indique si le véhicule v peut se déplacer sur cette coordonnée coord.
	 * 
	 * @pre coord != null vehicle != null getAllVehicles().contains(vehicle)
	 * 
	 */
	boolean canMoveTo(Vehicle vehicle, Coord coord);

	/**
	 * Indique si la colonne est valide.
	 */
	boolean isValidCol(int col);

	/**
	 * Indique si la ligne est valide.
	 */
	boolean isValidRow(int row);

	// COMMANDES

	/**
	 * Déplace le véhicule vechicle vers cette coordonnée coord.
	 * 
	 * @pre vehicle != null coord != null canMoveTo(vehicle, coord)
	 * @post getVehicle(coord) == v getCoord(vehicle) == c
	 */
	// void move(Vehicle vehicle, Coord coord);

	/**
	 * Avancer le véhicule aux coordonnées données.
	 * 
	 * @pre canMoveTo(vehicle, coord) vehicleMap.get(vehicle) != null
	 *      coord.isValidCol(coord.getCol()) && isValidRow(coord.getRow()
	 * 
	 *      vehicle).get(0).getCol() - coord.getCol() < 0 ||
	 *      vehicleMap.get(vehicle).get(0).getRow() - coord.getRow() < 0
	 * 
	 * @post coord ∈ vehicleMap.get(vehicle)
	 */
	void goForward(Vehicle vehicle, Coord coord);

	/**
	 * Méthode auxilliare pour avancer le véhicule aux coordonnées données.
	 * 
	 * @pre canMoveTo(vehicle, coord) vehicleMap.get(vehicle) != null
	 *      coord.isValidCol(coord.getCol()) && isValidRow(coord.getRow()
	 * 
	 *      vehicle).get(0).getCol() - coord.getCol() < 0 ||
	 *      vehicleMap.get(vehicle).get(0).getRow() - coord.getRow() < 0
	 * 
	 * @post coord ∈ vehicleMap.get(vehicle)
	 */
	void goForwardAux(Vehicle vehicle, Coord coord);

	/**
	 * Reculer le véhicule aux coordonnées données.
	 * 
	 * @pre canMoveTo(vehicle, coord) vehicleMap.get(vehicle) != null
	 *      coord.isValidCol(coord.getCol()) && isValidRow(coord.getRow())
	 * 
	 *      vehicleMap.get(vehicle).get(0).getCol() - coord.getCol() > 0 ||
	 *      vehicleMap.get(vehicle).get(0).getRow() - coord.getRow() > 0
	 * 
	 * @post coord ∈ vehicleMap.get(vehicle)
	 */

	void goBackwards(Vehicle vehicle, Coord coord);

	/**
	 * Méthode auxilliare pour reculer le véhicule aux coordonnées données.
	 * 
	 * @pre canMoveTo(vehicle, coord) vehicleMap.get(vehicle) != null
	 *      coord.isValidCol(coord.getCol()) && isValidRow(coord.getRow())
	 * 
	 *      vehicleMap.get(vehicle).get(0).getCol() - coord.getCol() > 0 ||
	 *      vehicleMap.get(vehicle).get(0).getRow() - coord.getRow() > 0
	 * 
	 * @post coord ∈ vehicleMap.get(vehicle)
	 */

	void goBackwardsAux(Vehicle vehicle, Coord coord);

	/**
	 * Déplace le véhicule à la coordonnée c1 vers la coordonnée c2.
	 * 
	 * @pre c1 != null c2 != null getVehicle(c2) == null
	 * @post getVehicle(c2) == v
	 */
	// void move(Coord c1, Coord c2);

	/**
	 * Place tous les véhicules de la carte card sur le plateau.
	 * 
	 * @pre getAllVehicles().isEmpty()
	 * @post for all vehicle in Card getCoord(vehicle) != null && ((0,0) <=
	 *       getCoord(vehicle) <= DEFAULT || (0,0) <= getCoord(vehicle) <= MAX)
	 *
	 */
	void placeVehicles(Card card);

	/**
	 * réer une carte.
	 * 
	 * @pre file != null card != null
	 * @post le fichier carte est crée
	 *
	 */
	void writeInFile(File file, Card card);

	/**
	 * Refaire une action.
	 */
	void redo();

	/**
	 * Annuler une action.
	 */
	void undo();
}