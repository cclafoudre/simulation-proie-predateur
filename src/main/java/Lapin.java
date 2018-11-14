import java.awt.*;

/**
 * Il peut se déplacer
 */
public class Lapin extends Vivant{
    public int pointsDeNutrition;
    public static Color COULEUR= new Color(255, 255, 255);

    public Lapin(){
        super(5);
    }
    public void manger() {
        pointsDeNutrition+=1;
    }

    public void jourSansManger(){
        pointsDeNutrition-=1;
    }
    public Color getCouleur() {
        return COULEUR;
    }
}
