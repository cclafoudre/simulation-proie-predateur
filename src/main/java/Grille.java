import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Grille  extends Canvas {
    GraphicsConfiguration gc;
    Rectangle bords;
    Image buffer;
    Image buffer2;
    int taille = 45;
    int tailleCase = 10;
    Vivant[][] simulation;
    Timer fps;

    boolean flip;

    public Grille(GraphicsConfiguration gc, Vivant[][] simulation) {
        super(gc);
        this.gc = gc;
        bords = gc.getBounds();
        this.simulation = simulation;
        fps = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        //fps.start();
    }

    public void paint(Graphics g) {
        if(buffer==null){
            buffer = createImage(getSize().width, getSize().height);
        }
        if(buffer2==null){
            buffer2 = createImage(getSize().width, getSize().height);
        }
        renderImage(buffer.getGraphics());
        g.drawImage(buffer2, 0, 0,null);
        renderImage(buffer2.getGraphics());
        buffer2 = buffer;
    }

    public void renderImage(Graphics g) {
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
        lancerGrille(new Vivant[45][45]);
    }
    public static Grille lancerGrille(Vivant[][] simulation) {
        Grille derniere = null;
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
                derniere = new Grille(gc[i],simulation);
                f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                f.setLocationRelativeTo(null);
                Rectangle gcBounds = gc[i].getBounds();
                int xoffs = gcBounds.x;
                int yoffs = gcBounds.y;
                f.getContentPane().add(derniere);
                f.setTitle("Screen# " + Integer.toString(j) + ", GC# " + Integer.toString(i));
                f.setSize(45*16, 45*16);
                f.setLocation((i * 50) + xoffs, (i * 60) + yoffs);
                f.setVisible(true);
            }
        }
        return derniere;
    }
}
