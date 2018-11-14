import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Affichage extends JFrame implements ActionListener {
    public Vivant[][] plateau;
    private JPanel plateauPanel = new JPanel();
    public Timer monTimer;

    public JMenuBar barreMenus = new JMenuBar();
    public JMenuItem boutonAction = new JMenuItem("Effectuer un tour");

    public Affichage(){
        super();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(700,500);
        setLocationRelativeTo(null);
        plateau = genererPlateau(9, plateauPanel);
        add(plateauPanel, BorderLayout.CENTER);
        monTimer = new Timer(10,this);

        boutonAction.addActionListener(this);
        barreMenus.add(boutonAction);
        setJMenuBar(barreMenus);
        setVisible(true);
        //monTimer.start();
    }

    public static void main(String[] args) {
        new Affichage();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(monTimer) || e.getSource().equals(boutonAction)) {
            System.out.println("On effectue un tour de simulation");
            //effectuer un tour
        }
        //fonction
    }

    public Vivant[][] genererPlateau(int taille, JPanel plateauPanel) {
        plateau = new Vivant[taille][taille];
        plateauPanel.setLayout(new GridLayout(taille, taille));
        for (int y=0; y<taille; y+=1) {
            for (int x=0; x<taille; x+=1) {
                plateau[y][x] = new Vivant();
                plateauPanel.add(plateau[y][x],x,y);
            }
        }
        plateau[0][0] = new Lapin();
        plateau[taille-1][taille-1] = new Potiron();
        plateauPanel.add(new Lapin(),0,0);
        plateauPanel.add(new Potiron(),taille,taille);
        return plateau;
    }
}
