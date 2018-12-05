import com.sun.istack.internal.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Repr&eacute;sente un &ecirc;tre vivant : {@link Potiron potiron} ou {@link Lapin lapin}.
 * Ils ont une dur&eacute;e de vie variable et meurent au bout d'un certain temps. une boucle se charge de les supprimmer lorsqu'ils meurent.
 * La variable {@link Vivant#SIMULATION_ACTIVE} active ou d&eacute;sactive le d&eacute;compte de leur esp&eacute;rance de vie.
 */
public class Vivant implements ActionListener {
    public int x;
    public int y;
    public Pos position;

    public static final Color COULEUR = new Color(64, 123, 67);
    public static int DELAI_TIMER=7000;
    public static boolean SIMULATION_ACTIVE; //active/désactive la mort des vivants
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
     * D&eacute;termine si le Vivant devra &ecirc;tre affich&eacute; et &eacute;ventuellement supprimm&eacute; le cas &eacute;ch&eacute;ant
     * @return true s'il doit &ecirc;tre affich&eacute;
     */
    public boolean visible() {
        return pointsDeVie > 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(SIMULATION_ACTIVE)
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

    /**
     * &Agrave; ex&eacute;cuter lors d'un tour de simulation, &ccedil;a fait perdre des PV.
     */
    protected void vivre(@Nullable ActionEvent e){
        pointsDeVie-=1;
    }
}
