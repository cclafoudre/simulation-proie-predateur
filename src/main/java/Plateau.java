import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class Plateau extends JPanel implements ActionListener {
    protected BufferedImage image;
    public Vivant[][] simulation;
    public static int tailleCase = 15;
    protected int taille;

    protected int iterations = 0;
    public Path dossier;
    protected boolean capturerSimul = false;
    private Timer perfTimer = new Timer(1000, this);
    private int perCompteur = 0;

    public Timer fps;

    public Plateau(int taille) {
        super();
        this.taille = taille;
        genererPlateau(taille);
        perfTimer.start();
        image = new BufferedImage((taille + 1) * tailleCase, (taille + 1) * tailleCase, BufferedImage.TYPE_USHORT_555_RGB);
        fps = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherPlateau();
            }
        });
        fps.start();
    }

    public void simuler() {
        mouvementTest();

        /*new Thread(this::mouvementTest).start();
        new Thread(this::mouvementTest).start();
        new Thread(()->{
            mouvementTest();
        }).start();*/
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

    public void genererPlateau(int taille) {
        simulation = new Vivant[taille][taille];
        simulation[0][0] = new Lapin();
        simulation[2][0] = new Potiron();
        simulation[2][1] = new Lapin();
        simulation[5][5] = new Lapin();
        simulation[taille - 2][taille - 2] = new Potiron();
        simulation[taille - 5][taille - 2] = new Potiron();
        simulation[taille - 2][taille - 5] = new Potiron();
        simulation[taille - 1][taille - 1] = new Potiron();
        simulation[taille - 1][taille - 2] = new Lapin();
        simulation[taille - 2][taille - 1] = new Lapin();
    }

    public void afficherPlateau() {
        for (int y = 0; y < taille + 1; y++) {
            for (int x = 0; x < taille + 1; x++) {
                if (x < taille && y < taille)
                    afficherCase(simulation[y][x], tailleCase * (x + 1), tailleCase * (y + 1));
            }
        }
        repaint();
        if (!capturerSimul) //si on ne demande pas de capturer, les images, le code s'arrête ici
            return;
        iterations += 1;
        try {
            String chemin = dossier.toString() + File.separator + String.format("%09d", iterations) + ".png"; //sauvegarde les fichiers avec 9 zéros
            ImageIO.write(image, "png", new File(chemin));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void afficherCase(Vivant vivant, int x, int y) {
        for (int j = -tailleCase / 2 + 1; j < tailleCase / 2 - 1; j++) {
            for (int i = -tailleCase / 2 + 1; i < tailleCase / 2 - 1; i++) {
                int X = x + i;
                int Y = y + j;
                try {
                    if (vivant != null && vivant.visible())
                        image.setRGB(X, Y, vivant.getCouleur().getRGB());
                    else
                        image.setRGB(X, Y, Vivant.COULEUR.getRGB());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("x=" + x + " y=" + y);
                    System.out.println("x+i=" + x + i + " y+j=" + y + j);
                    e.printStackTrace();
                } catch (NullPointerException e) { //il n'y a rien dans cette case
                    System.out.println("Il n'y a rien dans cette case !");
                    image.setRGB(X, Y, Vivant.COULEUR.getRGB());
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image,
                    (getWidth() - image.getWidth()) / 2,
                    (getHeight() - image.getHeight()) / 2,
                    null);
        }
    }

    public void zoom(int facteur) {
        fps.stop();
        tailleCase = facteur;
        image = new BufferedImage((taille + 1) * tailleCase, (taille + 1) * tailleCase, BufferedImage.TYPE_USHORT_555_RGB);
        fps.start();
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
        perCompteur+=1;
    }

    public void genererAlea(int nbrePotiron, int nbreLapins, Graph graph) {
        final int[] nP = {0};
        final int[] nL = {0};
        Thread safe = new Thread(() -> {
            try {
                Thread.sleep(2000);
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
                //graph.addLapins(getNbreLapins());
                graph.addPotirons(getNbrePotirons());
                nP[0] += 1;
            }
        }
        while (nL[0] < nbreLapins) {
            Pos pos = Pos.getRandomPos(taille);
            if (Pos.getTab(simulation, pos) == null) {
                ajouterVivant(new Lapin(), pos);
                graph.addLapins(getNbreLapins());
                //graph.addPotirons(getNbrePotirons());
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
                dossier = Files.createDirectories(Paths.get("simul-" + new Date().toString()));
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
        System.out.println(perCompteur+" itérations/seconde");
        perCompteur=0;
    }
}
