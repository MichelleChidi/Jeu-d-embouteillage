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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

import view.CreateCard;

/**
 * Cette classe est une extension à DraggableComponent qui sert à customiser un composant avec une image
 * grâce à la méthode <b>setImage</b>. Vous pouvez l'utiliser comme une simple image de Panel avec
 * <b>setDraggable(false)</b>.
 */
public class DraggableImageComponent extends DraggableComponent implements ImageObserver {

    protected Image image;
    private boolean autoSize = false;
    private Dimension autoSizeDimension = new Dimension(0, 0);

    public DraggableImageComponent(CreateCardModel model, int width, int height, boolean red) {
        super(model, width, height, red);
        setLayout(null);
        setBackground(Color.black);
    }

    /**
     * Cette méthode dessine une image sur le composant s'il n'y en pas déjà une. Sinon elle
     * dessine un Background avec une couleur.
	 * Si <b>autoSize</b> est vrai, l'image sera dessiner son rapport largeur/hauteur sur un rectangle blanc
	 * de la même taille.
     * @param g
     */
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0, 0, getWidth(), getHeight());
        if (image != null) {
            setAutoSizeDimension();
            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * C'est une simple technique pour redimensionner l'image, tout en garde son ratio l/h.
     * @param source
     * @param dest
     * @return
     */
    private Dimension adaptDimension(Dimension source, Dimension dest) {
        int sW = source.width;
        int sH = source.height;
        int dW = dest.width;
        int dH = dest.height;
        double ratio = ((double) sW) / ((double) sH);
        if (sW >= sH) {
            sW = dW;
            sH = (int) (sW / ratio);
        } else {
            sH = dH;
            sW = (int) (sH * ratio);
        }
        return new Dimension(sW, sH);
    }

    /**
     * Vérifie si l'image est totalement chargé.
     *
     * @param img l'objet de l'image
     * @param infoflags est égal à <b>ALLBITS</b> quand le chargement est fini
     * @param x position x
     * @param y position y
     * @param w largeur
     * @param h hauteur
     * @return TRUE si l'image peut générer des événements, FALSE sinon
     */
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
        if (infoflags == ALLBITS) {
            repaint();
            setAutoSizeDimension();
            return false;
        }
        return true;
    }

    /**
     * Cette méthode est utilisé pour redimensionner une image avec sa taille initiale. 
     */
    private void setAutoSizeDimension() {
        if (!autoSize) {
            return;
        }
        if (image != null) {
            if (image.getHeight(null) == 0 || getHeight() == 0) {
                return;
            }
            if ((getWidth() / getHeight()) == (image.getWidth(null) / (image.getHeight(null)))) {
                return;
            }
            autoSizeDimension = adaptDimension(new Dimension(image.getWidth(null), image.getHeight(null)), this.getSize());
            setSize(autoSizeDimension.width, autoSizeDimension.height);
        }
    }

    /**
     * Cette méthode est utilisé pour redimensionner le composant quand la valeur autoSize est TRUE.
     * @param pixels
     */
    public void grow(int pixels) {
        double ratio = getWidth() / getHeight();
        setSize(getSize().width + pixels, (int) (getSize().height + (pixels / ratio)));
    }

    /**
     * Retourne la valeur de autoSize.
     *
     * @return valeur de autoSize
     */
    public boolean isAutoSize() {
        return autoSize;
    }

    /**
     * Modifie la valeur de autoSize.
     *
     * @param autoSize nouvelle valeur de autoSize
     */
    public void setAutoSize(boolean autoSize) {
        this.autoSize = autoSize;
    }

    /**
     * Retourne l'image utilisé.
     *
     * @return image utilisé
     */
    public Image getImage() {
        return image;
    }

    /**
     * Modifie l'image utilisé en utilisant son chemin d'accès. Utilise ToolKit pour créer l'image dans un fichier.
     * Il faut utilisé setImage(Image image) s'il y a juste une image.
     *
     * @param image chemin d'accès de l'image
     */
    public void setImage(String image) {
        setImage(Toolkit.getDefaultToolkit().getImage(image));
    }

    /**
     * Modifie l'image utilisé.
     *
     * @param image nouvelle image
     */
    public void setImage(Image image) {
        this.image = image;
        repaint();
        setAutoSizeDimension();
    }
}
