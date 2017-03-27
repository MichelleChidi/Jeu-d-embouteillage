package model;

import util.Contract;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Matcher;

public class StdCard implements Card {

	// ATTRIBUTS

	/**
	 * Map pour tous les véhicules adans le fichier.
	 */
	private Map<Vehicle, Coord> places;

	/**
	 * Fichier qui represente la carte de jeu.
	 */
	private File file;

	// CONSTRUCTEURS

	/**
	 * Constructeur d'une carte.
	 */
	public StdCard(File file) {
		Contract.checkCondition(file != null, "Il faut un fichier à charger");
		this.file = file;
		places = new HashMap<Vehicle, Coord>();
	}

	// REQUETES

	public Map<Vehicle, Coord> getPlaces() {
		return places;
	}

	public File getFile() {
		return file;
	}

	// COMMANDES

	public void setFile(File file) {
		Contract.checkCondition(file != null, "Il faut un fichier à charger");
		this.file = file;
	}

	public void save(Map<Vehicle, Coord> carte) throws IOException {
		Contract.checkCondition(carte != null, "la carte est nulle");
		BufferedWriter writer = new BufferedWriter(new FileWriter(getFile()));
		SortedSet<Vehicle> vehicleSet = new ConcurrentSkipListSet<Vehicle>(carte.keySet());
		
		int i = 0;
		for (Vehicle v : vehicleSet) {
			StringBuffer string = new StringBuffer();
			
			Coord coord = carte.get(v);
			string.append(coord.getRow() + ":" + coord.getCol());
			if (i != (v.getSize() - 1)) {
				string.append(";");
			}
			writer.write(v.getType().getName() + ";" + v.getDirection().toString() + ";" + string + "\n");
		}
		writer.close();
	}

	public void load() throws IOException, BadSyntaxException {
		Contract.checkCondition(this.getFile() != null, "Il faut un fichier a charger !");
		BufferedReader reader = new BufferedReader(new FileReader(this.getFile()));

		String[] elements;
		String line = new String(reader.readLine());
		while (line != null) {
			Matcher matcher = LINE_RECOGNIZER.matcher(line);
			if (!matcher.matches()) {
				reader.close();
				throw new BadSyntaxException();
			} else {
				elements = line.split(";");
				Type type = Type.fromString(elements[0].trim());
				Direction dir = Direction.fromString(elements[1].trim());

				String[] coords = elements[2].split(":");
				// Ligne
				int cd1 = Integer.parseInt(coords[0]);
				// Colonne
				int cd2 = Integer.parseInt(coords[1]);
				Coord coord = new StdCoord(cd1, cd2);
				places.put(new StdVehicle(type, dir), coord);
				line = reader.readLine();
			}
		}
		reader.close();
	}
}