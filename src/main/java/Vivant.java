import javax.swing.*;
import java.awt.*;

public class Vivant {
    //public Vivant auDessus;
    //public Vivant enDessous;
    //public Vivant aDroite;
    //public Vivant aGauche;
    public int x;
    public int y;
    public Pos position;

    public static Color COULEUR = new Color(64, 123, 67);
    public int pointsDeVie = 0;

    public Vivant() {
        this(1);
    }
    public Vivant(int pv) {
        pointsDeVie = pv;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {this.y = y;}

    public void setPos(Pos pos){
        position = pos;
        setX(pos.getX());
        setY(pos.getY());
    }

    public Pos getPos() {
        return position;
    }

    public Color getCouleur() {return COULEUR;}

    /**
     * Détermine si le Vivant devra être affiché et éventuellement supprimmé le cas échéant
     * @return true s'il doit être affiché
     */
    public boolean visible() {
        return pointsDeVie > 0;
    }

}
