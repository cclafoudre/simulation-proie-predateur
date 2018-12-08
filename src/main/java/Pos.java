import javax.swing.*;

/**
 * Classe pour g&eacute;rer les positions X,Y avec plus de facilit&eacute;s
 */
public class Pos {
    public int X;
    public int Y;
    public int[] pos;

    /**
     * Cr&eacute;e un objet Pos
     * @param x
     * @param y
     */
    public Pos(int x, int y) {
        this(new int[]{x, y});
    }
    public Pos(int[] pos) {
        this.pos = pos;
        X = pos[0];
        Y=pos[1];
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    /** Donnes les coordonn&eacute;es dans un tableau 1d &agrave; deux cases x et y.
     * @return un tableau 1d avec les coordonn&eacute;es x,y
     */
    public int[] getPos() {
        return pos;
    }

    /**
     * Renvoie une nouvelle position al&eacute;atoire contenue dans la taille donn&eacute;e.
     * @param taille la position &agrave; ne pas d&eacute;passer
     * @return une position al&eacute;atoire
     */
    public static Pos getRandomPos(int taille) {
        return new Pos((int)(Math.random()*taille),(int)(Math.random()*taille));
    }

    /**
     * Donne l'objet contenu dans le tableau &agrave; la position donn&eacute;e
     * @param tab
     * @param pos
     * @return un l'objet du tableau &agrave; cette position
     */
    public static Object getTab(Object[][] tab, Pos pos){
        return tab[pos.getY()][pos.getX()];
    }

    /**
     * Enl&egrave;ve l'objet du tableau
     * @param tab
     * @param pos
     */
    public static void delTab(Object[][] tab, Pos pos){
        tab[pos.getY()][pos.getX()] = null;
    }

    /**
     * Place l'objet dans le tableau
     * @param tab
     * @param pos
     * @param obj
     */
    public static void setTab(Object[][] tab, Pos pos, Object obj){
        tab[pos.getY()][pos.getX()] = obj;
    }

    @Override
    public String toString() {
        return "x="+getX()+" y="+getY();
    }

    /**
     * V&eacute;rifie que la position actuelle est incluse dans taille.
     * @param taille
     * @return
     */
    public boolean positionValide(int taille){
        return  (0<X && 0<Y && X<taille && Y<taille);
    }

    /**
     * V&eacute;rifie que les coordonn&eacute;es en pixels sont incluses dans le domaine d'affichage de la simulation.
     * @param p coordonn&eacute;e en pixel (X ou Y)
     * @param tailleWindow taille du component : {@link JPanel#getWidth()},  {@link JPanel#getHeight()}
     * @param tailleTableau taille (int) du tableau d'objets
     * @param tailleCase taille d'un case du tableau d'objets en pixels
     * @return vrai si c'est valide
     */
    public boolean pixelsAppartienentAImage(int p, int tailleWindow, int tailleTableau, int tailleCase){
        return p>(tailleWindow - tailleCase*tailleTableau)/2 && p<(tailleWindow + tailleCase*tailleTableau)/2;
    }

    /**
     * Convertit des coordonn&eacute;es en pixel en des index d'un tableau d'objets
     * @param p coord en pixel
     * @param tailleW taille du JPanel
     * @param tailleCase taille d'un case du tableau d'objets
     * @param taille taille du tableau d'objets
     * @return index
     */
    public static int pixelsAindex(int p, int tailleW, int tailleCase, int taille){
        return (p-(tailleW-tailleCase*taille)/2)/tailleCase;
    }

    /**
     * Convertit des coordonn&eacute;es en pixel en des index d'un tableau d'objets
     * @param i index
     * @param tailleW taille du JPanel
     * @param tailleCase taille d'un case du tableau d'objets
     * @param taille taille du tableau d'objets
     * @return coord en pixel
     */
    public static int indexApixels(int i, int tailleW, int tailleCase, int taille){
        return ((tailleW - tailleCase*taille) / 2)+tailleCase *i;
    }

    /**
     * Fait tout le travail pour renvoyer une coordonnée dans la base  du tableau d'objets à partir des pixels
     * @param x pixel
     * @param y pixel
     * @param wWidth {@link JPanel#getWidth()}
     * @param wHeight   {@link JPanel#getHeight()}
     * @param tailleTableau int
     * @param tailleCase int
     * @return Pos
     */
    public static Pos coordToPos(int x, int y,int wWidth,int wHeight, int tailleTableau, int tailleCase){
        return new Pos(pixelsAindex(x, wWidth, tailleCase, tailleTableau),
                pixelsAindex(y, wHeight, tailleCase, tailleTableau)
        );
    }

    /**
     * Renvoie une coordonnée en pixels et non dans la base du tableau
     * @param wWidth
     * @param wHeight
     * @param tailleTableau
     * @param tailleCase
     * @return
     */
    public Pos toPixels(int wWidth,int wHeight, int tailleTableau, int tailleCase){
        return new Pos(indexApixels(X, wWidth, tailleCase, tailleTableau),
                indexApixels(Y, wHeight, tailleCase, tailleTableau));
    }
}
