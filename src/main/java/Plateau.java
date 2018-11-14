import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Plateau extends JPanel {
    protected BufferedImage image;
    public Vivant[][] simulation;
    public static int tailleCase = 15;
    protected int taille;

    public Plateau(int taille) {
        super();
        this.taille = taille;
        genererPlateau(taille);
        image = new BufferedImage((taille+1)*tailleCase,(taille+1)*tailleCase,BufferedImage.TYPE_INT_RGB);
    }

    public void simuler(){
        mouvementTest();
        afficherPlateau();
    }

    public void ajouterVivant(Vivant nouveau, Pos pos){
        Pos.setTab(simulation, pos, nouveau);
        nouveau.setPos(pos);
    }
    public Vivant enleverVivant(Vivant aSuppr){
        Pos.delTab(simulation, aSuppr.getPos());
        return aSuppr;
    }
    public Vivant enleverVivant(Pos pos){
        Vivant suppr = selectVivant(pos);
        Pos.delTab(simulation, pos);
        return suppr;
    }
    public Vivant selectVivant(Pos pos){
        return  (Vivant) Pos.getTab(simulation, pos);
    }

    public void genererPlateau(int taille) {
        simulation = new Vivant[taille][taille];
        simulation[0][0] = new Lapin();
        simulation[2][0] = new Potiron();
        simulation[2][1] = new Lapin();
        simulation[5][5] = new Lapin();
        simulation[taille-2][taille-2] = new Potiron();
        simulation[taille-5][taille-2] = new Potiron();
        simulation[taille-2][taille-5] = new Potiron();
        simulation[taille-1][taille-1] = new Potiron();
        simulation[taille-1][taille-2] = new Lapin();
        simulation[taille-2][taille-1] = new Lapin();
    }

    public void afficherPlateau(){
        for(int y=0;y<taille+1; y++) {
            for(int x=0; x<taille+1; x++) {
                if(x<taille && y<taille)
                    afficherCase(simulation[y][x], tailleCase*(x+1), tailleCase*(y+1));
            }
        }
        repaint();
    }
    private void afficherCase(Vivant vivant, int x, int y) {
        for (int j = -tailleCase/2+1; j < tailleCase/2-1; j++) {
            for (int i = -tailleCase/2+1; i < tailleCase/2-1; i++) {
                    int X=x + i;
                    int Y=y+j;
                try {
                    if(vivant != null && vivant.visible())
                        image.setRGB(X,Y, vivant.getCouleur().getRGB());
                    else
                        image.setRGB(X,Y, Vivant.COULEUR.getRGB());
                }catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("x="+x+" y="+y);
                    System.out.println("x+i="+x+i+" y+j="+y+j);
                    e.printStackTrace();
                } catch (NullPointerException e){ //il n'y a rien dans cette case
                    System.out.println("Il n'y a rien dans cette case !");
                    image.setRGB(X,Y, Vivant.COULEUR.getRGB());
                }
            }
        }
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
    public void zoom(int facteur) {
        tailleCase = facteur;
        image = new BufferedImage((taille+1)*tailleCase,(taille+1)*tailleCase,BufferedImage.TYPE_INT_RGB);
        afficherPlateau();
    }

    private void mouvementTest() { //fait un mouvement aléatoire
        Pos pos = Pos.getRandomPos(taille);
        while(Pos.getTab(simulation, pos)==null) { //on ne choisit pas une case vide
            pos = Pos.getRandomPos(taille);
        }
        Vivant ilVaBouger = (Vivant) Pos.getTab(simulation, pos);
        //Pos.delTab(simulation, pos);
        enleverVivant(pos);

        Pos newPos = Pos.getRandomPos(taille);
        while(Pos.getTab(simulation, newPos)!=null) { //on ne choisit pas une case vide
            newPos = Pos.getRandomPos(taille);
        }
        //Pos.setTab(simulation, newPos, ilVaBouger);
        ajouterVivant(ilVaBouger, newPos);
    }

    public void genererAlea(int nbrePotiron, int nbreLapins, Graph graph) {
        final int[] nP = {0};
        final int[] nL = {0};
        Thread safe = new Thread(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            nP[0] =nbrePotiron;
            nL[0] =nbreLapins;
            System.out.println("Génération arrêtée");
        });
        safe.start();
        while (nP[0] <nbrePotiron){
            Pos pos = Pos.getRandomPos(taille);
            if(Pos.getTab(simulation, pos)==null) {
                ajouterVivant(new Potiron(), pos);
                graph.addData(getNbreVivants());
                nP[0] +=1;
            }
        }
        while (nL[0] <nbreLapins){
            Pos pos = Pos.getRandomPos(taille);
            if(Pos.getTab(simulation, pos)==null) {
                ajouterVivant(new Lapin(), pos);
                graph.addData(getNbreVivants());
                nL[0] += 1;
            }
        }
        safe.stop();
        //System.out.println("Génération de vivants terminée");
    }
    public void supprAlea(int nombre, Graph graph) {
        final int[] n = {0};
        new Thread(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            n[0] =0;
            System.out.println("Génération arrêtée");
        }).start();
        while(n[0] <nombre) {
            Pos pos = Pos.getRandomPos(taille);
            if(Pos.getTab(simulation, pos)!=null) {
                enleverVivant(pos);
                graph.addData(getNbreVivants());
                n[0]+=1;
            }
        }
    }

    public int getNbreVivants(){
        int n=0;
        for (int y = 0; y < simulation.length; y++) {
            for (int x = 0; x < simulation[y].length; x++) {
                if(simulation[y][x]!=null)
                    n++;
            }
        }
        return n;
    }
}
