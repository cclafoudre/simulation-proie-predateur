import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Fen&ecirc;tre affichant une courbe de l'&eacute;volution des potions/lapins en fonction du temps.
 */
public class Graph extends JPanel implements ActionListener { //il faudrait passer à g.drawPolyline();
    protected BufferedImage image;
    public ArrayList<Integer> serieLapins;
    public ArrayList<Integer> seriePotirons;
    public int zoomX=1;
    int indexLap=0;
    int indexPot=0;

    public Graph(int timeWindow) {
        super();
        seriePotirons= new ArrayList<>();
        serieLapins= new ArrayList<>();
        //serieDonnee = new int[longeur];
        new Timer(32, this).start();
    }

    /**
     * Rajoute une valeur de recensement des lapins dans l'historique &agrave; l'&eacute;cran.
     * @param valeur le nombre de lapins
     */
    public void addLapins(int valeur) {
        serieLapins.add(valeur);
        indexLap+=1;
        repaint();
    }
    /**
     * Rajoute une valeur de recensement des potirons dans l'historique &agrave; l'&eacute;cran.
     * @param valeur le nombre de potirons
     */
    public void addPotirons(int valeur) {
        seriePotirons.add(valeur);
        indexPot+=1;
    }

    /**
     * Remet &agrave; z&eacute;ro le graphique pour effacer les pixels erron&eacute;s qui apparaissent de temps en temps.
     */
    public void clean() {

    }

    public void paint(Graphics g){
        g.setColor(Color.black);
        g.fillRect(seriePotirons.get(indexPot-1),seriePotirons.get(indexLap-1), 2,2);
        g.setColor(Color.red);
        int[] serP = convertArray(seriePotirons);
        int[] serL = convertArray(serieLapins);
        g.drawPolyline(serP, serL, Math.min(serieLapins.size(), seriePotirons.size()));
        /*int[] a =new int[]{50,100,20,60};
        int[] b =new int[]{90,300,80,60};
        g.drawPolyline(a,b,3);*/
    }

    /**
     * M&eacute;thode appel&eacute;e lors du redimensionnement de la fen&ecirc;tre. Sert &agrave; V&eacute;rifier que le graph
     * est bien affich&eacute; au milieu.
     * @param largeur
     * @param hauteur
     */
    public void setSize(int largeur, int hauteur){
        super.setSize(largeur, hauteur);
    }

    /**
     * Cr&eacute;e une nouvelle fen&ecirc;tre avec la courbe dedans.
     * @return le graphique cr&eacute;e
     */
    public static Graph lancer() {
        JFrame window = new JFrame("Graph");
        Graph g = new Graph(900);
        window.add(g);
        window.setSize(900,750);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        //window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return g;
    }

    public static void main(String[] args){
        Graph g = lancer();
        for(int i=0; i<200; i++) {
            int finalI = i;
            new Thread(()->{
                double y = 60*Math.sin(0.05*finalI);
                g.addLapins(100+(int)y);
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    private int[] convertArray(ArrayList<Integer> arrayList){
        int[] array=new int[arrayList.size()];
        Iterator<Integer> it = arrayList.iterator();
        int index=0;
        while (it.hasNext()) {
            array[index] = it.next();
            index += 1;
        }
        return array;
    }
}
