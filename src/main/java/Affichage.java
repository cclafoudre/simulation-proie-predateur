import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Affichage extends JFrame implements ActionListener {
    public Timer monTimer;
    private Plateau plateau;
    private Graph graphique;

    private JPanel zoneAffichage = new JPanel(new BorderLayout());
    private JMenuBar barreMenus = new JMenuBar();
    private JMenu actions = new JMenu("Actions");
    private JMenuItem boutonAction = new JMenuItem("Simuler un tour");
    private JMenuItem boutonStart = new JMenuItem("Démarrer la simulation");
    private JMenuItem boutonStop = new JMenuItem("Arrêter la simulation");
    private JMenuItem plusDeVivants = new JMenuItem("Ajouter des vivants");
    private JMenuItem moinsDeVivants = new JMenuItem("Supprimer des vivants");
    private JMenuItem genVid = new JMenuItem("Générer la vidéo (ffmpeg requis)");

    private JMenu parametres = new JMenu("Paramètres");
    private JMenuItem infoZoom = new JMenuItem("Zoom");
    private JSlider tailleCase = new JSlider(4,70,10);
    private JMenuItem vitesseAnim = new JMenuItem("Délai de simulation");
    private JSlider slideVitesse = new JSlider(1,100,50);
    private JMenuItem cleanGraph = new JMenuItem("Remise à zéro du graphique");
    private JCheckBoxMenuItem capturePix = new JCheckBoxMenuItem("Enregistrer la simulation");

    private JMenuItem textFPS = new JMenuItem("Vitesse d'affichage: T(s)");
    private JSlider slideFPS = new JSlider(1,60,30);

    public Affichage(){
        super();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800,850);
        setTitle("Simluation Proie-prédateur");
        setLocationRelativeTo(null);
        plateau = new Plateau(45);
        zoneAffichage.add(plateau);
        monTimer = new Timer(slideVitesse.getValue(),this);

        boutonAction.addActionListener(this);
        boutonStart.addActionListener(this);
        boutonStop.addActionListener(this);
        vitesseAnim.addActionListener(this);
        plusDeVivants.addActionListener(this);
        moinsDeVivants.addActionListener(this);
        cleanGraph.addActionListener(this);
        capturePix.addActionListener(this);
        genVid.addActionListener(this);

        tailleCase.setToolTipText("Taille des cases");
        tailleCase.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                plateau.zoom(tailleCase.getValue());
            }
        });
        slideVitesse.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                monTimer.setDelay(slideVitesse.getValue());
            }
        });
        infoZoom.setEnabled(false);
        textFPS.setEnabled(false);
        slideFPS.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                float fps= (float) slideFPS.getValue();
                float hz = 1/fps;
                plateau.fps.setDelay((int) (1000*hz));
                textFPS.setText("Vitesse d'affichage (T="+hz+" s");
            }
        });
        parametres.add(infoZoom);
        parametres.add(tailleCase);
        parametres.add(capturePix);
        parametres.add(new JSeparator());
        parametres.add(textFPS);
        parametres.add(slideFPS);
        parametres.add(new JSeparator());
        parametres.add(vitesseAnim);
        parametres.add(slideVitesse);
        parametres.add(new JSeparator());
        parametres.add(cleanGraph);
        barreMenus.add(parametres);

        actions.add(boutonAction);
        actions.add(boutonStart);
        actions.add(boutonStop);
        actions.add(new JSeparator());
        actions.add(plusDeVivants);
        actions.add(moinsDeVivants);
        actions.add(new JSeparator());
        actions.add(genVid);
        barreMenus.add(actions);
        setJMenuBar(barreMenus);

        add(zoneAffichage, BorderLayout.CENTER);
        setVisible(true);
        plateau.afficherPlateau();
        monTimer.start();
        new Thread(()->{
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            plateau.genererAlea(120,50, graphique);
        }).start();
        graphique = Graph.lancer();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        //    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (UnsupportedLookAndFeelException e) {
        }
        new Affichage();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(monTimer) || e.getSource().equals(boutonAction)) {
            //System.out.println("On effectue un tour de simulation");
            //new Thread(()->{
            plateau.simuler();
            graphique.addData(plateau.getNbreVivants());
            //}).start();
            //effectuer un tour
        }
        if(e.getSource().equals(cleanGraph)){graphique.clean();}


        if(e.getSource().equals(boutonStart)){monTimer.start();}
        if(e.getSource().equals(boutonStop)){monTimer.stop();}

        if(e.getSource().equals(plusDeVivants)){plateau.genererAlea(100,100, graphique);}
        if(e.getSource().equals(moinsDeVivants)){plateau.supprAlea(100, graphique);}

        if(e.getSource().equals(capturePix)){plateau.toggleCapture();}
        if(e.getSource().equals(genVid)){plateau.lancerFfmpeg();}

        if(e.getSource().equals(vitesseAnim)){
            System.out.println("Choix de vitesse");
            String reponse = JOptionPane.showInputDialog(
                    this,
                    "Délai entre les simulations (ms) :",
                    "Paramètres",
                    JOptionPane.PLAIN_MESSAGE);
            try {
                int vitesse = Integer.parseInt(reponse);
                System.out.println("Délai changée à "+vitesse);
                monTimer.setDelay(vitesse);
            } catch (NumberFormatException e1){ }
        }

        //fonction
    }
}
