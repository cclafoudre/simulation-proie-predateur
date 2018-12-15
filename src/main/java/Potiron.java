import java.awt.*;

/**
 * Un potion. Il peut se d&eacute;placer mais ne peut regagner des points de vie, mais il a plus de PV que les {@link Lapin}.
 */
public class Potiron extends Vivant {
    public static final Color COULEUR =  new Color(255, 166, 18);
    public static int PV_INITIAL=10;
    public static int CHANCE_REPRODUCTION=20;
    public Potiron() {
        super(PV_INITIAL);
    }
    public Color getCouleur() {
       return COULEUR;
    }

    @Override
    public boolean peutSeReproduire() {
        return Math.random()*100>(100-CHANCE_REPRODUCTION);
    }
}
