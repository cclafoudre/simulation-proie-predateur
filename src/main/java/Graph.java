import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Graph extends JPanel implements ActionListener {
    protected BufferedImage image;
    //public int[] serieDonnee;
    public int[] serieLapins;
    public int[] seriePotirons;
    public int zoomX=1;

    private int longeur;
    private int indexLapins=0;
    private int indexPotirons=0;

    public Graph(int timeWindow) {
        super();
        longeur = timeWindow;
        //serieDonnee = new int[longeur];
        serieLapins = new int[longeur];
        seriePotirons = new int[longeur];
        image = new BufferedImage(timeWindow*zoomX, 700,BufferedImage.TYPE_USHORT_555_RGB);
        new Timer(50, this).start();
    }

    public void addLapins(int valeur) {
        new Thread(()->{
            if(indexLapins<longeur-1){
                serieLapins[indexLapins] = valeur;
                try {
                     tracerPoint(indexLapins, serieLapins, Lapin.COULEUR);
                    effacerPoint(indexLapins+1, serieLapins);
                    effacerPoint(indexLapins+2, serieLapins);
                    effacerPoint(indexLapins+3, serieLapins);
                    effacerPoint(indexLapins+4, serieLapins);
                    effacerPoint(indexLapins+5, serieLapins);
                    indexLapins++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    //System.out.println("lap(" + indexLapins + ")=" + serieLapins[indexLapins]);
                }
            }
            else {
                decaler(1);
                addLapins(valeur);
            }
        }).start();
    }
    public void addPotirons(int valeur) {
        new Thread(()->{
            if(indexPotirons<longeur-1){
                seriePotirons[indexPotirons] = valeur;
                try {
                    tracerPoint(indexPotirons, seriePotirons, Potiron.COULEUR);
                    effacerPoint(indexPotirons+1, seriePotirons);
                    effacerPoint(indexPotirons+2, seriePotirons);
                    effacerPoint(indexPotirons+3, seriePotirons);
                    effacerPoint(indexPotirons+4, seriePotirons);
                    effacerPoint(indexPotirons+5, seriePotirons);
                    indexPotirons++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    //System.out.println("pot(" + indexPotirons + ")=" + seriePotirons[indexPotirons]);
                }
            }
            else {
                decaler(1);
                addPotirons(valeur);
            }
        }).start();
    }

    private void tracerPoint(int index, int[] serieDonnee, Color couleur) {
        image.setRGB(index * zoomX, image.getHeight()-1 - serieDonnee[index], couleur.getRGB());
    }
    private void effacerPoint(int x, int[] serieDonnee) {try {
        image.setRGB(x * zoomX, image.getHeight() - 1 - serieDonnee[x], new Color(0, 0, 0).getRGB());
    }catch (ArrayIndexOutOfBoundsException e){}
    }

    public void clean() {
        image.flush();
        image=null;
        image = new BufferedImage(longeur*zoomX, 700,BufferedImage.TYPE_USHORT_555_RGB);
        indexLapins=0;
        indexPotirons=0;
    }

    public void decaler(int n) {
        /*int[] nouveau = new int[longeur];
        for (int i = nouveau.length - 1; i > n; i -= 1) {
            nouveau[i - n] = serieDonnee[i];
        }
        index = longeur - 1 - n;
        serieDonnee = nouveau;
        nouveau = null;*/
        //index=0; //en fait tracer juste avant le précédent ça va ...
        indexLapins=0;
        indexPotirons=0;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image != null)
        {
            g.drawImage(image,
                    (getWidth()-image.getWidth())/2,
                    (getHeight()-image.getHeight())/2,
                    null);
        }
    }
    public void resize(int largeur, int hauteur){
        longeur = largeur;
        image = new BufferedImage(longeur*zoomX, hauteur,BufferedImage.TYPE_INT_RGB);
    }

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
}
