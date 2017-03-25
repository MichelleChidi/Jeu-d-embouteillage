package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

import util.Contract;

public class StdCreateCardModel implements CreateCardModel {

	/**
	 * maps de tous les véhicules et ses coordonnées dans le plateau.
	 */
	private Map<Vehicle, Coord> vehicleMap;
	private Map<Coord, Vehicle> coordMap;
	
	// CONSTRUCTEUR
	
	public StdCreateCardModel() {
		vehicleMap = new HashMap<Vehicle, Coord>();
		coordMap = new HashMap<Coord, Vehicle>();
	}
	
	// ACCESSEURS 
	
	public Set<Vehicle> getAllVehicles() {
		return vehicleMap.keySet();
	}

	public Vehicle getVehicle(Coord coord) {
		return coordMap.get(coord);
	}

	public Coord getCoord(Vehicle vehicle) {
		return vehicleMap.get(vehicle);
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
	
	public String toString() {
		String str = "";
		char[][] tab = new char[6][6];
		
		for (Coord c : coordMap.keySet()) {
			tab[c.getRow()][c.getCol()] = '*';
		}
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				if (tab[i][j] != '*') {
					tab[i][j] = ' ';
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
	
	public void addVehicle(Vehicle veh, Coord coord) {
		Contract.checkCondition(veh != null);
		Contract.checkCondition(coord.isValidCol(coord.getCol()) && coord.isValidRow(coord.getRow()));
		
		List<Coord> coords = new ArrayList<Coord>();
		
		switch (veh.getType()) {
			case CAR:
				if (veh.getDirection() == Direction.HORIZONTAL) {
					coords.add(coord);
					coords.add(new StdCoord(coord.getRow(), coord.getCol()+1));
				} else {
					coords.add(coord);
					coords.add(new StdCoord(coord.getRow()+1, coord.getCol()));
				}
				break;
			case TRUCK:
				if (veh.getDirection() == Direction.HORIZONTAL) {
					coords.add(coord);
					coords.add(new StdCoord(coord.getRow(), coord.getCol()+1));
					coords.add(new StdCoord(coord.getRow(), coord.getCol()+2));
				} else {
					coords.add(coord);
					coords.add(new StdCoord(coord.getRow()+1, coord.getCol()));
					coords.add(new StdCoord(coord.getRow()+2, coord.getCol()));
				}
				break;
			default:
				coords.add(coord);
				coords.add(new StdCoord(coord.getRow(), coord.getCol()+1));
				break;
		}
		
		vehicleMap.put(veh, coord);
		
		for (Coord c : coords) {
			coordMap.put(c, veh);
		}
	}
	
	public void removeVehicle(Vehicle veh, Coord coord) {
		Contract.checkCondition(veh != null);
		Contract.checkCondition(coord.isValidCol(coord.getCol()) && coord.isValidRow(coord.getRow()));
		
		List<Coord> coordTmp = new ArrayList<Coord>();
		coordTmp = getKeys(veh);
		
		// Suppression des coordonnées
		for (int i = 0; i < coordTmp.size(); i++ ) {
			coordMap.remove(coordTmp.get(i));
		}
		
		// Suppression du véhicule
		vehicleMap.remove(veh);
	}

	private List<Coord> getKeys(Vehicle v) {
		List<Coord> keys = new ArrayList<Coord>();
		for (Coord key : coordMap.keySet()) {
			if (coordMap.get(key).equals(v)) {
				keys.add(key);
			}
		}
		return keys;
	}

	public void clear() {
		vehicleMap.clear();
		coordMap.clear();
	}
}
