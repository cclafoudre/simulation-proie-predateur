import javax.swing.*;
import java.awt.*;

public class Vivant extends JPanel {
    public Vivant auDessus;
    public Vivant enDessous;
    public Vivant aDroite;
    public Vivant aGauche;
    public int x;
    public int y;

    public static Color COULEUR_VIDE = new Color(46, 255, 1);
    public static Color COULEUR_LAPIN = new Color(253, 253, 253);


    public static Color COULEUR;

    public JLabel texte = new JLabel(" ");

    public int pointsDeVie;

    public Vivant() {
        super();
        pointsDeVie=0; //par défaut il est mort, c'est une case vide
        setBackground(COULEUR_VIDE);
        add(texte);
    }
    public Vivant(int pv) {
        this();
        pointsDeVie = pv;
    }

    public void setX(int x) {
        this.x = x;
    }

    public static Color getCouleur() {
        return COULEUR;
    }
}
