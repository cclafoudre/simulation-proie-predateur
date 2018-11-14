import javax.swing.*;
import java.awt.*;

public class Case extends JPanel {
    public Case auDessus;
    public Case enDessous;
    public Case aDroite;
    public Case aGauche;
    public int x;
    public int y;
    public Vivant monVivant;
    public static Color COULEUR_VIDE = new Color(46, 255, 1);
    public static Color COULEUR_POTIRON =  new Color(255, 81,0);

    public JLabel texte = new JLabel(" ");

    public int pointsDeVie;

    public Case() {
        super();
        pointsDeVie=0; //par défaut il est mort, c'est une case vide
        setBackground(COULEUR_VIDE);
        add(texte);
    }

    public void setVivant(Vivant monVivant) {
        this.monVivant = monVivant;
        setBackground(monVivant.getCouleur());

    }
}
