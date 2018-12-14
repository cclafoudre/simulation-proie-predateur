import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

public class Graphique extends JPanel {
    private Timer topAffich;

    public ArrayList<Integer> lapins;
    public ArrayList<Integer> potirons;
    protected int maxLapins; //défini à l'entrée
    protected int maxPotirons;
    private int[] tabLap;
    private int[] tabPot;

    public Graphique(){
        lapins = new ArrayList<>();
        potirons = new ArrayList<>();
        topAffich =  new Timer(100, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();//exécute "paint();"
            }
        });
    }

    public void addLapinsN(int n){
        if(n>maxLapins)
            maxLapins=n;
        lapins.add(n);
    }
    public void addPotironsN(int n) {
        if (n > maxPotirons)
            maxPotirons=n;
        potirons.add(maxPotirons);
    }

    /**
     * y=(hauteur-y), x=x
     * @param g
     */
    public void paint(Graphics g){
        convertArrays();
        g.setColor(Color.cyan);
        g.drawPolyline(tabPot, tabLap, tabLap.length);
    }
    private void convertArrays() {
        lapins.trimToSize();
        potirons.trimToSize();
        int maxLP = Math.min(lapins.size(), potirons.size()); //les deux listes doivent avoir la meme taille
        Integer[] arrayLapins = new Integer[maxLP];
        Integer[] arrayPotirons = new Integer[maxLP];
        lapins.subList(0, maxLP).toArray(arrayLapins); //on convertit les ArrayList en Array
        potirons.subList(0, maxLP).toArray(arrayPotirons);
        tabLap = new int[maxLP]; //on réinitialise les tableaux
        tabPot = new int[maxLP];

        for (int y = 0; y < arrayLapins.length; y++) { //on convertit les positions X (pot) et Y (lap) dans un tableau
            tabLap[y] = convertLap(arrayLapins[y]);
        }
        for (int x = 0; x < arrayPotirons.length; x++) {
            tabPot[x] = convertPot(arrayPotirons[x]);
        }
    }

    private int convertPot(int x){
        return x*(getWidth()/maxPotirons);
    }
    private int convertLap(int y){
        return getHeight()-y*(getHeight()/maxLapins);
    }

}
class GDisp extends JFrame {
    public GDisp(Graphique graphique){
        setContentPane(graphique);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(500,500);
        setTitle("Graphique des lapins en fonction des potirons");
        setBackground(Color.black);
    }

    public static void main(String[] args){
        Graphique g = new Graphique();
        for (int i = 0; i < 100; i++) {
            g.addPotironsN(i);
            g.addLapinsN(20+(int)(2*Math.sin(0.1*i)));
        }
        g.addLapinsN(10);
        g.addPotironsN(3);
        g.addLapinsN(100);
        g.addPotironsN(30);
        g.addLapinsN(15);
        g.addPotironsN(90);
        new GDisp(g);
        g.repaint();
    }
}
