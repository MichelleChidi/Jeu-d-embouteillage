package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * ModÃ©lise un éditeur/créateur de carte basique permettant de sauvegarder une
 * carte dans un fichier ou de charger une carte.
 * 
 * 
 * @cons $ARGS$ File f $PRE$ f != null $POST$ getPlace() est une reprÃ©sentation
 *       des informations de f
 */
public interface Card {

	// CONSTANTES
	// Le pattern qui permet de tester la validité d'une ligne du fichier :
	// type de véhicule;
	// direction de véhicle;
	// coordonnées de véhicle(d:d;d:d; ou d:d;d:d;d:d où d repr�sente un
	// chiffre)
	Pattern LINE_RECOGNIZER = Pattern
			.compile("^[a-zA-Z]{3,5};" + "[a-zA-Z]{8,10};" + "(\\d:\\d;\\d:\\d|" + "\\d:\\d;\\d:\\d;\\d:\\d)$");

	// REQUETES

	/**
	 * Renvoie une map entre les vÃ©hicules et les coordonnÃ©es des vÃ©hicules.
	 */
	Map<Vehicle, Coord> getPlaces();

	/**
	 * Renvoie le fichier attachÃ© a cet Ã©diteur de carte.
	 */
	File getFile();

	// COMMANDES

	/**
	 * Fixer un fichier pour la carte.
	 * 
	 * @pre file != null
	 * @post getFile() == file
	 */
	void setFile(File file);

	/**
	 * Enregistre les donnÃ©es de la carte dans un fichier.
	 * 
	 * @pre carte != null
	 */
	void save(Map<Vehicle, Coord> carte) throws IOException;

	/**
	 * Charge les données du fichier lié dans la map.
	 * 
	 * @pre getFile() != null
	 */
	void load() throws FileNotFoundException, IOException, BadSyntaxException;
}