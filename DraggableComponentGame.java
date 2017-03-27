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

public class DraggableComponentGame extends JComponent {
	
	/** Si c'est <b>TRUE</b> ce composant est draggable */
    private boolean draggable = true;
    /** Point en 2D qui représente la coordonnée de la souris, relative au composant père */
    protected Point anchorPoint;
    /** Curseur par défaut pour drag un composent */
    protected Cursor draggingCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    /** Si c'est <b>TRUE</b> quand le composant est en mouvement, il va être déssiner au dessus des autres (z-Buffer change) */
    protected boolean overbearing = false;
    private int lastX;
    private int lastY;
    private boolean isRed;
    private boolean free;
    private int nbPlaced;
    private static Board model;
    private Vehicle veh;

    public DraggableComponentGame(Board m, Vehicle v, Coord c) {
    	model    = m;
    	veh      = v;
    	nbPlaced = 0;
    	
    	isRed = veh.isRed();
    	
        addDragListeners();
        setOpaque(true);
        setBackground(new Color(240,240,240));
        
        int[] pixel = coordToPixel(c);
        lastX = pixel[0];
        lastY = pixel[1];
        setLocation(lastX, lastY);
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
        final DraggableComponentGame handle = this;
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
               
                	nbPlaced ++;
				} else {
					int[] tab = getBestCoord(posX, posY);
					
                	Point position = new Point(tab[0], tab[1]);
                	setLocation(position);
                }
			}
        	
        });
    }
    
    private int[] getBestCoord (double x, double y) {
    	int[] bestCoordX = {280, 352, 423, 494, 565, 636}; // coordonnées X disponible pour placer le véhicule
    	int[] bestCoordY = {81, 152, 223, 294, 366, 438}; // coordonnées Y disponible pour placer le véhicule
    	int[] bestCoord = new int[2];
    	
    	int tmpX = (int) Math.abs(bestCoordX[0] - x);
    	int tmpY = (int) Math.abs(bestCoord[0] - y);
    	int bestX = 0;
    	int bestY = 0;
    	
    	// Voiture rouge
    	if (isRed) {
    		for (int i = 1; i < (bestCoordX.length-(veh.getSize()-1)); i++) {
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
    	
    	if (x < 230 || (x > 738 && x < 800)) {
    		if (nbPlaced > 0) {
    			bestCoord[0] = lastX;
    			bestCoord[1] = lastY;
    			return bestCoord;
    		} else {
	    		nbPlaced = 0;
	    		bestCoord[0] = lastX;
	    		bestCoord[1] = lastY;
	    		return bestCoord;
    		}
    	}

    	// déplacement sur plateau
    	tmpX = (int) Math.abs(bestCoordX[0] - x);
    	if (x >= 230 && x <= 737) { // limites du board
    		for (int i = 1; i < (bestCoordX.length-(veh.getSize()-1)); i++) {
    			if (Math.abs(bestCoordX[i] - x) < tmpX) {
    				tmpX = (int) Math.abs(bestCoordX[i] - x);
    				bestX = i;
    			}
    		}
    	}
    	
    	tmpY = (int) Math.abs(bestCoordY[0] - y);
    	if (y >= 30 && y <= 507) { // limites du board
    		for (int i = 1; i < (bestCoordY.length-(veh.getSize()-1)); i++) {
    			if (Math.abs(bestCoordY[i] - y) < tmpY) {
    				tmpY = (int) Math.abs(bestCoordY[i] - y);
    				bestY = i;
    			}
    		}
    	}
    	
    	free = false;
		if (model.isFree(veh, pixelToCoord(bestCoordX[bestX], bestCoordY[bestY]))) {
			lastX = bestCoordX[bestX];
			lastY = bestCoordY[bestY];
			free  = true;
		}
		bestCoord[0] = lastX;
		bestCoord[1] = lastY;	
		if (free) {
			nbPlaced ++;
		} else {
			
		}
    
    	return bestCoord;
    }
    
	private int[] coordToPixel(Coord coord) {
		int[] bestX = {281, 352, 423, 494, 565, 636};
    	int[] bestY = {81, 152, 223, 294, 366, 438};
    	
    	int[] best = new int[2];
    	
    	best[0] = bestX[coord.getCol()];
    	best[1] = bestY[coord.getRow()];
    	
    	return best;
	}
    
    private Coord pixelToCoord (int x, int y) {
    	int[] bestX = {281, 352, 423, 494, 565, 636};
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
