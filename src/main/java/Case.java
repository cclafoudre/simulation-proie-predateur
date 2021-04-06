import javax.swing.*;
import java.awt.*;

public class Case extends JPanel {
    public Case auDessus;
    public Case enDessous;
    public Case aDroite;
    public Case aGauche;
    public int x;
    public int y;
    public Espece monEspece;
    public static Color COULEUR_VIDE = new Color(46, 255, 1);
    public JLabel texte = new JLabel(" ");
    public int Energie;

    public Case() {
        super();
        Energie=0; //par défaut il est mort et c'est une case vide
        setBackground(COULEUR_VIDE);
        add(texte);
    }

    public void setEspece(Espece monEspece) {
        this.monEspece = monEspece;
        setBackground(monEspece.getCouleur());

    }
}
