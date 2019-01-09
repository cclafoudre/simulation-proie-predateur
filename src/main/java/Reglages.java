import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Reglages extends JPanel {
    private ChampReglage vivantEsperanceVie;
    private ChampReglage chanceReprodPotiron;
    private ChampReglage pvLapin;
    private ChampReglage pvPotiron;
    private JCheckBox repoductionACote = new JCheckBox("Naissance à coté du Vivant");
    private JButton save = new JButton("Sauvegarder les parametres");
    private JButton restore = new JButton("Restaurer les parametres");

    public Reglages() {
        vivantEsperanceVie = new ChampReglage(Vivant.DELAI_TIMER, "esperance de vie", 100, 10000, 1500);
        chanceReprodPotiron = new ChampReglage(Potiron.CHANCE_REPRODUCTION, "% reproduction potiron", 0, 100, 20);
        pvLapin = new ChampReglage(Lapin.PV_INITIAL, "PV lapin", 1, 100, 5);
        pvPotiron = new ChampReglage(Potiron.PV_INITIAL, "PV potiron", 1, 100, 10);

        chanceReprodPotiron.valSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e){
                Potiron.CHANCE_REPRODUCTION = chanceReprodPotiron.valSlider.getValue();
                chanceReprodPotiron.valField.setText(""+Potiron.CHANCE_REPRODUCTION);
            }
        });
        vivantEsperanceVie.valSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Vivant.DELAI_TIMER = vivantEsperanceVie.valSlider.getValue();
                vivantEsperanceVie.valField.setText(""+Vivant.DELAI_TIMER);
            }
        });
        pvLapin.valSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Lapin.PV_INITIAL = pvLapin.valSlider.getValue();
                pvLapin.valField.setText(""+Lapin.PV_INITIAL);
            }
        });
        pvPotiron.valSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e){
                Potiron.PV_INITIAL = pvPotiron.valSlider.getValue();
                pvPotiron.valField.setText(""+Potiron.PV_INITIAL);
            }
        });
        repoductionACote.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Plateau.REPRODUCTION_A_COTE=repoductionACote.isSelected();
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Parametres.serializer();
            }
        });
        restore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Parametres p = Parametres.deserializer();
                pvPotiron.valField.setText(""+Potiron.PV_INITIAL);
                pvPotiron.valSlider.setValue(Potiron.PV_INITIAL);
                chanceReprodPotiron.valField.setText(""+Potiron.CHANCE_REPRODUCTION);
                chanceReprodPotiron.valSlider.setValue(Potiron.CHANCE_REPRODUCTION);
                vivantEsperanceVie.valField.setText(""+Vivant.DELAI_TIMER);
                vivantEsperanceVie.valSlider.setValue(Vivant.DELAI_TIMER);
                pvLapin.valField.setText(""+Lapin.PV_INITIAL);
                pvLapin.valSlider.setValue(Lapin.PV_INITIAL);
                pvPotiron.valField.setText(""+Potiron.PV_INITIAL);
                pvPotiron.valSlider.setValue(Potiron.PV_INITIAL);
                repoductionACote.setSelected(Plateau.REPRODUCTION_A_COTE);
            }
        });

        add(chanceReprodPotiron);
        add(vivantEsperanceVie);
        add(pvLapin);
        add(pvPotiron);
        add(repoductionACote);
        add(save);
        add(restore);
    }

    public static JFrame fenetre(Reglages reglages) {
        JFrame f = new JFrame("Réglages");
        f.add(reglages);
    	f.setLocationRelativeTo(null);
        f.setSize(500, 500);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        return f;
    }

    public static void main(String[] args) {
        fenetre(new Reglages()).setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
class Parametres implements Serializable {
    private static final long serialVersionUID = 2;
    int pvPotiron;
    int vivantEsperanceVie;
    int chanceReprodPotiron;
    int pvLapin;
    boolean reprodACote;

    public Parametres saveValeurs(){
        vivantEsperanceVie = Vivant.DELAI_TIMER;
        pvPotiron = Potiron.PV_INITIAL;
        pvLapin = Lapin.PV_INITIAL;
        chanceReprodPotiron = Potiron.CHANCE_REPRODUCTION;
        reprodACote=Plateau.REPRODUCTION_A_COTE;
        return this;
    }
    public Parametres restoreValeurs() {
        Vivant.DELAI_TIMER = vivantEsperanceVie;
        Potiron.PV_INITIAL = pvPotiron;
        Lapin.PV_INITIAL = pvLapin;
        Potiron.CHANCE_REPRODUCTION=chanceReprodPotiron;
        Plateau.REPRODUCTION_A_COTE=reprodACote;
        return this;
    }
    public static void serializer() {
        try {
            final FileOutputStream fichier = new FileOutputStream("parametres.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fichier);
            oos.writeObject(new Parametres().saveValeurs());
            oos.flush();
            oos.close();
            fichier.close(); // faut le faire ?
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Parametres deserializer() {
        try {
            FileInputStream fichier = new FileInputStream("parametres.bin");
            ObjectInputStream ois = new ObjectInputStream(fichier);
            Parametres p = (Parametres) ois.readObject();
            return p.restoreValeurs();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}