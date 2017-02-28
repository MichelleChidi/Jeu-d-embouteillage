package source;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * ModÃ©lise un Ã©diteur/crÃ©ateur de carte basique
 * permettant de sauvegarder une carte dans un fichier
 * ou de charger une carte
 * 
 * 
 * @cons <pre>
 * $ARGS$ File f
 * $PRE$  f != null
 * $POST$ getPlace() est une reprÃ©sentation des informations de f
 */
public interface Card {

	// REQUETES
	
	/**
	 * Renvoie une map entre les vÃ©hicules et les coordonnÃ©es des vÃ©hicules
	 */
	Map<Vehicle,List<Coord>> getPlace();
	
	/**
	 * Renvoie le fichier attachÃ© a cet Ã©diteur de carte
	 */
	File getFile();
	
	// COMMANDS
	
	/**
	 * Enregistre les donnÃ©es de la carte dans un fichier de nom s 
	 * @throws IOException 
	 */
	void save(String s) throws IOException;
	
	/**
	 * Charge les donnÃ©es du fichier liÃ© dans la map
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 */
	void load() throws FileNotFoundException, IOException;
}
