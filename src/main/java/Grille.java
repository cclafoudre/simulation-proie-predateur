import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;

public class Grille  extends Canvas implements Runnable{
    GraphicsConfiguration gc;
    Rectangle bords;
    int taille = 45;
    int tailleCase = 10;
    Vivant[][] simulation;
    Timer fps;

    BufferStrategy doubleBuffering;
    AffineTransform monZoom;

    boolean flip;

    public Grille(GraphicsConfiguration gc, Vivant[][] simulation) {
        super(gc);
        this.gc = gc;
        bords = gc.getBounds();
        this.simulation = simulation;
        fps = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                run();
                //repaint();
            }
        });
        fps.start();
    }

    public void paint(Graphics g) {
        renderImage(g);
    }

    public void displayImage() {
        if(doubleBuffering==null){
            this.createBufferStrategy(1);
            doubleBuffering = getBufferStrategy();
            //prépare une transormation 2x
            monZoom = ((Graphics2D)doubleBuffering.getDrawGraphics()).getTransform();
            monZoom.setToScale(3,3);
        }
        do {
            // The following loop ensures that the contents of the drawing buffer
            // are consistent in case the underlying surface was recreated
            do {
                // Get a new graphics context every time through the loop
                // to make sure the strategy is validated
                Graphics g = doubleBuffering.getDrawGraphics();
                // Render to graphics

                Graphics2D g2d = (Graphics2D) g;
                g2d.setTransform(monZoom);
                renderImage(g2d);

                // Dispose the graphics
                g.dispose();
                g2d.dispose();

                // Repeat the rendering if the drawing buffer contents
                // were restored
            } while (doubleBuffering.contentsRestored());

            // Display the buffer
            //doubleBuffering.show();

            // Repeat the rendering if the drawing buffer was lost
        } while (doubleBuffering.contentsLost());
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

    @Override
    public void run() {
        displayImage();
    }
}
