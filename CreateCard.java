package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.DraggableImageComponent;

public class CreateCard {

    public static JFrame frame;
    public static PanelCreateCard createBoard;
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
	
	public CreateCard() {
		initGraphic();
		createModel();
		createView();
		placeComponent();
		createController();
	}
	
	public void display() {
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
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
		
	}
	
	public void createView() {
		final int height = 720;
		final int width  = 1280;
		
		frame = new JFrame("Créateur de carte");
        frame.setPreferredSize(new Dimension(width, height));
        frame.setLayout(new BorderLayout());
		
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
		frame.add(createBoard, BorderLayout.CENTER);
		
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER)); {
			p.add(sauvegarder);
			p.add(effacer);
			p.add(retour);
		}
		frame.add(p, BorderLayout.SOUTH);
	}
	
	public void createController() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		effacer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				java.awt.EventQueue.invokeLater(new Runnable() {
		            public void run() {
		                loadVehicles();
		            }
		        });
			}
			
		});
	}

    public static void loadVehicles() {
        createBoard.removeAll();
       
        addNewVehicle(redCar, 310, 224, 139, 67, true);
        
        // initialisation des camions horizontaux
        for (int i = 0; i < 4; i++) {
        	addNewVehicle(getHorizontalTruck(), 20, 20, 207, 67, false);
        }
        
        //initialisation des camions verticaux
        for (int i = 0; i < 4; i++) {
        	addNewVehicle(getVerticalTruck(), 90, 110, 67, 207, false);
        }
        
        //initialisation des voitures horizontales
        for (int i = 0; i < 6; i++) {
        	addNewVehicle(getHorizontalCar(), 50, 345, 139, 67, false);
        }
        
        // initialisation des voitures verticales
        for (int i = 0; i < 6; i++) {
        	addNewVehicle(getVerticalCar(), 90, 440, 67, 139, false);
        }
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
    	
        //Creates a draggableImageComponent and adds loaded image
        DraggableImageComponent comp = new DraggableImageComponent(vehicleWidth, vehicleHeight, red);
        createBoard.add(comp);//Adds this component to main container
        comp.setImage(img);//Sets image
        comp.setOverbearing(true);//On click ,this panel gains lowest z-buffer

        comp.setSize(width, height);
        comp.setLocation(posX, posY);
        
        createBoard.repaint();
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
	
	// MAIN
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CreateCard().display();
            }
        });
	}
}


