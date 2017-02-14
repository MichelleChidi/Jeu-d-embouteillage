package source;

import java.io.File;
import java.util.Map;

/**
 * Modélise un éditeur/créateur de carte basique
 * permettant de sauvegarder une carte dans un fichier
 * ou de charger une carte
 * 
 * 
 * @cons <pre>
 * $ARGS$ File f
 * $PRE$  f != null
 * $POST$ getPlace() est une représentation des informations de f
 */
public interface ICard {

	// REQUETES
	
	/**
	 * Renvoie une map entre les véhicules et les coordonnées des véhicules
	 */
	Map<IVehicle,List<Coord>> getPlace();
	
	/**
	 * Renvoie le fichier attaché a cet éditeur de carte
	 */
	File getFile();
	
	// COMMANDS
	
	/**
	 * Enregistre les données de la carte dans un fichier de nom s 
	 */
	void save(String s);
	
	/**
	 * Charge les données du fichier lié dans la map
	 */
	void load();
}
