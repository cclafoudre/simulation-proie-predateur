import java.awt.*;

public class Potiron extends Vivant {
    public static final Color COULEUR =  new Color(255, 166, 18);
    public static int PV_INITIAL=10;
    public Potiron() {
        super(PV_INITIAL);
    }
    public Color getCouleur() {
        return COULEUR;
    }
}
