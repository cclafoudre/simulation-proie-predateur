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
}
