package model;

import cmd.Command;
import cmd.Move;
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
		if (coordMap.get(new StdCoord(2,5)) == null) {
			return false;
		}
		if (coordMap.get(new StdCoord(2,5)).isRed()) {
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
		Coord c;
		int row = coord.getRow();
		int col = coord.getCol();

		if (v.getDirection() == Direction.HORIZONTAL) {
			for (int i = 0; i < v.getSize(); i++) {
				c = new StdCoord(row, col + i);
				if (! isFree(c) && coordMap.get(c) != v) {
					return false;
				}
			}
		} else { // véhicule vertical
			for (int i = 0; i < v.getSize(); i++) {
				c = new StdCoord(row + i, col);
				if (! isFree(c) && coordMap.get(c) != v) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean canMoveTo(Vehicle vehicle, Coord coord) {
		Contract.checkCondition(vehicle != null, "Ce véhicule n'existe pas");
		Contract.checkCondition(coord != null, "Cette coordonnée est nulle");
		
		boolean t = false;
		
		if (vehicle.getDirection() == Direction.HORIZONTAL) {
			t = coord.getCol() <= 5 - (vehicle.getSize() - 1);
		} else {
			t = coord.getRow() <= 5 - (vehicle.getSize() - 1);
		}
		
		return coord.isAlignedWith(vehicleMap.get(vehicle), vehicle.getDirection())
				&& isFree(vehicle, coord)
				&& checkAllFreeCoords(vehicle, coord)
				&& t;
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
			int x = c.getCol() - 1;
			if (x <= 0 && canMoveTo(veh, new StdCoord(c.getRow(), 0))) {
				return new StdCoord(c.getRow(), 0);
			}
			while (x > 0 && canMoveTo(veh, new StdCoord(c.getRow(), x))) {
				x --;
			}
			if (! canMoveTo(veh, new StdCoord(c.getRow(), 0))) {
				x ++;
			}
			return new StdCoord(c.getRow(), x);
		} else {
			int y = c.getRow() - 1;
			if (y <= 0 && canMoveTo(veh, new StdCoord(0, c.getCol()))) {
				return new StdCoord(0, c.getCol());
			}
			while (y > 0 && canMoveTo(veh, new StdCoord(y, c.getCol()))) {
				y --;
			}
			if (! canMoveTo(veh, new StdCoord(0, c.getCol()))) {
				y ++;
			}
			return new StdCoord(y, c.getCol());
		}
	}

	public Coord getFirstVehicleAfter(Vehicle veh) {
		Coord c = getCoord(veh);
		
		if (veh.getDirection() == Direction.HORIZONTAL) {
			int x = c.getCol() + 1;
			if (x >= 5 - (veh.getSize()-1)) {
				return new StdCoord(c.getRow(), 5 - (veh.getSize()-1));
			}
			while (x < 6 - (veh.getSize()-1) && canMoveTo(veh, new StdCoord(c.getRow(), x))) {
				x ++;
			}
			x --;
			return new StdCoord(c.getRow(), x);
		} else {
			int y = c.getRow() + 1;
			if (y >= 5 - (veh.getSize()-1)) {
				return new StdCoord(5 - (veh.getSize()-1), c.getCol());
			}
			while (y < 6 - (veh.getSize()-1) && canMoveTo(veh, new StdCoord(y, c.getCol()))) {
				y ++;
			}
			y --;
			return new StdCoord(y, c.getCol());
		}
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
		Contract.checkCondition(canMoveTo(veh, coord), "le vehicule ne peut pas y aller !!!");
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
	
	public void moveAux(Vehicle vehicle, Coord coord) {
		// TODO Auto-generated method stub
		Coord oldCoord = vehicleMap.get(vehicle);
		List<Coord> oldCoords = new LinkedList<Coord>();
		for (Coord c : coordMap.keySet()) {
			if (coordMap.get(c).equals(vehicle)) {
				oldCoords.add(c);
			}
		}
		List<Coord> newCoords = new LinkedList<Coord>();
		if (vehicle.getDirection() == Direction.HORIZONTAL) {
			// on avance
			if (oldCoord.getCol() < coord.getCol()) {
				for (int i = vehicle.getSize() - 1; i >= 1; i--) {
					Coord newCd = new StdCoord(coord.getRow(), coord.getCol() - i);
					newCoords.add(newCd);
				}
				newCoords.add(coord);
			} else {
				// on recule
				newCoords.add(coord);
				for (int i = 1; i < vehicle.getSize(); i++) {
					Coord newCd = new StdCoord(coord.getRow(), coord.getCol() + i);
					newCoords.add(newCd);
				}
			}
		} else {
			// on avance
			if (oldCoord.getRow() < coord.getRow()) {
				for (int i = vehicle.getSize() - 1; i >= 1; i--) {
					Coord newCd = new StdCoord(coord.getRow() - i, coord.getCol());
					newCoords.add(newCd);
				}
				newCoords.add(coord);
			} else {
				newCoords.add(coord);
				for (int i = 1; i < vehicle.getSize(); i++) {
					Coord newCd = new StdCoord(coord.getRow() + i, coord.getCol());
					newCoords.add(newCd);
				}
			}
		}
		vehicleMap.remove(vehicle);
		vehicleMap.put(vehicle, newCoords.get(0));
		for (int i = 0; i < oldCoords.size(); i++) {
			coordMap.remove(oldCoords.get(i));
		}
		for (int i = 0; i < newCoords.size(); i++) {
			coordMap.put(newCoords.get(i), vehicle);
		}
	}

	public void placeVehicles(Card card) {
		Contract.checkCondition(card != null);
		vehicleMap.clear();
		coordMap.clear();
		
		for (Vehicle veh : card.getPlaces().keySet()) {
			vehicleMap.put(veh, card.getPlaces().get(veh));
		}

		List<Coord> coords;
		for (Vehicle vehicle : vehicleMap.keySet()) {
			coords = new ArrayList<Coord>();

			Coord c = vehicleMap.get(vehicle);
			coords.add(c);
			if (vehicle.getDirection() == Direction.HORIZONTAL) { // véhicule horizontal
				coords.add(new StdCoord(c.getRow(), c.getCol() + 1));
				if (vehicle.getSize() == 3) {
					coords.add(new StdCoord(c.getRow(), c.getCol() + 2));
				}
			} else { // véhicule vertical
				coords.add(new StdCoord(c.getRow() + 1, c.getCol()));
				if (vehicle.getSize() == 3) {
					coords.add(new StdCoord(c.getRow() + 2, c.getCol()));
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
	public boolean checkAllFreeCoords(Vehicle vehicle, Coord coord) {
		 if (vehicle.getDirection() == Direction.HORIZONTAL) {
			 Coord c = vehicleMap.get(vehicle);
			 int col = c.getCol() + (vehicle.getSize()- 1);
			 for (int i = col + 1; i <= coord.getCol(); i++) {
				 Coord next = new StdCoord(c.getRow(), i);
				 if (!isFree(next)) {
					 return false;
				 }
			 }
		 } else {
			 Coord c = vehicleMap.get(vehicle);
			 int row = c.getRow() + (vehicle.getSize()- 1);
			 for (int i = (row +1); i <= coord.getRow(); i++) {
				 Coord next = new StdCoord(i,c.getCol());
				 if (!isFree(next)) {
					 return false;
				 }
			 }
		 }
		 
		 return true;
	 }
}