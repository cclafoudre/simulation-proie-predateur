import javax.swing.*;
import java.awt.*;

public class Vivant {
    public Vivant auDessus;
    public Vivant enDessous;
    public Vivant aDroite;
    public Vivant aGauche;
    public int x;
    public int y;

    public static Color COULEUR = new Color(46, 255, 1);
    public int pointsDeVie = 0;

    public Vivant() {}
    public Vivant(int pv) {
        this();
        pointsDeVie = pv;
    }

    public void setX(int x) {
        this.x = x;
    }

    public Color getCouleur() {
        return COULEUR;
    }
}
