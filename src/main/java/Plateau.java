import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Plateau extends JPanel {
    protected BufferedImage image;
    public Vivant[][] simulation;
    public int tailleCase = 15;
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
        simulation[2][0] = new Potiron();
        simulation[2][1] = new Lapin();
        simulation[5][5] = new Lapin();
        simulation[taille-2][taille-2] = new Potiron();
        simulation[taille-5][taille-2] = new Potiron();
        simulation[taille-2][taille-5] = new Potiron();
        simulation[taille-1][taille-1] = new Potiron();
        simulation[taille-1][taille-2] = new Lapin();
        simulation[taille-2][taille-1] = new Lapin();
    }

    public void afficherPlateau(){
        for(int y=0;y<taille+1; y++) {
            for(int x=0; x<taille+1; x++) {
                if(x<taille && y<taille)
                    afficherCase(simulation[y][x], tailleCase*(x+1), tailleCase*(y+1));
            }
        }
        repaint();
    }
    private void afficherCase(Vivant vivant, int x, int y) {
        for (int j = -tailleCase/2+1; j < tailleCase/2-1; j++) {
            for (int i = -tailleCase/2+1; i < tailleCase/2-1; i++) {
                    int X=x + i;
                    int Y=y+j;
                try {
                    image.setRGB(X,Y, vivant.getCouleur().getRGB());
                }catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("x="+x+" y="+y);
                    System.out.println("x+i="+x+i+" y+j="+y+j);
                    e.printStackTrace();
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
