package view;

import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.Card;
import model.CreateCardModel;
import model.StdCard;

public class CreateFile {

	private JFrame frame;
	private CreateCardModel model;
	private PanelCreateCard pc;
	private JButton annuler, confirmer;
	private JTextField nom;
	
	public CreateFile(CreateCardModel m, PanelCreateCard pcc ) {
		pc = pcc;
		model = m;
		createView();
		placeComponent();
		createController();
	}
	
	public void display() {
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void createView() {
		frame = new JFrame("Création du fichier");
		
		annuler = new JButton("Annuler");
		confirmer = new JButton("Confirmer");
		
		nom = new JTextField();
	}
	
	public void placeComponent() {
		JPanel p = new JPanel(new GridLayout(0,1)); {
			p.add(new JLabel("Ecrivez le nom de votre niveau :"));
			p.add(nom);
			
			JPanel q = new JPanel(new FlowLayout(FlowLayout.CENTER)); {
				q.add(annuler);
				q.add(confirmer);
			}
			p.add(q);
			
		}
		frame.add(p);
	}

	public void createController() {
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int reponse = JOptionPane.showConfirmDialog(frame,
						"Voulez-vous quitter sans sauvegarder ?", "Confirmation",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (reponse == JOptionPane.YES_OPTION) {
					frame.dispose();
				}
			}
		});

		annuler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});

		confirmer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (nom.getText() != null) {
					String path = "niveaux/perso/" + nom.getText();
					File monFichier = new File( path + ".lvl");
					if (monFichier.exists()) {
						int reponse = JOptionPane.showConfirmDialog(frame,
								"Votre fichier existe déjà voulez vous l'écraser ?", "Confirmation",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if (reponse == JOptionPane.YES_OPTION) {
							monFichier.delete();
							
							Card card = new StdCard(monFichier);
							try {
								card.save(model.getAllVehicles());
							} catch (IOException e1) { e1.printStackTrace(); }
							
							createImg( path );
							frame.dispose();
						} else {
							frame.dispose();
							new CreateFile(model, pc).display();
						}
					} else {
						Card card = new StdCard(monFichier);
						try {
							card.save(model.getAllVehicles());
						} catch (IOException e1) { e1.printStackTrace(); }
						
						createImg( path );
						frame.dispose();
					}
				} else {
					int reponse = JOptionPane.showConfirmDialog(frame,
							"Vous n'avez pas mentionné de nom. Voulez-vous quand même sauvegarder sous le nom de \"" + model.getNbFileWithoutName() + ".lvl\" ?", "Confirmation",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (reponse == JOptionPane.YES_OPTION) {
						String path = "niveaux/perso/" + model.getNbFileWithoutName();
						Card card = new StdCard(new File( path + ".lvl"));
						try {
							card.save(model.getAllVehicles());
						} catch (IOException e1) { e1.printStackTrace(); }
						
						createImg( path );
						frame.dispose();
					}
				}

			}
		});
	}
	
	public void createImg( String path ){
		BufferedImage bi = new BufferedImage( pc.getWidth() - 470, pc.getHeight() + 100, BufferedImage.TYPE_INT_ARGB );
		
		Graphics2D graphics = (Graphics2D) bi.getGraphics();
		pc.paint( graphics );
		
		System.out.println( path );
		
		path += ".png";

		File img = new File ( path );
		try { ImageIO.write( bi.getSubimage( 250, 5, 535, 600 ), "PNG", img ); } 
		catch( IOException ie ) { ie.getMessage(); }
		
		System.out.println(model.toString());
	}
}
