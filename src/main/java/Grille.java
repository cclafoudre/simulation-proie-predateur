import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Classe utilis&eacute;e pour l'affichage graphique de la simulation.
 * Pour afficher uniquement la modification sur une case, utiliser la m&eacute;thode {@link Grille#dessineCase(int, int, Vivant) dessineCase()}
 * Pour tout afficher d'un coup, appeler la m&eacute;thode {@link Grille#repaint() repaint()}
 */
public class Grille  extends JPanel implements Runnable, MouseListener, MouseMotionListener {
    int taille = 45;
    int tailleCase = 10;
    private Vivant[][] simulation;
    private Timer fps;
    public EditListener editListener;
    public Vivant dernierVivantSelectionne= new Potiron();

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
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
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
        g.fillRect(indexApixelsX(x), //dessine la case relativement au milieu de l'écran
                indexApixelsY(y),
                tailleCase-2,
                tailleCase-2);
    }

    /**
     * Dessine une seule case sur la grille sans se pr&eacute;occuper de rafra&icirc;chir l'affichache (c'est pour &ccedil;a que les cases nouvellement dessin&eacute;es se superposent aux menus
     * @param pos la position du vivant
     * @param ici le vivant &agrave; afficher
     */
    public void dessineCase(Pos pos, Vivant ici){
        Graphics g = getGraphics();
        dessineCase(pos.getX(), pos.getY(), ici);
        if (ici == null)
            g.setColor(Vivant.COULEUR);
        else
            g.setColor(ici.getCouleur());
        Pos px = pos.toPixels(getWidth(), getHeight(), taille, tailleCase);
        g.fillRect(px.X, px.Y, tailleCase-2, tailleCase-2);
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

    /**
     * Interface qui sert &agrave; effectuer proprement les modifications sur le tableau {@link Plateau#simulation} sans tout casser.
     */
    public interface EditListener{
        /**
         * Change un lapin en potiron, un potiron en rien, et rien en lapin.
         * @param pos la position de l'&eacute;l&eacute;ment
         * @return l'&eacute;l&eacute;ment cr&eacute;e
         */
        Vivant toggleVivant(Pos pos);

        /**
         * Force un vivant &agrave; la position donn&eacute;e
         * @param pos position
         * @param vivant &agrave; ins&eacute;rer
         */
        void setVivant(Pos pos, Vivant vivant);
    }

    /**
     * Assigne l'interface un peu comme le font les codes Java Swing
     * @param that une classe qui impl&eacute;ment {@link EditListener}
     */
    public void addEditListener(EditListener that){
        this.editListener = that;
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
    private int pixelsAindexX(int xp){
        return (xp-(getWidth()-tailleCase*taille)/2)/tailleCase;
    }
    private int pixelsAindexY(int xp){
        return (xp-(getHeight()-tailleCase*taille)/2)/tailleCase;
    }
    private int indexApixelsX(int i){
        return ((getWidth() - tailleCase*taille) / 2)+tailleCase *i;
    }
    private int indexApixelsY(int i){
        return ((getHeight() - tailleCase*taille) / 2)+tailleCase *i;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        /*Pos clicked = Pos.coordToPos(e.getX(),e.getY(),getWidth(),getHeight(),taille,tailleCase);
        if(clicked.positionValide(taille)){
            dernierVivantSelectionne = editListener.toggleVivant(clicked);
            //repaint();
        }else{
            //System.out.println(e.getX()+" "+e.getY());
            System.out.println("Position invalide: "+clicked);
        }*/
        mouseDragged(e);
    }
    public void mousePressed(MouseEvent e) {}public void mouseReleased(MouseEvent e) {}public void mouseEntered(MouseEvent e) {}public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {
        Pos clic = Pos.coordToPos(e.getX(),e.getY(),getWidth(),getHeight(),taille,tailleCase);
        if(clic.positionValide(taille)) {
            Vivant nouveau=null;//valeur par défaut
            if(dernierVivantSelectionne instanceof Lapin)
                nouveau=new Lapin();
            if(dernierVivantSelectionne instanceof Potiron)
                nouveau=new Potiron();
            editListener.setVivant(clic,nouveau);
        }

    }public void mouseMoved(MouseEvent e) {}

    public int autoFit(){
        tailleCase=(Math.min(getHeight(), getWidth())/taille)-1;
        repaint();
        return tailleCase;
    }
}
