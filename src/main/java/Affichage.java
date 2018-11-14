import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Affichage extends JFrame implements ActionListener {
    public Timer monTimer;
    private Plateau plateau;

    public JMenuBar barreMenus = new JMenuBar();
    public JMenuItem boutonAction = new JMenuItem("Effectuer un tour");

    public Affichage(){
        super();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(700,750);
        setLocationRelativeTo(null);
        plateau = new Plateau(45);
        add(plateau, BorderLayout.CENTER);
        monTimer = new Timer(10,this);

        boutonAction.addActionListener(this);
        barreMenus.add(boutonAction);
        setJMenuBar(barreMenus);
        setVisible(true);
        plateau.afficherPlateau();
        //monTimer.start();
    }

    public static void main(String[] args) {
        new Affichage();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(monTimer) || e.getSource().equals(boutonAction)) {
            System.out.println("On effectue un tour de simulation");
            plateau.afficherPlateau();
            //effectuer un tour
        }
        //fonction
    }
}
