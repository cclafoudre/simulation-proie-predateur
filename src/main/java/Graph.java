import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

public class Graph extends JPanel {
    protected BufferedImage image;
    public int[] serieDonnee;
    public int zoomX=1;

    private int longeur;
    private int index=0;

    public Graph(int timeWindow) {
        super();
        longeur = timeWindow;
        serieDonnee = new int[longeur];
        //serieDonnee = new int[getWidth()];
        //image = new BufferedImage(timeWindow*zoomX, getHeight(),BufferedImage.TYPE_INT_RGB);
        image = new BufferedImage(timeWindow*zoomX, 700,BufferedImage.TYPE_INT_RGB);
        new Thread(()->{
            while (1>0){
                repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void addData(int valeur) {
        new Thread(()->{
        if(index<longeur-1){
            serieDonnee[index] = valeur;
            index++;
            tracer();
        }
        else {
            decaler(1);
            addData(valeur);
        }
        }).start();
    }

    public void decaler(int n){
        int[] nouveau = new int[longeur];
        for (int i = nouveau.length-1; i >n; i-=1) {
            nouveau[i-n]=serieDonnee[i];
        }
        index=longeur-1-n;
        serieDonnee = nouveau;
        nouveau = null;
        /*for (int i = 0; i < longeur; i++) {
            for(int y=0;y<image.getHeight()-1;y++){
                image.setRGB(i*zoomX,y,Color.BLACK.getRGB());
            }
        }*/
    }

    public void tracer(){
        for (int i = 0; i < serieDonnee.length; i++) {
            if(serieDonnee[i]==0)
                continue;
            for(int y=0;y<image.getHeight()-1;y++){
                image.setRGB(i*zoomX,y,Color.BLACK.getRGB()); // on efface l'ordonnée précédente
            }
            try{image.setRGB(i*zoomX,image.getHeight()-serieDonnee[i], new Color(26, 255, 47).getRGB());}
            catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("f(" + i + ")=" + serieDonnee[i]);
            }
        }
        //repaint();
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
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return g;
    }

    public static void main(String[] args){
        Graph g = lancer();
        for(int i=0; i<200; i++) {
            int finalI = i;
            new Thread(()->{
                double y = 60*Math.sin(0.05*finalI);
                g.addData((int)y);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
