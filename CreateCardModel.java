package model;

import java.util.Set;

public interface CreateCardModel {

	/**
	 * Renvoie l'ensemble de tous les véhicules qui se trouvent sur le plateau.
	 */
	Set<Vehicle> getAllVehicles();

	/**
	 * Renvoie le vehicule dont une partie se trouve à cette coordonnée coord.
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
	boolean isFree(Vehicle veh, Coord coord);
	
	/**
	 * Permet de tester le model en mode console
	 * @return affiche du plateau
	 */
	String toString();
	
	/**
	 * Ajoute un véhicle à la coordonnée donnée
	 */
	void addVehicle(Vehicle veh, Coord coord);
	
	/**
	 * Supprime un véhicle à la coordonnée donnée
	 */
	void removeVehicle(Vehicle veh, Coord coord);
	
	/**
	 * Supprime tout les véhicules sur le plateau mais ne remet pas la voiture rouge à sa position d'origne : Coord(2,0).
	 */
	void clear();
}
