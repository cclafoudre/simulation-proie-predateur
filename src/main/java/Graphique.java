import org.omg.PortableServer.THREAD_POLICY_ID;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Attention, il ne faut pas qu'il y ait de point en dehors du JPanel, sinon drawPolyLine() ne trace rien !
 * les nombres sont faux pour la courbe blanche et orange, car elles ne sont pas dessinées avec le même maximum que la courbe bleue
 * -> à changer
 */
public class Graphique extends JPanel {
    public ArrayList<Integer> lapins;
    public ArrayList<Integer> potirons;
    protected int maxLapins; //défini à l'entrée
    protected int maxPotirons;
    protected int maxGraph; //max pour afficher les deux courbes de population
    private int[] tabLap;
    private int[] tabPot;

    private int[] xP;
    private int[] potironsY;
    private int[] xL;
    private int[] lapinsY;

    private int height;
    private int width;
    public Graphique(){
        lapins = new ArrayList<>();
        potirons = new ArrayList<>();
        JFrame f = new JFrame("Graphique des lapins en fonction des potirons");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //f.setLocation(-700,1000);
        f.setLocationRelativeTo(null);
        f.setSize(500,500);
        f.setBackground(Color.black);
        f.add(this);

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
        f.setVisible(true);
    }

    public void addLapins(int n){
        lapins.add(Math.abs(n));
        if(n>maxLapins) {
            maxLapins=n;
            if(n>maxGraph)
                maxGraph=n;
        }
    }
    public void addPotirons(int n) {
        potirons.add(Math.abs(n));
        if (n > maxPotirons) {
            maxPotirons=n;
            if(n>maxGraph)
                maxGraph=n;
        }
    }

    /**
     * y=(hauteur-y), x=x
     */
    public void paint(Graphics g0){
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        height=getHeight();
        width=getWidth();
        convertArrays();

        g.setColor(Color.BLACK);
        g.fillRect(0,0,width, height);

        g.setColor(Lapin.COULEUR);
        g.drawPolyline(xL, lapinsY, xL.length);
        g.setColor(Potiron.COULEUR);
        g.drawPolyline(xP, potironsY, xP.length);

        g.setColor(Color.cyan);
        g.drawPolyline(tabPot, tabLap, tabLap.length);

        g.setColor(Color.red);
        g.fillRect(tabPot[tabPot.length-1], tabLap[tabLap.length-1], 8,8);

        g.setColor(Color.green);
        g.drawLine(2,0,2,height);
        g.drawLine(0,height-2,width,height-2);

        drawAxes(g,height,width,2,2,9);
    }
    public void setSize(int x, int y){
        super.setSize(x,y);
        height=getHeight();
        width=getWidth();
    }
    private void convertArrays() {
        //lapins.trimToSize();
        //potirons.trimToSize();
        int maxLP = Math.min(lapins.size(), potirons.size()); //les deux listes doivent avoir la meme taille
        Integer[] arrayLapins = new Integer[maxLP];
        Integer[] arrayPotirons = new Integer[maxLP];
        lapins.subList(0, maxLP).toArray(arrayLapins); //on convertit les ArrayList en Array
        potirons.subList(0, maxLP).toArray(arrayPotirons);
        tabLap = new int[maxLP]; //on réinitialise les tableaux
        tabPot = new int[maxLP];
        xL=new int[maxLP];
        xP=new int[maxLP];
        potironsY=new int[maxLP];
        lapinsY=new int[maxLP];

        for (int y = 0; y < arrayLapins.length; y++) { //on convertit les positions X (pot) et Y (lap) dans un tableau
            /*tabLap[y] = convertLap(arrayLapins[y]);
            xL[y]=y*width/(maxLP-1);
            lapinsY[y]=height-arrayLapins[y]*(height/maxGraph);*/
            tabLap[y]=height-map(arrayLapins[y],0,maxLapins,5,height-5);
            xL[y]=map(y,0,maxLP,5,width-5);
            lapinsY[y]=height-map(arrayLapins[y],0,maxGraph,5,height-5);
        }
        for (int x = 0; x < arrayPotirons.length; x++) {
            tabPot[x] = width-map(arrayPotirons[x],0,maxPotirons,5,width-5);
            potironsY[x]=height-map(arrayPotirons[x],0,maxGraph,5,height-5);
            xP[x]=map(x,0,maxLP,5,width-5);
/*
            tabPot[x] = convertPot(arrayPotirons[x]);
            potironsY[x]=height-arrayPotirons[x]*(height/maxGraph);
            xP[x]=x*width/(maxLP-1);*/
        }
    }
    public int map(int x, int in_min, int in_max, int out_min, int out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    private int convertPot(int x){ //produit en croix pour maximiser l'affichage en X
        return x*width/maxPotirons;
    }
    private int convertLap(int y){ //prdui en coroix avec 0 en bas de la fenetre
        return height-y*(height/maxLapins);
    }

    public static void main(String[] args) throws InterruptedException {
        Graphique g = new Graphique();
        for (int i = 0; i < 1000; i++) {
            g.addPotirons(30+(int)(20*Math.sin(0.1*i)+500*Math.sin(0.001*i)));
            g.addLapins(20+(int)(20*Math.cos(0.1*i)+500*Math.sin(0.01*i)));
            g.repaint();
            System.out.println(g.lapins.size()+" "+g.potirons.size());
            Thread.sleep(50);
        }
    }

    private void drawAxes(Graphics2D g, int height, int width, int x0, int y0, int resolution){
        g.setColor(Color.white);
        g.drawString("0",x0+3,height-4);
        for (int n = 1; n <= resolution; n++) {
            int x=map(n,0,resolution,x0,width);
            int y = map(n,0,resolution,y0,height);
            g.setColor(Color.green);
            g.fillRect(x,height-9,2,10); //on trace les traits sur les abscisses
            g.fillRect(x0-3,height-y,10,3); //ordonnées

            g.setColor(Color.white);
            g.drawString(n*maxPotirons/resolution+"",x+4,height-4); //on affiche les numéros des abscicsses
            g.drawString(n*maxLapins/resolution+"",x0+2,height-y-4); //idem pour odrinneée
        }
    }

}