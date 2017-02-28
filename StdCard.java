package source;

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

import util.Contract;

public class StdCard implements Card {
	
	// ATTRIBUTS
	
	private Map<Vehicle, List<Coord>> places;
	
	private File file;
	
	// CONSTRUCTEURS
	
	public StdCard(File f) {
		Contract.checkCondition(f != null, "Il faut un fichier Ã  charger");
		file = f;
		places = new HashMap<Vehicle, List<Coord>>();
	}
	
	// REQUETES
	
	public Map<Vehicle, List<Coord>> getPlace() {
		return places;
	}
	
	public File getFile() {
		return file;
	}
	
	// COMMANDES
	
	public void save(String name) throws IOException {
		Contract.checkCondition(name != null, "Il faut un nom pour votre carte !!"); 
		FileWriter fW = new FileWriter(name);
		BufferedWriter writer = new BufferedWriter(fW);
		SortedSet<Vehicle> kS = new ConcurrentSkipListSet<Vehicle>(getPlace().keySet());
		for (Vehicle v : kS) {
			String s = new String();
			for (Coord c : getPlace().get(v)) {
				s.concat("" + c.getCol() + ":" + c.getRow() + ";");
			}
			writer.write(
			"" + v.getId() + ";" + v.getSize() + ";"  + v.getDirection().toString() + ";" 
			 + s + "\n"  , 0 , 100);
		}
		writer.close();
	}
	
	public void load() throws IOException {
		Contract.checkCondition(this.getFile() != null, "Il faut un fichier a charger !");
		BufferedReader reader = new BufferedReader(new FileReader(this.getFile()));
		
		String[] elements;
		String[] c1;
		String[] c2;
		String[] c3;
		int size;
		Direction d;
		Coord firstCoord;
		int f1;
		int f2;
		Coord secondCoord;
		int s1;
		int s2;
		Coord thirdCoord;
		int t1;
		int t2;
		List<Coord> lC;
		
		String s = new String(reader.readLine());
		while (s != null) {
			elements = s.split(";");
			c1 = elements[3].split(":");
			c2 = elements[4].split(":");
			c3 = elements[5].split(":");
			
			// Gestion des attributs du véhicule
			size = Integer.parseInt(elements[1]);
			if (elements[2].compareTo(Direction.VERTICAL.toString()) == 0) {
				d = Direction.VERTICAL;
			} else {
				d = Direction.HORIZONTAL;
			}
			
			//gestion des coordonnées
			lC = new ArrayList<Coord>();
			
			f1 = Integer.parseInt(c1[0]);
			f2 = Integer.parseInt(c1[1]);
			firstCoord = new StdCoord(f1,f2);
			lC.add(firstCoord);
			
			s1 = Integer.parseInt(c2[0]);
			s2 = Integer.parseInt(c2[1]);
			secondCoord = new StdCoord(s1,s2);
			lC.add(secondCoord);
			
			if (c3[0].compareTo("\n") != 0 ) {
				t1 = Integer.parseInt(c3[0]);
				t2 = Integer.parseInt(c3[1]);
				thirdCoord = new StdCoord(t1,t2);
				lC.add(thirdCoord);
			}
			places.put(new StdVehicle(size, d),lC);
			s = new String(reader.readLine());
		}
		
		reader.close();
	}
}
