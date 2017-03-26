package model;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;

import view.CreateCard;

public class DraggableComponent extends JComponent {

    /** Si c'est <b>TRUE</b> ce composant est draggable */
    private boolean draggable = true;
    /** Point en 2D qui représente la coordonnée de la souris, relative au composant père */
    protected Point anchorPoint;
    /** Curseur par défaut pour drag un composent */
    protected Cursor draggingCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    /** Si c'est <b>TRUE</b> quand le composant est en mouvement, il va être déssiner au dessus des autres (z-Buffer change) */
    protected boolean overbearing = false;
    private int vehicleWidth;
    private int vehicleHeight;
    private int lastX;
    private int lastY;
    private boolean isRed;
    private int nbPlaced;
    private static CreateCardModel model;
    private Vehicle veh;

    public DraggableComponent(CreateCardModel m, int width, int height, boolean red) {
    	model         = m;
    	vehicleWidth  = width;
    	vehicleHeight = height;
    	isRed         = red;
    	nbPlaced      = 0;
    	
    	if (width == 3 && height == 1) {
    		lastX = 20;
    		lastY = 20;
    		veh = new StdVehicle(Type.TRUCK, Direction.HORIZONTAL);
    	}
    	if (width == 1 && height == 3) {
    		lastX = 90;
    		lastY = 110;
    		veh = new StdVehicle(Type.TRUCK, Direction.VERTICAL);
    	}
    	if (width == 2 && height == 1 && !isRed) {
    		lastX = 50;
    		lastY = 345;
    		veh = new StdVehicle (Type.CAR, Direction.HORIZONTAL);
    	}
    	if (width == 1 && height == 2 && !isRed) {
    		lastX = 90;
    		lastY = 440;
    		veh = new StdVehicle(Type.CAR, Direction.VERTICAL);
    	}
    	if (isRed) {
    		lastX = 311;
    		lastY = 223;
    		veh = new StdVehicle(Type.RED, Direction.HORIZONTAL);
    		model.addVehicle(veh, new StdCoord(2,0));
    	}
    	
        addDragListeners();
        setOpaque(true);
        setBackground(new Color(240,240,240));
    }

    /**
     * Cette méthode sert a le composant et de l'associer au graphic.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * Ajoute un Mouse Motion Listener avec ses fonctions de drag
     */
    private void addDragListeners() {
        final DraggableComponent handle = this;
        addMouseMotionListener(new MouseAdapter() {
        	
            public void mouseMoved(MouseEvent e) {
                anchorPoint = e.getPoint();
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            public void mouseDragged(MouseEvent e) {
                int anchorX = anchorPoint.x;
                int anchorY = anchorPoint.y;

                Point parentOnScreen = getParent().getLocationOnScreen();
                Point mouseOnScreen = e.getLocationOnScreen();
                Point position = new Point(mouseOnScreen.x - parentOnScreen.x - anchorX, mouseOnScreen.y - parentOnScreen.y - anchorY);
                if (isRed) {
                	setLocation(new Point((int) position.getX(), 223));
                } else {
                	setLocation(position);
                }

                //Change le Z-Buffer si il est "overbearing"
                if (overbearing) {
                    getParent().setComponentZOrder(handle, 0);
                    repaint();
                }
            }
        });
        
        addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) { }
			
			public void mouseEntered(MouseEvent e) { }

			public void mouseExited(MouseEvent e) {	}

			public void mousePressed(MouseEvent e) { }
			
			public void mouseReleased(MouseEvent e) {
				int anchorX = anchorPoint.x;
                int anchorY = anchorPoint.y;

                Point parentOnScreen = getParent().getLocationOnScreen();
                Point mouseOnScreen = e.getLocationOnScreen();
                
                double posX = mouseOnScreen.x - parentOnScreen.x - anchorX;
                double posY = mouseOnScreen.y - parentOnScreen.y - anchorY;
                if (posX >= 800 && !isRed) {
                	setSize(new Dimension(0,0));
                	
                	if (nbPlaced == 0) {
	                	createVehicle(vehicleWidth, vehicleHeight, isRed);
                	}
                	if (nbPlaced > 0) {
                		model.removeVehicle(veh, pixelToCoord(lastX,lastY));
                	}
                	veh = null;
                	System.out.println("Suppression 1");
                	System.out.println(model.toString());
                	nbPlaced ++;
				} else {
					if (nbPlaced > 1 || isRed) {
                		model.removeVehicle(veh, pixelToCoord(lastX, lastY));
                		System.out.println("Suppression 2");
                    	System.out.println(model.toString());
                	}
					
					int[] tab = getBestCoord(posX, posY);
					
                	Point position = new Point(tab[0], tab[1]);
                	setLocation(position);
                	model.addVehicle(veh, pixelToCoord(tab[0], tab[1]));
                	System.out.println("Ajout");
                	System.out.println(model.toString());
                }
			}
        	
        });
    }
    
    private int[] getBestCoord (double x, double y) {
    	int[] bestCoordX = {311, 382, 453, 524, 595, 666}; // coordonnées X disponible pour placer le véhicule
    	int[] bestCoordY = {81, 152, 223, 294, 366, 438}; // coordonnées Y disponible pour placer le véhicule
    	int[] bestCoord = new int[2];
    	
    	int tmpX = (int) Math.abs(bestCoordX[0] - x);
    	int tmpY = (int) Math.abs(bestCoord[0] - y);
    	int bestX = 0;
    	int bestY = 0;
    	
    	// Voiture rouge
    	if (isRed) {
    		for (int i = 1; i < (bestCoordX.length-(vehicleWidth-1)); i++) {
    			if (Math.abs(bestCoordX[i] - x) < tmpX) {
    				tmpX = (int) Math.abs(bestCoordX[i] - x);
    				bestX = i;
    			}
    		}
    		
    		if (model.isFree(veh, pixelToCoord(bestCoordX[bestX], bestCoordY[2]))) {
    			bestCoord[0] = bestCoordX[bestX];
    			bestCoord[1] = bestCoordY[2];
        		
        		lastX = bestCoord[0];
        		lastY = bestCoord[1];
    		} else {
    			bestCoord[0] = lastX;
    			bestCoord[1] = bestCoordY[2];
    		}
    		
    		return bestCoord;
    	} 
    	
    	if (x < 230 || x > 738) {
    		if (nbPlaced > 0) {
    			bestCoord[0] = lastX;
    			bestCoord[1] = lastY;
    			return bestCoord;
    		} else {
	    		nbPlaced = 0;
	    		lastX = xInitiale();
	    		lastY = yInitiale();
	    		bestCoord[0] = lastX;
	    		bestCoord[1] = lastY;
	    		return bestCoord;
    		}
    	}

    	// déplacement sur plateau
    	tmpX = (int) Math.abs(bestCoordX[0] - x);
    	if (x >= 230 && x <= 737) { // limites du board
    		for (int i = 1; i < (bestCoordX.length-(vehicleWidth-1)); i++) {
    			if (Math.abs(bestCoordX[i] - x) < tmpX) {
    				tmpX = (int) Math.abs(bestCoordX[i] - x);
    				bestX = i;
    			}
    		}
    	}
    	
    	tmpY = (int) Math.abs(bestCoordY[0] - y);
    	if (y >= 30 && y <= 507) { // limites du board
    		for (int i = 1; i < (bestCoordY.length-(vehicleHeight-1)); i++) {
    			if (Math.abs(bestCoordY[i] - y) < tmpY) {
    				tmpY = (int) Math.abs(bestCoordY[i] - y);
    				bestY = i;
    			}
    		}
    	}
    		
		if (model.isFree(veh, pixelToCoord(bestCoordX[bestX], bestCoordY[bestY]))) {
			lastX = bestCoordX[bestX];
			lastY = bestCoordY[bestY];
			
		}
		bestCoord[0] = lastX;
		bestCoord[1] = lastY;	
		if (nbPlaced == 0) {
    		createVehicle (vehicleWidth, vehicleHeight, isRed);
    	}
		nbPlaced ++;
    
    	return bestCoord;
    }
    
    private Coord pixelToCoord (int x, int y) {
    	int[] bestX = {311, 382, 453, 524, 595, 666};
    	int[] bestY = {81, 152, 223, 294, 366, 438};
    	
    	int coordX = 0;
    	for (int i = 0; i < bestX.length; i++) {
    		if (bestX[i] == x) {
    			coordX = i;
    		}
    	}
    	
    	int coordY = 0;
    	for (int i = 0; i < bestY.length; i++) {
    		if (bestY[i] == y) {
    			coordY = i;
    		}
    	}
    	
    	return new StdCoord(coordY, coordX);
    }
    
    /**
     * Cette méthode privé sert à créer un véhicule avec un largeur et une hauteur
     * @param width largeur du véhicule
     * @param height hauteur du véhicule
     * @param red true si la voiture est rouge
     */
    private static void createVehicle (int width, int height, boolean red) {
    	if (width == 3 && height == 1) {
    		CreateCard.addNewHorizontalTruck();
    	}
    	if (width == 1 && height == 3) {
    		CreateCard.addNewVerticalTruck();
    	}
    	if (width == 2 && height == 1 && !red) {
    		CreateCard.addNewHorizontalCar();
    	}
    	if (width == 1 && height == 2 && !red) {
    		CreateCard.addNewVerticalCar();
    	}
    }

    private int xInitiale() {
    	if (vehicleWidth == 3 && vehicleHeight == 1) {
    		return 20;
    	}
    	if (vehicleWidth == 1 && vehicleHeight == 3) {
    		return 90;
    	}
    	if (vehicleWidth == 2 && vehicleHeight == 1 && !isRed) {
    		return 50;
    	}
    	if (vehicleWidth == 1 && vehicleHeight == 2 && !isRed) {
    		return 90;
    	}
    	return 0;
    }
    
    private int yInitiale() {
    	if (vehicleWidth == 3 && vehicleHeight == 1) {
    		return 20;
    	}
    	if (vehicleWidth == 1 && vehicleHeight == 3) {
    		return 110;
    	}
    	if (vehicleWidth == 2 && vehicleHeight == 1 && !isRed) {
    		return 345;
    	}
    	if (vehicleWidth == 1 && vehicleHeight == 2 && !isRed) {
    		return 440;
    	}
    	return 0;
    }

    /**
     * Enlève tous les Mouse Motion Listener et gèle le composant.
     */
    private void removeDragListeners() {
        for (MouseMotionListener listener : this.getMouseMotionListeners()) {
            removeMouseMotionListener(listener);
        }
        setCursor(Cursor.getDefaultCursor());
    }

    /**
     * Retourne la valeur de <code>draggable</code>
     *
     * @return valeur de <code>draggable</code>
     */
    public boolean isDraggable() {
        return draggable;
    }

    /**
     * Modifie la valeur de <code>draggable</code>
     *
     * @param nouvelle valeur de <code>draggable</code>
     */
    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
        if (draggable) {
            addDragListeners();
        } else {
            removeDragListeners();
        }

    }

    /**
     * Retourne le curseur utilisé pour drag un composant
     *
     * @return curseur utilisé
     */
    public Cursor getDraggingCursor() {
        return draggingCursor;
    }

    /**
     * Modifie le curseur utilisé pour drag un composant
     *
     * @param nouvelle valeur du curseur utilisé
     */
    public void setDraggingCursor(Cursor draggingCursor) {
        this.draggingCursor = draggingCursor;
    }

    /**
     * Retourne la valeur de <code>overbearing</code>
     *
     * @return valeur de <code>overbearing</code>
     */
    public boolean isOverbearing() {
        return overbearing;
    }

    /**
     * Modifie la valeur de <code>overbearing</code>
     *
     * @param nouvelle valeur de <code>overbearing</code>
     */
    public void setOverbearing(boolean overbearing) {
        this.overbearing = overbearing;
    }
}
