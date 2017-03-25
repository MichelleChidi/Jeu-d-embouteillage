package model;

import java.util.Set;

public interface CreateCardModel {

	/**
	 * Renvoie l'ensemble de tous les v�hicules qui se trouvent sur le plateau.
	 */
	Set<Vehicle> getAllVehicles();

	/**
	 * Renvoie le vehicule dont une partie se trouve �cette coordonn�e coord.
	 * 
	 * @pre coord != null
	 */
	Vehicle getVehicle(Coord coord);

	/**
	 * Renvoie les coordonn�es associ�es au v�hicule vehicle.
	 * 
	 * @pre vehicle != null getAllVehicles().contains(vehicle)
	 */
	Coord getCoord(Vehicle vehicle);
	
	/**
	 * Indique s'il y a un v�hicule les coordonn�es coord est vide.
	 */
	boolean isFree(Vehicle veh, Coord coord);
	
	/**
	 * Permet de tester le model en mode console
	 * @return affiche du plateau
	 */
	String toString();
	
	/**
	 * Ajoute un v�hicle � la coordonn�e donn�e
	 */
	void addVehicle(Vehicle veh, Coord coord);
	
	/**
	 * Supprime un v�hicle � la coordonn�e donn�e
	 */
	void removeVehicle(Vehicle veh, Coord coord);
	
	/**
	 * Supprime tout les v�hicules sur le plateau mais ne remet pas la voiture rouge � sa position d'origne : Coord(2,0).
	 */
	void clear();
}
