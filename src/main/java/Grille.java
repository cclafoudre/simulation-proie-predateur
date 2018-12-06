import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Classe utilis&eacute;e pour l'affichage graphique de la simulation.
 * Pour afficher uniquement la modification sur une case, utiliser la m&eacute;thode {@link Grille#dessineCase(int, int, Vivant) dessineCase()}
 * Pour tout afficher d'un coup, appeler la m&eacute;thode {@link Grille#repaint() repaint()}
 */
public class Grille  extends JPanel implements Runnable{
    int taille = 45;
    int tailleCase = 10;
    private Vivant[][] simulation;
    private Timer fps;

    /**
     * @param simulation la r&eacute;f&eacute;rence d'un tableau de vivants
     */
    public Grille(Vivant[][] simulation) {
        this.simulation = simulation;
        this.taille = simulation.length;
        fps = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                run();
            }
        });
        //fps.start();
    }

    /**
     * Dessine enti&egrave;rement toute la grille. Appeler avec la m&eacute;thode repaint()
     */
    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0,0,getWidth(), getHeight());
        for (int y = 0; y < taille + 1; y++) {
            for (int x = 0; x < taille + 1; x++) { //itère le tableau "simulation" dans ses 2 dimensiosn
                if (x < taille && y < taille) {
                    dessineCase(x,y,simulation[y][x],g);
                }
            }
        }
    }

    /**
     * Dessine une seule case sur la grille sans se pr&eacute;occuper de rafra&icirc;chir l'affichache (c'est pour &ccedil;a que les cases nouvellement dessin&eacute;es se superposent aux menus
     * @param x coordonn&eacute;es x et y, peuvent &ecirc;tre remplac&eacute;es par un objet Pos
     * @param y coordonn&eacute;e y
     * @param ici le Vivant &agrave; afficher. peut &ecirc;tre "null" (case vide)
     * @param g Graphics sur lequel dessiner, optionnel
     */
    public void dessineCase(int x, int y, Vivant ici, Graphics g){
        if (ici == null)
            g.setColor(Vivant.COULEUR);
        else
            g.setColor(ici.getCouleur()); //utilise la couleur du vivant
        g.fillRect(((getWidth() - tailleCase*taille) / 2)+tailleCase * (x), //dessine la case relativement au milieu de l'écran
                ((getHeight() - tailleCase*taille) / 2)+tailleCase * (y),
                tailleCase-2,
                tailleCase-2);
    }

    /**
     * Dessine une seule case sur la grille sans se pr&eacute;occuper de rafra&icirc;chir l'affichache (c'est pour &ccedil;a que les cases nouvellement dessin&eacute;es se superposent aux menus
     * @param pos la position du vivant
     * @param ici le vivant &agrave; afficher
     */
    public void dessineCase(Pos pos, Vivant ici){
        dessineCase(pos.getX(), pos.getY(), ici);
    }
    /**
     * Dessine une seule case sur la grille sans se pr&eacute;occuper de rafra&icirc;chir l'affichache (c'est pour &ccedil;a que les cases nouvellement dessin&eacute;es se superposent aux menus
     * @param x coordonn&eacute;es x et y, peuvent &ecirc;tre remplac&eacute;es par un objet Pos
     * @param y coordonn&eacute;e y
     * @param ici le Vivant &agrave; afficher. peut &ecirc;tre "null" (case vide)
     */
    public void dessineCase(int x, int y, Vivant ici) {
        if(isSingleRefresh())
            return;
        dessineCase(x,y,ici,getGraphics());
    }


    @Override
    public void run() {
        repaint();
    }

    public void zoom(int facteur) {
        tailleCase=facteur;
        repaint();
    }

    public void stopRefresh() {
        this.fps.stop();
    }
    public void startRefresh() {
        this.fps.start();
    }
    public void setRefreshDelay(int ms){this.fps.setDelay(ms);}

    private boolean isSingleRefresh(){return fps.isRunning();}

    public void afficherTexte(String str){
        Graphics g = getGraphics();
        g.fillRect(0,0,50,20);
        g.setColor(Color.red);
        g.drawString(str,5,10);
    }
}
