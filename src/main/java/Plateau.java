import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Plateau extends JPanel {
    protected BufferedImage image;
    public Vivant[][] simulation;
    public int tailleCase = 10;
    protected int taille;

    public Plateau(int taille) {
        super();
        this.taille = taille;
        genererPlateau(taille);
        image = new BufferedImage((taille+1)*tailleCase,(taille+1)*tailleCase,BufferedImage.TYPE_INT_RGB);
    }

    public void genererPlateau(int taille) {
        simulation = new Vivant[taille][taille];
        for (int y=0; y<taille; y+=1) {
            for (int x=0; x<taille; x+=1) {
                simulation[y][x] = new Vivant();
            }
        }
        simulation[0][0] = new Lapin();
        simulation[taille-1][taille-1] = new Potiron();
    }

    public void afficherPlateau(){
        for(int y=0;y<taille; y++) {
            for(int x=0; x<taille; x++) {
                afficherCase(simulation[y][x], tailleCase*(x+1), tailleCase*(y+1));
                //afficherCase(simulation[y][x], x, y);
            }
        }
        repaint();
    }
    public void afficherCase(Vivant vivant, int x , int y) {
        //image.setRGB(x, y, vivant.getCouleur().getRGB());
        //System.out.println(taille*tailleCase);
        for (int j = -tailleCase; j < tailleCase; j++) {
            for (int i = -tailleCase; i < tailleCase; i++) {
                    int X=x + i;
                    int Y=y+j;
                try {
                    image.setRGB(X,Y, vivant.getCouleur().getRGB());
                }catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("x="+x+" y="+y);
                    System.out.println("x+i="+x+i+" y+j="+y+j);
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image != null)
        {
            g.drawImage(image, 0, 0, null);
        }
    }
}
