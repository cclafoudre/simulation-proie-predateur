import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Random;

public class Plateau extends JPanel implements ActionListener {
    public Vivant[][] simulation;
    public static int tailleCase = 15;
    protected int taille;

    protected int iterations = 0;
    public Path dossier;
    protected boolean capturerSimul = false;
    private Timer perfTimer = new Timer(1000, this);
    private int perfCompteur = 0;

    public Timer fps;
    public Timer simulTimer;
    Grille maGrille;

    public Plateau(int taille, Timer eventTimer) {
        super();
        this.taille = taille;
        simulTimer = eventTimer;
        genererPlateau(taille);
        genererAlea(1,1, Graph.lancer());
        perfTimer.start();
        //image = new BufferedImage((taille + 1) * tailleCase, (taille + 1) * tailleCase, BufferedImage.TYPE_USHORT_555_RGB);
        fps = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //afficherPlateau();
                repaint();
            }
        });
        fps.start();
       // maGrille = Grille.lancerGrille(simulation);
    }

    public void simuler() {
        //mouvementTest();
        new Thread(()->{
        menageDesMorts(); //supprimme les vivants qui n'ont plus de points de vie
        mangerBouger(); //action principale
        perfCompteur +=1;
        }).start();
        new Thread(()->{
            menageDesMorts(); //supprimme les vivants qui n'ont plus de points de vie
            mangerBouger(); //action principale
            perfCompteur +=1;
        }).start();
    }

    /**
     * Entrée d'une action sur les vivants
     */
    private void mangerBouger() {
        Pos initial = getRandomVivant();
        if((selectVivant(initial) instanceof Lapin)) { //le fait manger si c'est un lapin
            lapinsMangent(initial);
        }
        deplacement(initial); //il se séplace dans tous les cas
    }

    private void deplacement(Pos initial) {
        int dx= new Random().nextInt(3)-1; //on bouge d'une case dans une direction au hazard (& en diagoanel)
        int dy= new Random().nextInt(3)-1;
        Pos destination = new Pos(initial.getX()+dx, initial.getY()+dy);
        if(destination.positionValide(taille) && estVide(destination)){
            Vivant vaBouger = enleverVivant(initial);
            ajouterVivant(vaBouger, destination);
        }
    }

    private void lapinsMangent(Pos initial) {
        //Pos initial = getRandomVivant();
            Lapin predateur = (Lapin) selectVivant(initial);
            for (int j = - 1; j <  1; j++) {
                for (int i = -1; i < 1; i++) {
                    Pos dest = new Pos(predateur.getPos().getX() + i, predateur.getPos().getY() + j);
                    if (dest.positionValide(taille)) {
                        if (selectVivant(dest) instanceof Potiron) {
                            enleverVivant(dest);
                            predateur.manger();
                        }
                    }
                }
            }
        /*
        Pos destination = new Pos(initial.getX(), initial.getY());
        if(destination.positionValide(taille)){
            Vivant vaBouger = enleverVivant(initial);
            if(estVide(destination))
                ajouterVivant(vaBouger, destination);
            if(selectVivant(destination) instanceof Potiron && vaBouger instanceof Lapin) {
                Lapin vaManger = (Lapin) vaBouger;
                vaManger.manger();
                enleverVivant(destination); //on enlève la proie mangée
                ajouterVivant(vaManger, destination); //on déplace le mangeur
            }
        }*/
    }

    public void ajouterVivant(Vivant nouveau, Pos pos) {
        Pos.setTab(simulation, pos, nouveau);
        nouveau.setPos(pos);
    }

    public Vivant enleverVivant(Vivant aSuppr) {
        Pos.delTab(simulation, aSuppr.getPos());
        return aSuppr;
    }

    public Vivant enleverVivant(Pos pos) {
        Vivant suppr = selectVivant(pos);
        Pos.delTab(simulation, pos);
        return suppr;
    }

    public Vivant selectVivant(Pos pos) {
        return (Vivant) Pos.getTab(simulation, pos);
    }

    public boolean estVide(Pos pos){
        return Pos.getTab(simulation, pos) == null;
    }

    public void genererPlateau(int taille) {
        simulation = new Vivant[taille][taille];
    }

    public void paint(Graphics g) {
        if(iterations==0){
            g.setColor(Color.black);
            g.fillRect(0,0,getWidth(), getHeight());
        }
        for (int y = 0; y < taille + 1; y++) {
            for (int x = 0; x < taille + 1; x++) { //itère le tableau "simulation" dans ses 2 dimensiosn
                if (x < taille && y < taille) {
                    Vivant ici = simulation[y][x];
                    if (ici == null)
                        g.setColor(Vivant.COULEUR);
                    if (ici instanceof Lapin)
                        g.setColor(Lapin.COULEUR);
                    if(ici instanceof Potiron)
                        g.setColor(Potiron.COULEUR);
                    g.fillRect(((getWidth() - tailleCase*taille) / 2)+tailleCase * (x),
                            ((getHeight() - tailleCase*taille) / 2)+tailleCase * (y),
                            tailleCase-2,
                            tailleCase-2);
                    //ça effectue un produit en croix pour dessiner chaque case comme plusieurs pixels
                }
            }
        }
    }

    public void zoom(int facteur) {
        tailleCase = facteur;
    }

    private void mouvementTest() { //fait un mouvement aléatoire
        Pos pos = Pos.getRandomPos(taille);
        while (Pos.getTab(simulation, pos) == null) { //on ne choisit pas une case vide
            pos = Pos.getRandomPos(taille);
        }
        Vivant ilVaBouger = (Vivant) Pos.getTab(simulation, pos);
        //Pos.delTab(simulation, pos);
        enleverVivant(pos);

        Pos newPos = Pos.getRandomPos(taille);
        while (Pos.getTab(simulation, newPos) != null) { //on ne choisit pas une case vide
            newPos = Pos.getRandomPos(taille);
        }
        //Pos.setTab(simulation, newPos, ilVaBouger);
        ajouterVivant(ilVaBouger, newPos);
    }

    public void genererAlea(int nbrePotiron, int nbreLapins, Graph graph) {
        final int[] nP = {0};
        final int[] nL = {0};
        Thread safe = new Thread(() -> {
            try {
                Thread.sleep(2000); //coupe l'action si elle dure plus de 2s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            nP[0] = nbrePotiron;
            nL[0] = nbreLapins;
            System.out.println("Génération arrêtée");
        });
        safe.start();
        while (nP[0] < nbrePotiron) {
            Pos pos = Pos.getRandomPos(taille);
            if (Pos.getTab(simulation, pos) == null) {
                ajouterVivant(new Potiron(), pos);
                graph.addPotirons(getNbrePotirons());
                nP[0] += 1;
            }
        }
        while (nL[0] < nbreLapins) {
            Pos pos = Pos.getRandomPos(taille);
            if (Pos.getTab(simulation, pos) == null) {
                ajouterVivant(new Lapin(), pos);
                graph.addLapins(getNbreLapins());
                nL[0] += 1;
            }
        }
        safe.stop();
        //System.out.println("Génération de vivants terminée");
    }

    public void supprAlea(int nombre, Graph graph) {
        final int[] n = {0};
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            n[0] = 0;
            System.out.println("Génération arrêtée");
        }).start();
        while (n[0] < nombre) {
            Pos pos = Pos.getRandomPos(taille);
            if (Pos.getTab(simulation, pos) != null) {
                enleverVivant(pos);
                graph.addLapins(getNbreLapins());
                graph.addPotirons(getNbrePotirons());
                n[0] += 1;
            }
        }
    }

    public int getNbrePotirons() {
        int n = 0;
        for (int y = 0; y < simulation.length; y++) {
            for (int x = 0; x < simulation[y].length; x++) {
                if (simulation[y][x] instanceof Potiron)
                    n++;
            }
        }
        return n;
    }
    public int getNbreLapins() {
        int n = 0;
        for (int y = 0; y < simulation.length; y++) {
            for (int x = 0; x < simulation[y].length; x++) {
                if (simulation[y][x] instanceof Lapin)
                    n++;
            }
        }
        return n;
    }

    public void startCapture() {
        capturerSimul = true;
        if (iterations == 0) { //si on reprend la capture, pas besoin de recréer le dossier
            try {
                dossier = Files.createDirectories(Paths.get("simul-" + new Date().toString())); //crée
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopCapture() {
        capturerSimul = false;
    }

    public void toggleCapture() {
        if (capturerSimul)
            stopCapture();
        else
            startCapture();
    }

    public void lancerFfmpeg() {
        System.out.println("ffmpeg -r 60 -f image2 -s 500x500 -i \"" + dossier.toString() + "\"/%09d.png -vcodec libx264 -crf 25  -pix_fmt yuv420p \"" + dossier.toString() + "\".mp4");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(perfCompteur +" itérations/seconde");
        perfCompteur =0;
    }

    public Pos getRandomVivant() {
        Pos pos = Pos.getRandomPos(taille);
        while (Pos.getTab(simulation, pos) == null) {
            pos = Pos.getRandomPos(taille);
        }
        return pos;
    }

    public Pos getRandomVide(){
        Pos pos = Pos.getRandomPos(taille);
        while (Pos.getTab(simulation, pos) != null) {
            pos = Pos.getRandomPos(taille);
        }
        return pos;
    }

    public void menageDesMorts() {
        int nbVivants=0;
        for (int y = 0; y < taille; y++) { //parcourt tout le tableau pour supprimmer
            for (int x = 0; x < taille; x++) { // les vivants qui n'ont plus de PV
                if(simulation[y][x]!=null) {
                    if (!simulation[y][x].visible())
                        simulation[y][x] = null;
                    else
                        nbVivants+=1;
                }
            }
        }
        if(nbVivants == 0) {
            System.out.println("Arrêt de la simulation");
            simulTimer.stop(); //ça marche pas en fait
        }
    }

    public void setMortDelay(int ms){ //itère tout le tableau des vivants pour leur changer leur délai de vérification de mort
        for (int y = 0; y < taille; y++) {
            for (int x = 0; x < taille; x++) {
                if(simulation[y][x]!=null)
                    simulation[y][x].setDelay(ms);
            }
        }
    }
}
