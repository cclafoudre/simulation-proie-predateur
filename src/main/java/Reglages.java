import javax.swing.*;
import java.awt.*;

public class Reglages extends JPanel {
    private ChampReglage vivantEsperanceVie;
    private ChampReglage chanceReprodPotiron;
    private ChampReglage pvLapin;
    private ChampReglage pvPotiron;

    public Reglages() {
        vivantEsperanceVie = new ChampReglage(Vivant.DELAI_TIMER, "esperance de vie", 100, 10000, 1500);
        chanceReprodPotiron = new ChampReglage(Potiron.CHANCE_REPRODUCTION, "% reproduction potiron", 0, 100, 20);
        pvLapin = new ChampReglage(Lapin.PV_INITIAL, "PV lapin", 1, 100, 5);
        pvPotiron = new ChampReglage(Potiron.PV_INITIAL, "PV potiron", 1, 100, 10);

        add(chanceReprodPotiron);
        add(vivantEsperanceVie);
        add(pvLapin);
        add(pvPotiron);
    }

    public static JFrame fenetre(Reglages reglages) {
        JFrame f = new JFrame("Réglages");
        f.add(reglages);
//	f.setLocationRelativeTo(null);
        f.setSize(500, 500);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        return f;
    }

    public static void main(String[] args) {
        fenetre(new Reglages()).setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
