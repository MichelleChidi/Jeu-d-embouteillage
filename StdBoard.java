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
			for (int i = 0; i < v.getSize(); i++) {
				if (coordMap.get(new StdCoord(row, col + i)) != null && coordMap.get(new StdCoord(row, col + i)) != v) {
					test = false;
				}
			}
		} else { // véhicule vertical
			for (int i = 0; i < v.getSize(); i++) {
				if (coordMap.get(new StdCoord(row + i, col)) != null && coordMap.get(new StdCoord(row + i, col)) != v) {
					test = false;
				}
			}
		}
		return test;
	}

	public boolean canMoveTo(Vehicle vehicle, Coord coord) {
		Contract.checkCondition(vehicle != null, "Ce véhicule n'existe pas");
		Contract.checkCondition(coord != null, "Cette coordonnée est nulle");
		return coord.isAlignedWith(vehicleMap.get(vehicle), vehicle.getDirection()) && isFree(vehicle, coord);
	}

	public boolean isValidCol(int col) {
		return 0 <= col && col <= getColNb();
	}

	public boolean isValidRow(int row) {
		return 0 <= row && row <= getRowNb();
	}
	
	public Coord getFirstVehicleBefore(Vehicle veh) {
		Coord c = getCoord(veh);
		
		if (veh.getDirection() == Direction.HORIZONTAL) {
			// le cas du véhicule qui a le coffre sur le bord du plateau
			if (c.getCol() == 0) {
				return null;
			}
			
			Coord tmp;
			for (int x = c.getCol() - 1; x > 0; x--) {
				if (! isFree(new StdCoord(c.getRow(), x))) {
					tmp = new StdCoord(c.getRow(), x);
					return tmp;
				}
			}
		} else {
			// le cas du véhicule qui a le coffre sur le bord du plateau
			if (c.getRow() == 0) {
				return null;
			}
			
			Coord tmp;
			for (int y = c.getRow() - 1; y > 0; y--) {
				if (! isFree(new StdCoord(y, c.getCol()))) {
					tmp = new StdCoord(c.getRow(), y);
					return tmp;
				}
			}
		}
		
		return null;
	}

	public Coord getFirstVehicleAfter(Vehicle veh) {
		Coord c = getCoord(veh);
		
		if (veh.getDirection() == Direction.HORIZONTAL) {
			// le cas du véhicule qui a le coffre sur le bord du plateau
			if (c.getCol() >= 5) {
				return null;
			}
			
			Coord tmp;
			for (int x = c.getCol() + veh.getSize(); x < 5; x++) {
				if (! isFree(new StdCoord(c.getRow(), x))) {
					tmp = new StdCoord(c.getRow(), x);
					return tmp;
				}
			}
		} else {
			// le cas du véhicule qui a le coffre sur le bord du plateau
			if (c.getRow() >= 5) {
				return null;
			}
			
			Coord tmp;
			for (int y = c.getRow() + veh.getSize(); y < 5; y++) {
				if (! isFree(new StdCoord(y, c.getCol()))) {
					tmp = new StdCoord(c.getRow(), y);
					return tmp;
				}
			}
		}
		
		return null;
	}
	
	public String toString() {
		String str = "";
		char[][] tab = new char[6][6];

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				if (coordMap.get(new StdCoord(i, j)) == null) {
					tab[i][j] = ' ';
				} else {
					tab[i][j] = '*';
				}
			}
		}

		for (int i = 0; i < 6; i++) {
			str += "|";
			for (int j = 0; j < 6; j++) {
				str += tab[i][j] + "|";
			}
			str += "\n";
		}

		return str;
	}

	// COMMANDES

	public void moveTo (Vehicle veh, Coord coord) {
		Contract.checkCondition(isFree(veh, coord));
		Contract.checkCondition(vehicleMap.get(veh) != null, "ce vehicule n'existe pas");
		
		List<Coord> newCoords = new ArrayList<Coord>();

		newCoords.add(coord);
		if (veh.getDirection() == Direction.HORIZONTAL) {
			newCoords.add(new StdCoord(coord.getRow(), coord.getCol() + 1));
			if (veh.getSize() == 3) {
				newCoords.add(new StdCoord(coord.getRow(), coord.getCol() + 2));
			}
		} else {
			newCoords.add(new StdCoord(coord.getRow() + 1, coord.getCol()));
			if (veh.getSize() == 3) {
				newCoords.add(new StdCoord(coord.getRow() + 2, coord.getCol()));
			}

		}

		Coord old = vehicleMap.get(veh);
		List<Coord> oldCoords = new ArrayList<Coord>();
		oldCoords.add(old);
		if (veh.getDirection() == Direction.HORIZONTAL) {
			oldCoords.add(new StdCoord(old.getRow(), old.getCol() + 1));
			if (veh.getSize() == 3) {
				oldCoords.add(new StdCoord(old.getRow(), old.getCol() + 2));
			}
		} else {
			oldCoords.add(new StdCoord(old.getRow() + 1, old.getCol()));
			if (veh.getSize() == 3) {
				oldCoords.add(new StdCoord(old.getRow() + 2, old.getCol()));
			}
		}

		vehicleMap.remove(veh);
		vehicleMap.put(veh, newCoords.get(0));

		for (int i = 0; i < oldCoords.size(); i++) {
			coordMap.remove(oldCoords.get(i));
		}

		for (int i = 0; i < newCoords.size(); i++) {
			coordMap.put(newCoords.get(i), veh);
		}
	}

	public void placeVehicles(Card card) {
		Contract.checkCondition(card != null);
		vehicleMap.clear();
		coordMap.clear();
		vehicleMap.putAll(card.getPlaces());

		List<Coord> coords;
		for (Vehicle vehicle : vehicleMap.keySet()) {
			coords = new ArrayList<Coord>();
			int width = 1;
			int height = 1;

			if (vehicle.getDirection() == Direction.HORIZONTAL) { // véhicule
																	// horizontal
				width = vehicle.getSize();
			} else { // véhicule vertical
				height = vehicle.getSize();
			}

			coords.add(vehicleMap.get(vehicle));
			if (width != 1) { // véhicule horizontal
				coords.add(new StdCoord(coords.get(0).getRow(), coords.get(0).getCol() + 1));
				if (width == 3) {
					coords.add(new StdCoord(coords.get(0).getRow(), coords.get(0).getCol() + 2));
				}
			} else { // véhicule vertical
				coords.add(new StdCoord(coords.get(0).getRow() + 1, coords.get(0).getCol()));
				if (height == 3) {
					coords.add(new StdCoord(coords.get(0).getRow() + 2, coords.get(0).getCol()));
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
}