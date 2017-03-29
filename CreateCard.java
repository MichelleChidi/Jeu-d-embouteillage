package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.CreateCardModel;
import model.DraggableImageComponent;
import model.StdCreateCardModel;

public class CreateCard {
    private static PanelCreateCard createBoard;
	
    private MainMenu mainMenu;
    
    private JFrame mainFrame;
    private static CreateCardModel model;
    private JButton sauvegarder;
	private JButton effacer;
	private JButton retour;
    
    private static Image redCar;
    private static int colorHorizontalTruck;
	private static int colorVerticalTruck;
	private static int colorHorizontalCar;
	private static int colorVerticalCar;
	private static ArrayList<Image> verticalTrucks;
	private static ArrayList<Image> horizontalTrucks;
	private static ArrayList<Image> verticalCars;
	private static ArrayList<Image> horizontalCars;
	
	public CreateCard( JFrame frame ){
		mainFrame = frame;
		initGraphic();
		createModel();
		createView();
		placeComponent();
		createController();
	}
	
	private void initGraphic() {
		// création de la voiture rouge
	    redCar = Toolkit.getDefaultToolkit().createImage("images/redCar.png");
	    
        // création des ArrayList<Image>
	    verticalTrucks   = new ArrayList<Image>();
	    horizontalTrucks = new ArrayList<Image>();
	    verticalCars     = new ArrayList<Image>();
	    horizontalCars   = new ArrayList<Image>();
	    
	    // verticalTrucks 
	    verticalTrucks.add(Toolkit.getDefaultToolkit().createImage("images/yellowTruckVertical.png"));
	    verticalTrucks.add(Toolkit.getDefaultToolkit().createImage("images/blueTruckVertical.png"));
	    verticalTrucks.add(Toolkit.getDefaultToolkit().createImage("images/purpleTruckVertical.png"));
	    
	    // horizontalTruks
	    horizontalTrucks.add(Toolkit.getDefaultToolkit().createImage("images/yellowTruckHorizontal.png"));
	    horizontalTrucks.add(Toolkit.getDefaultToolkit().createImage("images/blueTruckHorizontal.png"));
	    horizontalTrucks.add(Toolkit.getDefaultToolkit().createImage("images/purpleTruckHorizontal.png"));
	    
	    // verticalCars
	    verticalCars.add(Toolkit.getDefaultToolkit().createImage("images/orangeCarVertical.png"));
	    verticalCars.add(Toolkit.getDefaultToolkit().createImage("images/blueCarVertical.png"));
	    verticalCars.add(Toolkit.getDefaultToolkit().createImage("images/greenCarVertical.png"));
	    
	    // horizontalCars
	    horizontalCars.add(Toolkit.getDefaultToolkit().createImage("images/orangeCarHorizontal.png"));
	    horizontalCars.add(Toolkit.getDefaultToolkit().createImage("images/blueCarHorizontal.png"));
	    horizontalCars.add(Toolkit.getDefaultToolkit().createImage("images/greenCarHorizontal.png"));
        
        colorHorizontalTruck = 1;
	    colorVerticalTruck   = 3;
	    colorHorizontalCar   = 1;
	    colorVerticalCar     = 3;

    	java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                loadVehicles();
            }
        });
	}
	
	public void createModel() {
		model = new StdCreateCardModel();
	}
	
	public void createView() {
		final int height = 720;
		final int width  = 1280;
		
        createBoard = new PanelCreateCard();
        createBoard.setLayout(null);
        createBoard.setPreferredSize(new Dimension(width, height-100));
        
        sauvegarder = new JButton("Sauvegarder");
		sauvegarder.setPreferredSize(new Dimension(200, 50));
		
		effacer = new JButton("Effacer");
		effacer.setPreferredSize(new Dimension(200, 50));
		
		retour = new JButton("Retour");
		retour.setPreferredSize(new Dimension(200, 50));
	}
	
	public void placeComponent() {
		mainFrame.add(createBoard, BorderLayout.CENTER);
		
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER)); {
			p.add(sauvegarder);
			p.add(effacer);
			p.add(retour);
		}
		mainFrame.add(p, BorderLayout.SOUTH);
	}
	
	public void createController() {
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		effacer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.clear();
				java.awt.EventQueue.invokeLater(new Runnable() {
		            public void run() {
		                loadVehicles();
		            }
		        });
			}
		});
		
		sauvegarder.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				new CreateFile(model, createBoard).display();
			}
		});
		
		retour.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.getContentPane().removeAll();
				
				mainMenu = new MainMenu( mainFrame );
				mainFrame.setContentPane( mainMenu.getJFrame().getContentPane() );
				mainFrame.repaint();
			}
		});
	}

    public static void loadVehicles() {
        createBoard.removeAll();
       
        addNewVehicle(redCar, 310, 224, 139, 67, true);
        
        // initialisation des camions horizontaux
        addNewVehicle(getHorizontalTruck(), 20, 20, 207, 67, false);
        
        //initialisation des camions verticaux
       	addNewVehicle(getVerticalTruck(), 90, 110, 67, 207, false);
        
        //initialisation des voitures horizontales
        addNewVehicle(getHorizontalCar(), 50, 345, 139, 67, false);
        
        // initialisation des voitures verticales
        addNewVehicle(getVerticalCar(), 90, 440, 67, 139, false);
        
        createBoard.repaint();
    }

    public static void addNewVehicle(Image img, int posX, int posY, int width, int height, boolean red) {
    	int vehicleWidth = 0;
    	int vehicleHeight = 0;
    	
    	switch(width) {
    		case 67 : vehicleWidth = 1; break;
    		case 139 : vehicleWidth = 2; break;
    		default : vehicleWidth = 3; break;
    	}
    	
    	switch(height) {
	    	case 67 : vehicleHeight = 1; break;
			case 139 : vehicleHeight = 2; break;
			default : vehicleHeight = 3; break;
    	}
    	
    	// Créer un DraggableImageComponent et ajoute son image
        DraggableImageComponent comp = new DraggableImageComponent(model, vehicleWidth, vehicleHeight, red);
        createBoard.add(comp); // Ajoute ce Component sur la frame
        comp.setImage(img); // Modifie l'image
        comp.setOverbearing(true); // Avec un click, ce component est prioritaire sur les autres

        comp.setSize(width, height);
        comp.setLocation(posX, posY);
        
        createBoard.repaint();
    }
    
    public static void addNewHorizontalTruck() {
    	addNewVehicle(getHorizontalTruck(), 20, 20, 207, 67, false);
    }
    
    public static void addNewVerticalTruck() {
    	addNewVehicle(getVerticalTruck(), 90, 110, 67, 207, false);
    }
    
    public static void addNewHorizontalCar() {
    	addNewVehicle(getHorizontalCar(), 50, 345, 139, 67, false);
    }

    public static void addNewVerticalCar() {
    	addNewVehicle(getVerticalCar(), 90, 440, 67, 139, false);
    }
    
    public static int getRandom(int range) {
        int r = (int) (Math.random() * range) - range;
        return r;
    }
    
    // OUTILS GRAPHIC
	private static Image getVerticalTruck() {
		int nb = colorVerticalTruck%3;
		colorVerticalTruck ++;
		
		return verticalTrucks.get(nb);
	}
	
	private static Image getHorizontalTruck() {
		int nb = colorHorizontalTruck%3;
		colorHorizontalTruck ++;
		
		return horizontalTrucks.get(nb);
	}
	
	private static Image getVerticalCar() {
		int nb = colorVerticalCar%3;
		colorVerticalCar ++;
		
		return verticalCars.get(nb);
	}
	
	private static Image getHorizontalCar() {
		int nb = colorHorizontalCar%3;
		colorHorizontalCar ++;
		
		return horizontalCars.get(nb);
	}
	
	public JFrame getJFrame(){
		return mainFrame;
	}
}