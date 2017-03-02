package model;

/*
 *  Copyright 2010 De Gregorio Daniele.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;

public class DraggableComponent extends JComponent {

    /** If sets <b>TRUE</b> this component is draggable */
    private boolean draggable = true;
    /** 2D Point representing the coordinate where mouse is, relative parent container */
    protected Point anchorPoint;
    /** Default mouse cursor for dragging action */
    protected Cursor draggingCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    /** If sets <b>TRUE</b> when dragging component, it will be painted over each other (z-Buffer change) */
    protected boolean overbearing = false;
    private int vehicleWidth;
    private int vehicleHeight;
    private int lastX;
    private int lastY;
    private boolean isRed;

    public DraggableComponent(int width, int height, boolean red) {
    	vehicleWidth  = width;
    	vehicleHeight = height;
    	isRed         = red;
        addDragListeners();
        setOpaque(true);
        setBackground(new Color(240,240,240));
    }

    /**
     * We have to define this method because a JComponent is a void box. So we have to
     * define how it will be painted. We create a simple filled rectangle.
     *
     * @param g Graphics object as canvas
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * Add Mouse Motion Listener with drag function
     */
    private void addDragListeners() {
        /** This handle is a reference to THIS beacause in next Mouse Adapter "this" is not allowed */
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
                lastX = (int) position.getX();
                lastY = (int) position.getY();

                //Change Z-Buffer if it is "overbearing"
                if (overbearing) {
                    getParent().setComponentZOrder(handle, 0);
                    repaint();
                }
            }
        });
        
        addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) { }
			
			public void mouseEntered(MouseEvent e) { }

			public void mouseExited(MouseEvent e) { }

			public void mousePressed(MouseEvent e) {	}

			public void mouseReleased(MouseEvent e) {
				int anchorX = anchorPoint.x;
                int anchorY = anchorPoint.y;

                Point parentOnScreen = getParent().getLocationOnScreen();
                Point mouseOnScreen = e.getLocationOnScreen();
                
                double posX = mouseOnScreen.x - parentOnScreen.x - anchorX;
                double posY = mouseOnScreen.y - parentOnScreen.y - anchorY;
                Point position = new Point(getBestCoordX(posX), getBestCoordY(posY) );
                setLocation(position);
			}
        	
        });
    }
    
    /**
     * This private method is used to obtain the best X coordonate
     */
    private int getBestCoordX(double x) {
    	int[] bestCoord = {311, 382, 453, 524, 595, 666}; // coordonnées X disponible pour placer le véhicule
    	
    	int tmp = (int) Math.abs(bestCoord[0] - x);
    	int best = 0;
    	
    	if (isRed) {
    		for (int i = 1; i < (bestCoord.length-(vehicleWidth-1)); i++) {
    			if (Math.abs(bestCoord[i] - x) < tmp) {
    				tmp = (int) Math.abs(bestCoord[i] - x);
    				best = i;
    			}
    		}
    		lastX = bestCoord[best];
    		return bestCoord[best];
    	}
    	
    	if (x >= 230 && x <= 737) { // limites du board
    		for (int i = 1; i < (bestCoord.length-(vehicleWidth-1)); i++) {
    			if (Math.abs(bestCoord[i] - x) < tmp) {
    				tmp = (int) Math.abs(bestCoord[i] - x);
    				best = i;
    			}
    		}
    		lastX = bestCoord[best];
    		return bestCoord[best];
    	}
    	return lastX;
    }
    
    /**
     * This private method is used to obtain the best Y coordonate
     */
    private int getBestCoordY(double y) {
    	int[] bestCoord = {81, 152, 223, 294, 366, 438}; // coordonnées Y disponible pour placer le véhicule
    	
    	int tmp = (int) Math.abs(bestCoord[0] - y);
    	int best = 0;
    	
    	if (isRed) {
    		return bestCoord[2];
    	}
    	
    	if (y >= 30 && y <= 507) { // limites du board
    		for (int i = 1; i < (bestCoord.length-(vehicleHeight-1)); i++) {
    			if (Math.abs(bestCoord[i] - y) < tmp) {
    				tmp = (int) Math.abs(bestCoord[i] - y);
    				best = i;
    			}
    		}
    		lastY = bestCoord[best];
    		return bestCoord[best];
    	}
    	return lastY;
    }


    /**
     * Remove all Mouse Motion Listener. Freeze component.
     */
    private void removeDragListeners() {
        for (MouseMotionListener listener : this.getMouseMotionListeners()) {
            removeMouseMotionListener(listener);
        }
        setCursor(Cursor.getDefaultCursor());
    }

    /**
     * Get the value of draggable
     *
     * @return the value of draggable
     */
    public boolean isDraggable() {
        return draggable;
    }

    /**
     * Set the value of draggable
     *
     * @param draggable new value of draggable
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
     * Get the value of draggingCursor
     *
     * @return the value of draggingCursor
     */
    public Cursor getDraggingCursor() {
        return draggingCursor;
    }

    /**
     * Set the value of draggingCursor
     *
     * @param draggingCursor new value of draggingCursor
     */
    public void setDraggingCursor(Cursor draggingCursor) {
        this.draggingCursor = draggingCursor;
    }

    /**
     * Get the value of overbearing
     *
     * @return the value of overbearing
     */
    public boolean isOverbearing() {
        return overbearing;
    }

    /**
     * Set the value of overbearing
     *
     * @param overbearing new value of overbearing
     */
    public void setOverbearing(boolean overbearing) {
        this.overbearing = overbearing;
    }
}
