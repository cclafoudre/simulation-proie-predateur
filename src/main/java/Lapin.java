import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Il peut se déplacer
 */
public class Lapin extends Vivant{
    public int pointsDeNutrition;
    public static Color COULEUR= new Color(255, 255, 255);
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

    public void jourSansManger(){
        pointsDeNutrition-=1;
    }
    public Color getCouleur() {
        return COULEUR;
    }

    @Override
    public void vivre(ActionEvent e){
        if(pointsDeNutrition<=0)
            pointsDeVie-=1;
        else
            jourSansManger();
    }
}
