 package model;

import cmd.Command;
import cmd.GoBackwards;
import cmd.GoForward;
import util.Contract;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StdBoard implements Board {

	// ATTRIBUTS

	/**
	 * Le nombre de lignes sur le plateau.
	 */
	private int rowNb;

	/**
	 * Le nombre de colonnes sur le plateau.
	 */
	private int columnNb;

	/**
	 * La coordonnée de la sortie.
	 */
	private Coord exit;

	/**
	 * La historique.
	 */
	private History<Command> history;

	/**
	 * Nombre des annuls possibles.
	 */
	private int nbOfPossibleUndo;

	/**
	 * Nombre des réetablissement possibles.
	 */
	private int nbOfPossibleRedo;

	/**
	 * maps de tous les véhicules et ses coordonnées dans le plateau.
	 */
	private Map<Vehicle, Coord> vehicleMap;
	private Map<Coord, Vehicle> coordMap;

	// CONSTRUCTEURS

	/**
	 * Créer un plateau de jeu par défaut.
	 */
	public StdBoard() {
		columnNb = DEFAULT;
		rowNb = DEFAULT;
		exit = EXIT;
		history = new StdHistory<Command>(10);
		vehicleMap = new HashMap<Vehicle, Coord>();
		coordMap = new HashMap<Coord, Vehicle>();
		nbOfPossibleUndo = 0;
		nbOfPossibleRedo = 0;
	}

	public Coord getExit() {
		return exit;
	}

	public boolean hasWon() {
		if (coordMap.get(getExit()).isRed()) {
			return true;
		}
		return false;
	}

	public int getRowNb() {
		return rowNb;
	}

	public int getColNb() {
		return columnNb;
	}

	public History<Command> getHistory() {
		return history;
	}
	
	public Set<Vehicle> getAllVehicles() {
		return vehicleMap.keySet();
	}
	
	public Vehicle getVehicle(Coord coord) {
		return coordMap.get(coord);
	}

	public int nbOfPossibleUndo() {
		return nbOfPossibleUndo;
	}

	public int nbOfPossibleRedo() {
		return nbOfPossibleRedo;
	}

	public Coord getCoord(Vehicle vehicle) {
		return vehicleMap.get(vehicle);
	}
	
	public boolean isFree(Coord coord) {
		return coordMap.get(coord) == null;
	}

	public boolean isFree(Vehicle v, Coord coord) {
		boolean test = true;
		int row = coord.getRow();
		int col = coord.getCol();
		
		if (v.getDirection() == Direction.HORIZONTAL) {
			for (int i = 0; i < v.getSize(); i++ ) {
				if (coordMap.get(new StdCoord(row, col+i)) != null) {
					test = false;
				}
			}
		} else { // véhicule vertical
			for (int i = 0; i < v.getSize(); i++ ) {
				if (coordMap.get(new StdCoord(row+i, col)) != null) {
					test = false;
				}
			}
		}
		return test;
	}

	public boolean canMoveTo(Vehicle vehicle, Coord coord) {
		Contract.checkCondition(vehicle != null, "Ce véhicule n'existe pas");
		Contract.checkCondition(coord != null, "Cette coordonnée est nulle");
		return coord.isAlignedWith(vehicleMap.get(vehicle), vehicle.getDirection())
				&& checkAllFreeCoords(vehicle, coord);
	}

	public boolean isValidCol(int col) {
		return 0 <= col && col <= getColNb();
	}

	public boolean isValidRow(int row) {
		return 0 <= row && row <= getRowNb();
	}

	// COMMANDES

	public void goForward(Vehicle vehicle, Coord coord) {
		Contract.checkCondition(canMoveTo(vehicle, coord), "cette action n'est pas possible");
		Contract.checkCondition(vehicleMap.get(vehicle) != null, "ce vehicule n'existe pas");
		Contract.checkCondition(coord.isValidCol(coord.getCol()) && isValidRow(coord.getRow()),
				"cette coordonée n'est pas valide");
		Contract.checkCondition(checkForwardDirection(vehicle, coord), "action impossible");

		Command cmd = new GoForward(this, vehicle, coord, vehicleMap.get(vehicle));
		history.add(cmd);
		cmd.act();
		nbOfPossibleUndo += 1;
	}

	public void goBackwards(Vehicle vehicle, Coord coord) {
		Contract.checkCondition(canMoveTo(vehicle, coord), "cette action n'est pas possible");
		Contract.checkCondition(vehicleMap.get(vehicle) != null, "ce vehicule n'existe pas");
		Contract.checkCondition(coord.isValidCol(coord.getCol()) && isValidRow(coord.getRow()),
				"cette coordonée n'est pas valide");
		Contract.checkCondition(checkBackwardsDirection(vehicle, coord), "action impossible");

		Command cmd = new GoBackwards(this, vehicle, coord, vehicleMap.get(vehicle));
		history.add(cmd);
		cmd.act();
		nbOfPossibleUndo += 1;
	}
	
	public void placeVehicles(Card card) {
		Contract.checkCondition(card != null);
		vehicleMap.clear();
		vehicleMap.putAll(card.getPlaces());
		coordMap.clear();
		
		List<Coord> coords;
		for (Vehicle vehicle : vehicleMap.keySet()) {
			coords = new ArrayList<Coord>();
			int width = 1;
			int height = 1;
			
			if (vehicle.getDirection() == Direction.HORIZONTAL) { // véhicule horizontal
				width = vehicle.getSize();
			} else { // véhicule vertical
				height = vehicle.getSize();
			}
			
			coords.add(vehicleMap.get(vehicle));
			if (width != 1) { // véhicule horizontal
				coords.add(new StdCoord(coords.get(0).getRow(), coords.get(0).getCol()+1));
				if (width == 3) {
					coords.add(new StdCoord(coords.get(0).getRow(), coords.get(0).getCol()+2));
				}
			} else { // véhicule vertical
				coords.add(new StdCoord(coords.get(0).getRow()+1, coords.get(0).getCol()));
				if (width == 3) {
					coords.add(new StdCoord(coords.get(0).getRow()+2, coords.get(0).getCol()));
				}
			}
			
			for (Coord coord : coords) {
				coordMap.put(coord, vehicle);
			}
		}

	}

	public void writeInFile(File file, Card card) {
		Contract.checkCondition(file != null && card != null);
		card.setFile(file);
		try {
			card.save(vehicleMap);
		} catch (IOException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
	}

	public void redo() {
		Contract.checkCondition(nbOfPossibleRedo > 0);
		nbOfPossibleUndo += 1;
		nbOfPossibleRedo -= 1;
		history.getCurrentElement().act();
		history.goForward();
	}
	
	public void undo() {
		Contract.checkCondition(nbOfPossibleUndo > 0);
		nbOfPossibleUndo -= 1;
		nbOfPossibleRedo += 1;
		history.getCurrentElement().act();
		history.goBackward();
	}

	public void goForwardAux(Vehicle vehicle, Coord coord) {
		Contract.checkCondition(canMoveTo(vehicle, coord), "cette action n'est pas possible");
		Contract.checkCondition(vehicleMap.get(vehicle) != null, "ce vehicule n'existe pas");
		Contract.checkCondition(coord.isValidCol(coord.getCol()) && isValidRow(coord.getRow()),
				"cette coordonée n'est pas valide");
		Contract.checkCondition(checkForwardDirection(vehicle, coord), "action impossible");

		// Calculer laquelle des 2 anciennes coordonnées
		// est la plus proche de la coordonnée indiquée
		Coord newCoord;
		if (vehicle.getDirection() == Direction.HORIZONTAL) {
			newCoord = new StdCoord(coord.getRow(), coord.getCol() - 1);
		} else {
			newCoord = coord;
		}
		Coord oldCoord = vehicleMap.get(vehicle);

		vehicleMap.remove(vehicle);
		vehicleMap.put(vehicle, newCoord);

		coordMap.remove(oldCoord);

		coordMap.put(newCoord, vehicle);
	}

	public void goBackwardsAux(Vehicle vehicle, Coord coord) {
		Contract.checkCondition(canMoveTo(vehicle, coord), "cette action n'est pas possible");
		Contract.checkCondition(vehicleMap.get(vehicle) != null, "ce vehicule n'existe pas");
		Contract.checkCondition(coord.isValidCol(coord.getCol()) && isValidRow(coord.getRow()),
				"cette coordonée n'est pas valide");
		Contract.checkCondition(checkBackwardsDirection(vehicle, coord), "action impossible");

		// Calculer laquelle des 2 anciennes coordonnées
		// est la plus proche de la coordonnée indiquée
		Coord newCoord;

		if (vehicle.getDirection() == Direction.HORIZONTAL) {
			newCoord = new StdCoord(coord.getRow(), coord.getCol() + 1);
		} else {
			newCoord = new StdCoord(coord.getRow() - 1, coord.getCol());
		}

		Coord oldCoord = vehicleMap.get(vehicle);
		vehicleMap.remove(vehicle);
		vehicleMap.put(vehicle, newCoord);

		coordMap.remove(oldCoord);
		
		coordMap.put(newCoord, vehicle);
	}

	// OUTILS
	/**
	 * Indique si le mouvement goBackwards (reculer) est possible.
	 */
	private boolean checkBackwardsDirection(Vehicle vehicle, Coord newCoord) {
		assert vehicle != null && newCoord != null;
		if (vehicle.getDirection() == Direction.HORIZONTAL) {
			return vehicleMap.get(vehicle).getRow() == newCoord.getRow()
					&& vehicleMap.get(vehicle).getCol() >= newCoord.getCol();
		} else {
			return vehicleMap.get(vehicle).getRow() <= newCoord.getRow()
					&& vehicleMap.get(vehicle).getCol() == newCoord.getCol();
		}
	}

	/**
	 * Indique si le mouvement goForward (avancer) est possible.
	 */
	private boolean checkForwardDirection(Vehicle vehicle, Coord newCoord) {
		assert vehicle != null && newCoord != null;
		if (vehicle.getDirection() == Direction.HORIZONTAL) {
			return vehicleMap.get(vehicle).getRow() == newCoord.getRow()
					&& vehicleMap.get(vehicle).getCol() <= newCoord.getCol();
		} else {
			return vehicleMap.get(vehicle).getRow() >= newCoord.getRow()
					&& vehicleMap.get(vehicle).getCol() == newCoord.getCol();
		}
	}

	private boolean checkAllFreeCoords(Vehicle vehicle, Coord coord) {
		int difference;
		if (vehicleMap.get(vehicle).getRow() != coord.getRow()) {
			difference = (vehicleMap.get(vehicle).getRow() - coord.getRow()) - 1;
		} else {
			difference = (vehicleMap.get(vehicle).getCol() - coord.getCol()) - 1;
		}
		if (checkForwardDirection(vehicle, coord)) {
			for (int i = 0; i < difference; i++) {
				if (vehicle.getDirection() == Direction.VERTICAL) {
					if (!isFree(new StdCoord(coord.getRow() + 1, coord.getCol()))) {
						return false;
					}
				} else {
					if (!isFree(new StdCoord(coord.getRow(), coord.getCol() - 1))) {
						return false;
					}
				}
			}
		} else {
			for (int i = 0; i < difference; i++) {
				if (vehicle.getDirection() == Direction.VERTICAL) {
					if (!isFree(new StdCoord(coord.getRow() - 1, coord.getCol()))) {
						return false;
					}
				} else {
					if (!isFree(new StdCoord(coord.getRow(), coord.getCol() + 1))) {
						return false;
					}
				}
			}
		}
		return true;
	}
}