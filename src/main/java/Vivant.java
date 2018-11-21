import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Vivant implements ActionListener {
    //public Vivant auDessus;
    //public Vivant enDessous;
    //public Vivant aDroite;
    //public Vivant aGauche;
    public int x;
    public int y;
    public Pos position;

    public static Color COULEUR = new Color(64, 123, 67);
    public static int DELAI_TIMER=7000;
    public int pointsDeVie = 0;

    public Timer timer;

    public Vivant() {
        this(1);
    }
    public Vivant(int pv) {
        pointsDeVie = pv;
        timer = new Timer(DELAI_TIMER, this);
        timer.start(); //commenter pour désactiver la mort des vivants
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

    @Override
    public void actionPerformed(ActionEvent e) {
        vivre(e);
    }

    public void setDelay(int ms){
        DELAI_TIMER=ms;
        timer.setDelay(ms+(int) (Math.random()*20));
    }

    public void startTimer(){
        timer.start();
    }
    public void stopTimer() {
        timer.stop();
    }

    public void vivre(ActionEvent e){
        pointsDeVie-=1;
    }
}
