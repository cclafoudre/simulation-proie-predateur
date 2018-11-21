public class Pos {
    public int X;
    public int Y;
    public int[] pos;

    /**
     * Classe pour gérer les positions X,Y avec plus de facilités
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
    public int[] getPos() {
        return pos;
    }

    public static Pos getRandomPos(int taille) {
        return new Pos((int)(Math.random()*taille),(int)(Math.random()*taille));
    }

    public static Object getTab(Object[][] tab, Pos pos){
        return tab[pos.getY()][pos.getX()];
    }

    public static void delTab(Object[][] tab, Pos pos){
        tab[pos.getY()][pos.getX()] = null;
    }

    public static void setTab(Object[][] tab, Pos pos, Object obj){
        tab[pos.getY()][pos.getX()] = obj;
    }

    @Override
    public String toString() {
        return "x="+getX()+" y="+getY();
    }

    public boolean positionValide(int taille){
        return  (0<X && 0<Y && X<taille && Y<taille);
    }
}
