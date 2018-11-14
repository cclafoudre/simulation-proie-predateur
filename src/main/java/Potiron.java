import java.awt.*;

public class Potiron extends Vivant {
    public static Color COULEUR =  new Color(255, 81,0);
    public Potiron() {
        super(10);
        setBackground(COULEUR);
    }
    public static Color getCouleur() {
        return COULEUR;
    }
}
