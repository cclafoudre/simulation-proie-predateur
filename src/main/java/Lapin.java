import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Un objet repr&eacute;sentant un Lapin, qui peut se d&eacute;placer et manger les potions.
 * &Agrave; chaque fois qu'il mange, ses points de vie remontent au maximum, rallongeant son esp&eacute;rance de vie.
 */
public class Lapin extends Vivant{
    /**
     * Lorsque le lapin n'a plus de points de nutrition, il commence &agrave; perdre des points de vie.
     */
    public int pointsDeNutrition;
    public static final Color COULEUR= new Color(255, 255, 255);
    public static int PV_INITIAL=5;

    public Lapin(){
        super(PV_INITIAL);
    }

    public void manger() {
        if(pointsDeVie<PV_INITIAL) {
            //pointsDeVie += 1;
            pointsDeVie = PV_INITIAL;
        }
        else
            pointsDeNutrition+=1;
    }

    /**
     * Un tour de simulation pass&eacute; sans que la lapin mange, il pert donc un PV.
     */
    public void jourSansManger(){
        pointsDeNutrition-=1;
    }
    public Color getCouleur() {
        if(peutSeReproduire())
            return Color.gray;
        else
            return COULEUR;
    }

    @Override
    protected void vivre(){
        if(pointsDeNutrition<=0)
            super.vivre();
        else
            jourSansManger();
    }

    @Override
    public boolean peutSeReproduire() {
        return pointsDeNutrition>1;
    }
}
