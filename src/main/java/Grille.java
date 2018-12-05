import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Grille  extends JPanel implements Runnable{
    int taille = 45;
    int tailleCase = 10;
    Vivant[][] simulation;
    Timer fps;

    public Grille(Vivant[][] simulation) {
        this.simulation = simulation;
        fps = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                run();
            }
        });
        fps.start();
    }
    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0,0,getWidth(), getHeight());
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

    @Override
    public void run() {
        repaint();
    }

    public void zoom(int facteur) {
        tailleCase=facteur;
    }
}
