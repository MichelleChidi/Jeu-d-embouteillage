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
    private Coord lastCoord;
    private boolean isRed;
    private static Board model;
    private Vehicle veh;
    private Coord before, after;
    private int min, max;

    public DraggableComponentGame(Board m, Vehicle v, Coord c) {
    	model    = m;
    	veh      = v;
    	
    	isRed = veh.isRed();
    	
        addDragListeners();
        setOpaque(true);
        setBackground(new Color(240,240,240));
        
        lastCoord = c;
        
        int[] pixel = coordToPixel(c);
        lastX = pixel[0];
        lastY = pixel[1];
        setLocation(lastX, lastY);
    	
    	if (veh.getDirection() == Direction.HORIZONTAL) {
			min = 280;
			
			max = 636;
    	} else {
			min = 81;
			
			max = 438;
    	}
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
                if (veh.getDirection() == Direction.HORIZONTAL) {
                	if ((int) position.getX() <= min) {
            			setLocation(new Point(min, lastY));
            		} else if ((int) position.getX() >= max) {
            			setLocation(new Point(max, lastY));
            		} else {
            			setLocation(new Point((int) position.getX(),lastY));
            		}
                } else {
                	if ((int) position.getY() <= min) {
            			setLocation(new Point(lastX, min));
            		} else if ((int) position.getY() >= max) {
            			setLocation(new Point(lastX, max));
            		} else {
            			setLocation(new Point(lastX, (int) position.getY()));
            		}
                }

                //Change le Z-Buffer si il est "overbearing"
                if (overbearing) {
                    getParent().setComponentZOrder(handle, 0);
                    repaint();
                }
                repaint();
            }
        });
        
        addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) { }
			
			public void mouseEntered(MouseEvent e) { }

			public void mouseExited(MouseEvent e) {	}

			public void mousePressed(MouseEvent e) {
				int[] bestCoordX = {280, 352, 423, 494, 565, 636}; // coordonnées X disponible pour placer le véhicule
            	int[] bestCoordY = {81, 152, 223, 294, 366, 438}; // coordonnées Y disponible pour placer le véhicule
            	
				Coord before = model.getFirstVehicleBefore(veh);
            	Coord after  = model.getFirstVehicleAfter(veh);
            	
            	if (veh.getDirection() == Direction.HORIZONTAL) {
        			min = bestCoordX[before.getCol()];
        			
        			max = bestCoordX[after.getCol()];
            	} else {
        			min = bestCoordY[before.getRow()];
        			
        			max = bestCoordY[after.getRow()];
            	}
			}
			
			public void mouseReleased(MouseEvent e) {
				Point position = getLocation();
				
				int[] tab = getBestCoord(position.getX(), position.getY());
				
            	
            	setLocation(new Point(tab[0], tab[1]));
            	
            	Coord newCoord = pixelToCoord(tab[0], tab[1]);
            	model.moveTo(veh, newCoord);
            	
            	if (model.hasWon()) {
            		System.out.println("Victoire !!");
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
    	
    	
    	
    	tmpY = (int) Math.abs(bestCoordY[0] - y);
		for (int i = 1; i < (bestCoordY.length-(veh.getSize()-1)); i++) {
			if (Math.abs(bestCoordY[i] - y) < tmpY) {
				tmpY = (int) Math.abs(bestCoordY[i] - y);
				bestY = i;
			}
		}
		
		if (veh.getDirection() == Direction.HORIZONTAL) {
			tmpX = (int) Math.abs(bestCoordX[0] - x);
			for (int i = 1; i < (bestCoordX.length-(veh.getSize()-1)); i++) {
				if (Math.abs(bestCoordX[i] - x) < tmpX) {
					tmpX = (int) Math.abs(bestCoordX[i] - x);
					bestX = i;
				}
			}
			
			if (bestCoordX[bestX] < min) {
				bestCoord[0] = min;
			} else if (bestCoordX[bestX] > max) {
				bestCoord[0] = max;
			} else {
				bestCoord[0] = bestCoordX[bestX];
			}
			bestCoord[1] = lastY;
		} else {
			tmpY = (int) Math.abs(bestCoordY[0] - y);
			for (int i = 1; i < (bestCoordY.length-(veh.getSize()-1)); i++) {
				if (Math.abs(bestCoordY[i] - y) < tmpY) {
					tmpY = (int) Math.abs(bestCoordY[i] - y);
					bestY = i;
				}
			}
			
			bestCoord[0] = lastX;
			if (bestCoordY[bestY] < min) {
				bestCoord[1] = min;
			} else if (bestCoordY[bestY] > max) {
				bestCoord[1] = max;
			} else {
				bestCoord[1] = bestCoordY[bestY];
			}
			
		}

		return bestCoord;
    }
    
	private int[] coordToPixel(Coord coord) {
		int[] bestX = {280, 352, 423, 494, 565, 636};
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
    	
    	Coord c = new StdCoord(coordY, coordX);
    	return c;
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
