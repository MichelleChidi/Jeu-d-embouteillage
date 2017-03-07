package source;

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
  private Map<Vehicle, List<Coord>> places;
  
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
    places = new HashMap<Vehicle, List<Coord>>();
  }
    
  // REQUETES
    
  public Map<Vehicle, List<Coord>> getPlaces() {
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
  
  
  @Override
  public void save(Map<Vehicle,List<Coord>> carte) throws IOException {
    Contract.checkCondition(carte != null, "la carte est nulle"); 
    BufferedWriter writer = new BufferedWriter(new FileWriter(getFile()));
    SortedSet<Vehicle> vehicleSet = new ConcurrentSkipListSet<Vehicle>(carte.keySet());
    for (Vehicle v : vehicleSet) {
      StringBuffer string = new StringBuffer();
      for (int i = 0; i < carte.get(v).size(); i++) {
        Coord coord = carte.get(v).get(i);
        string.append(coord.getCol() + ":" + coord.getRow());
        if (i != (carte.get(v).size() - 1)) {
          string.append(";");
        }
      }
      writer.write(
            v.getType().getName() + ";"  + v.getDirection().toString() + ";" 
             + string + "\n");
    }   
    writer.close();
  }
  
  @Override
  public void load() throws IOException, BadSyntaxException {
    Contract.checkCondition(this.getFile() != null, "Il faut un fichier a charger !");
    BufferedReader reader = new BufferedReader(new FileReader(this.getFile()));
        
    String[] elements;
    String line = new  String(reader.readLine());
    while (line != null) {
      Matcher matcher = LINE_RECOGNIZER.matcher(line);
      if (!matcher.matches()) {
        reader.close();
        throw new BadSyntaxException();
      } else {
        elements = line.split(";");
        Type type = Type.fromString(elements[0].trim());
        Direction dir = Direction.fromString(elements[1].trim());
        
        List<Coord> list = new ArrayList<Coord>();
        for ( int i = 0; i < type.getSize(); i++ ) {
          String[] coords = elements[2 + i].split(":"); 
          //Ligne
          int cd1 = Integer.parseInt(coords[0]);
          //Colonne
          int cd2 = Integer.parseInt(coords[1]);
          Coord coord = new StdCoord(cd1, cd2); 
          list.add(coord);
        }
        checkCoords(type, list);
        places.put(new StdVehicle(type, dir), list);
        line = reader.readLine();
      }
    }
    reader.close();
  }
  
  // OUTILS
  /**
   * Indique si le nombre de coordonnées correspond au taille du véhicule.
   */
  private boolean checkCoords(Type type, List<Coord> coord) {
    assert type != null && coord != null;
    return type.getSize() == coord.size();
  }
}
