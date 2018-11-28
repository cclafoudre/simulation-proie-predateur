import javax.swing.*;
import java.awt.*;

public class Grille  extends Canvas {
    GraphicsConfiguration gc;
    Rectangle bords;
    int taille = 45;
    int tailleCase = 15;
    Vivant[][] simulation;

    public Grille(GraphicsConfiguration gc, Vivant[][] simulation) {
        super(gc);
        this.gc = gc;
        bords = gc.getBounds();
        this.simulation = simulation;
    }

    public void paint(Graphics g) {
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
                    g.fillRect(tailleCase * (x), tailleCase * (y), tailleCase-1, tailleCase-1);
                    //ça effectue un produit en croix pour dessiner chaque case comme plusieurs pixels
                }
            }
        }
    }

    public static void main(String[] args) {
    }
    public void lancerGrille(Vivant[][] simulation) {
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        for (int j = 0; j < gs.length; j++) {
            System.out.println(j);
            GraphicsDevice gd = gs[j];
            GraphicsConfiguration[] gc =
                    gd.getConfigurations();
            for (int i = 0; i < gc.length; i++) {
                System.out.println(i);
                JFrame f =
                        new JFrame(gs[j].getDefaultConfiguration());
                Grille c = new Grille(gc[i], new Vivant[45][45]);
                f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                f.setLocationRelativeTo(null);
                Rectangle gcBounds = gc[i].getBounds();
                int xoffs = gcBounds.x;
                int yoffs = gcBounds.y;
                f.getContentPane().add(c);
                f.setTitle("Screen# " + Integer.toString(j) + ", GC# " + Integer.toString(i));
                f.setSize(45*16, 45*16);
                f.setLocation((i * 50) + xoffs, (i * 60) + yoffs);
                f.setVisible(true);
            }
        }
    }
}
