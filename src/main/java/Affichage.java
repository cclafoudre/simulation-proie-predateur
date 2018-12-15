import javax.management.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Classe pour lancer le programme. Il y a dedans tous les contr&ocirc;les, et c'est elle qui poss&egrave;de et g&egrave;re les objets
 * {@link Grille} (affichage du plateau de simulation),
 * {@link Graphique} (affiche l'&eacute;volution dans le temps des populations)
 * et {@link Plateau} (logique m&eacute;tier des tours de simulation, n'effectue aucun affichage)
 */
public class Affichage extends JFrame implements ActionListener {
    private Vivant[][] simulation;
    private Plateau plateau;
    private Grille grille;

    private Graphique graphique;
    private Graphique lapinsPotironsG2;
    private JFrame gf1;
    private JFrame gf2;

    Timer updateGraph = new Timer(100, this);

    private JSplitPane graphs;
    private JSplitPane vue;

    private JMenuBar barreMenus = new JMenuBar();
    private JMenu actions = new JMenu("Actions");
    private JMenuItem plusDeVivants = new JMenuItem("Ajouter des vivants");
    private JMenuItem moinsDeVivants = new JMenuItem("Supprimer des vivants");
    private JMenuItem resetVivants = new JMenuItem("Tout supprimer");
//    private JMenuItem genVid = new JMenuItem("Générer la vidéo (ffmpeg requis)");
    private JRadioButtonMenuItem mLapin = new JRadioButtonMenuItem("Ajouter des lapins", new Icone(Lapin.COULEUR));
    private JRadioButtonMenuItem mPotiron = new JRadioButtonMenuItem("Ajouter des potirons", new Icone(Potiron.COULEUR));
    private JRadioButtonMenuItem mVide = new JRadioButtonMenuItem("Supprimer des vivants", new Icone(Vivant.COULEUR));

    private JMenu affichage = new JMenu("Affichage");
    private JMenuItem infoZoom = new JMenuItem("Zoom");
    private JSlider tailleCase = new JSlider(4,50,Plateau.tailleCase);
    private JMenuItem vivantsTimer = new JMenuItem("Changer le délai de mort");
    private JSlider slideVitesse = new JSlider(1,100,1);
//    private JCheckBoxMenuItem capturePix = new JCheckBoxMenuItem("Enregistrer la simulation");
    private JMenuItem textFPS = new JMenuItem("Vitesse d'affichage: T(s)");
    private JCheckBoxMenuItem singleRefresh = new JCheckBoxMenuItem("Rafraîchir tout le plateau");
    private JSlider slideFPS = new JSlider(1,60,30);
    private JMenuItem autofit = new JMenuItem("Ajuster la taille automatiquement");
    private JMenuItem resetDisposition = new JMenuItem("Réinitialiser la disposition");

    private JMenu simulMenu = new JMenu("Simulation");
    private JMenuItem boutonAction = new JMenuItem("Simuler un tour");
    private JMenuItem startStopSimul = new JMenuItem("Démarrer/arrêter la simulation");

    private JMenu graphControl = new JMenu("Graphiques");
    private JCheckBoxMenuItem lapPotFenetre = new JCheckBoxMenuItem("Graphique Lapins&Potirons dans une fenêtre séparée");
    private JCheckBoxMenuItem lapFpotFenetre = new JCheckBoxMenuItem("Graphique Lapins=f(Potirons) dans une fenêtre séparée");
    private JMenuItem cleanLapPot = new JMenuItem("Réintialiser le graphique des lapins & potirons");
    private JMenuItem cleanLapFpot = new JMenuItem("Réinitialiser le graphique cyan : lapins=f(potirons)");

    public Affichage(int taille){
        super();
        simulation = new Vivant[taille][taille];
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1200,850);
        setTitle("Simluation Proie-prédateur");
        setLocationRelativeTo(null);

        grille = new Grille(simulation);

        graphique = new Graphique();//Graphique.nouvelleFenetre();
        graphique.setTypeAffichage(Graphique.DEUX_COURBES);
        lapinsPotironsG2 = new Graphique();
        graphique.setChaineDeGraphique(lapinsPotironsG2);

        plateau = new Plateau(simulation, grille);
        plateau.setDisplay(grille);

        //setContentPane(grille);

        boutonAction.addActionListener(this);
        startStopSimul.addActionListener(this);
        vivantsTimer.addActionListener(this);
        plusDeVivants.addActionListener(this);
        moinsDeVivants.addActionListener(this);
        resetVivants.addActionListener(this);
//        capturePix.addActionListener(this);
//        genVid.addActionListener(this);
        mLapin.addActionListener(this);
        mPotiron.addActionListener(this);
        mVide.addActionListener(this);
        autofit.addActionListener(this);
        resetDisposition.addActionListener(this);
        singleRefresh.addActionListener(this);
        lapFpotFenetre.addActionListener(this);
        lapPotFenetre.addActionListener(this);
        cleanLapFpot.addActionListener(this);
        cleanLapPot.addActionListener(this);

        startStopSimul.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
        boutonAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0));
        mLapin.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0));
        mPotiron.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0));
        mVide.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, 0));

        /*plusDeVivants.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0));
        plusDeVivants.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.SHIFT_DOWN_MASK));
        moinsDeVivants.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0));*/

        plusDeVivants.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0));
        moinsDeVivants.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0));

        tailleCase.setToolTipText("Taille des cases");
        tailleCase.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                grille.zoom(tailleCase.getValue());
            }
        });
        slideVitesse.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                plateau.setSimulationDelay(slideVitesse.getValue());
            }
        });
        infoZoom.setEnabled(false);
        textFPS.setEnabled(false);
        slideFPS.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                float fps= (float) slideFPS.getValue();
                float hz = 1/fps;
                grille.setRefreshDelay((int) (1000*hz));
                textFPS.setText("Vitesse d'affichage (T="+hz+" s");
            }
        });

        affichage.add(infoZoom);
        affichage.add(tailleCase);
        affichage.add(autofit);
//        affichage.add(capturePix);
        affichage.add(new JSeparator());
        affichage.add(textFPS);
        affichage.add(slideFPS);
        affichage.add(singleRefresh);
        affichage.add(new JSeparator());
        affichage.add(resetDisposition);

        simulMenu.add(boutonAction);
        simulMenu.add(startStopSimul);
        simulMenu.add(new JSeparator());
        JMenuItem a1 = new JMenuItem("Délai de simulation");
        a1.setEnabled(false);
        simulMenu.add(a1);
        simulMenu.add(slideVitesse);
        simulMenu.add(new JSeparator());
        simulMenu.add(vivantsTimer);

        actions.add(plusDeVivants);
        actions.add(moinsDeVivants);
//        actions.add(new JSeparator());
//        actions.add(genVid);
        actions.add(resetVivants);
        actions.add(new JSeparator());
        actions.add(mLapin);
        actions.add(mPotiron);
        actions.add(mVide);

        graphControl.add(lapPotFenetre);
        graphControl.add(lapFpotFenetre);
        graphControl.add(cleanLapPot);
        graphControl.add(cleanLapFpot);

        barreMenus.add(affichage);
        barreMenus.add(simulMenu);
        barreMenus.add(actions);
        barreMenus.add(graphControl);
        //barreMenus.add(boutonStart);
        //barreMenus.add(boutonStop);
        setJMenuBar(barreMenus);

        /*graphs = new JTabbedPane();
        graphs.addTab("Potirons & Lapins séparés",graphique);
        graphs.addTab("lapins=f(Potirons)", lapinsPotironsG2);*/
        graphs= new JSplitPane(JSplitPane.VERTICAL_SPLIT);graphs.setDividerLocation(getHeight()/3);graphs.setContinuousLayout(true);
        graphs.add(graphique,0);
        graphs.add(lapinsPotironsG2,1);
        vue = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        vue.add(grille);
        vue.add(graphs);
        vue.setDividerLocation(getWidth()/2);
        vue.setContinuousLayout(true);
        vue.addPropertyChangeListener(new PropertyChangeListener() { //pour que la grille se mette toute seule à la meilleure taille quand on déplace le slider
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                tailleCase.setValue(grille.autoFit());
            }
        });
        add(vue);
        setVisible(true);
        new Thread(()->{
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            plateau.genererAlea(1000,50);
        }).start();
        updateGraph.start();
        grille.autoFit();
        //JOptionPane.showMessageDialog(null,new JLabel("Veuillez lancer la simulation depuis le menu Simulation > Lancer"),"Task failed successfully !",JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) throws URISyntaxException, IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        URI nomJar = Affichage.class.getProtectionDomain().getCodeSource().getLocation().toURI();
        if (args.length == 0 && nomJar.toString().endsWith("jar")) {
                // re-launch the app itselft with VM option passed
            System.out.println(nomJar.getPath());
            Runtime.getRuntime().exec(new String[] {"java", "-Xmx100m", "-jar", nomJar.getPath(), "45"}); //ça évite que le programme prenne 1Go de RAM ...
            System.exit(0);
        } //merci à internet pour l'astuce !
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        //UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        Affichage af;
        for (int i = 0; i < args.length; i++) {
            System.out.print(args[i]+" ");
            System.out.println(Integer.parseInt(args[i]));
        }
        try{
            System.out.println(Integer.parseInt(args[0]));
            af=new Affichage(Integer.parseInt(args[0]));
        }catch (Exception e){
            af=new Affichage(50);
        }
        try {
            mbs.registerMBean(af.plateau, new ObjectName("org.jeanribes.simulation-proie-predateur:type=PlateauMBean"));

            System.out.println("Lancement ...");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        } catch (MBeanRegistrationException e) {
            e.printStackTrace();
        } catch (NotCompliantMBeanException e) {
            e.printStackTrace();
        }
        System.out.println("Lancement ...");
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(boutonAction)) {
            //System.out.println("On effectue un tour de simulation");
            //new Thread(()->{
            Plateau.SIMULATION_ACTIVE=true;
            plateau.run();
            Plateau.SIMULATION_ACTIVE=false; //sinon la boucle "getRandomVivant" renvoie null car elle s'arrête direct
            //}).start();
            //effectuer un tour
        }
        if(e.getSource().equals(autofit)) {
            tailleCase.setValue(grille.autoFit());
        }

        if(e.getSource().equals(startStopSimul)){toggleSimulation();}

        if(e.getSource().equals(plusDeVivants)){plateau.genererAlea(200,10);System.out.println("ajout de viants");}
        if(e.getSource().equals(moinsDeVivants)){plateau.supprAlea(100);}
        if(e.getSource().equals(resetVivants)){plateau.resetVivants();grille.repaint();}

        /*if(e.getSource().equals(capturePix)){plateau.toggleCapture();}
        if(e.getSource().equals(genVid)){plateau.lancerFfmpeg();}*/
        if(e.getSource().equals(singleRefresh)){
            if(!singleRefresh.getState()){
                //plateau.setDisplay(grille);
                grille.stopRefresh();
            }else {
                grille.startRefresh();
            }
        }
        if(e.getSource().equals(vivantsTimer)){
            System.out.println("Choix de Vitesse de mort");
            String reponse = JOptionPane.showInputDialog(
                    this,
                    "Délai entre les pertes de PV (ms) : (actuellement "+Vivant.DELAI_TIMER+"ms)",
                    "Paramètres",
                    JOptionPane.PLAIN_MESSAGE);
            try {
                int vitesse = Integer.parseInt(reponse);
                System.out.println("Délai changé à "+vitesse);
                Vivant.DELAI_TIMER = vitesse;
                plateau.setMortDelay(vitesse);
            } catch (NumberFormatException e1){ }
        }
        if(e.getSource().equals(mLapin)) {
            grille.dernierVivantSelectionne = new Lapin();
            mPotiron.setSelected(false);
            mVide.setSelected(false);
        }
        if(e.getSource().equals(mPotiron)) {
            grille.dernierVivantSelectionne = new Potiron();
            mLapin.setSelected(false);
            mVide.setSelected(false);
        }
        if(e.getSource().equals(mVide)) {
            grille.dernierVivantSelectionne = null;
            mPotiron.setSelected(false);
            mLapin.setSelected(false);
        }
        if(e.getSource().equals(updateGraph)){
            if(plateau.isSimulationActive()){
                graphique.addLapins(plateau.getNbreLapins());
                graphique.addPotirons(plateau.getNbrePotirons());
            }
            graphique.repaint();
        }
        if(e.getSource().equals(lapFpotFenetre)){
            if(lapFpotFenetre.getState()){
                graphs.remove(lapinsPotironsG2);
                gf1=Graphique.fenetre(lapinsPotironsG2);
            }
            else {
                gf1.dispose();
                graphs.add(lapinsPotironsG2,0);
                graphs.setDividerLocation(getHeight()/3);
            }
        }
        if(e.getSource().equals(lapPotFenetre)){
            if(lapPotFenetre.getState()){
                graphs.remove(graphique);
                gf2=Graphique.fenetre(graphique);
            }else {
                gf2.dispose();
                graphs.add(graphique,1);
                graphs.setDividerLocation(getHeight()/3);
            }
        }
        if(e.getSource().equals(resetDisposition)){
            vue.setDividerLocation(getWidth()/2);
            graphs.setDividerLocation(getHeight()/3);
            vue.updateUI();
            graphs.updateUI();
            repaint();
        }
        if(e.getSource().equals(cleanLapPot)){
            graphique.vider();
        }
        if(e.getSource().equals(cleanLapFpot)){
            lapinsPotironsG2.vider();
        }
    }
    public void setSize(int largeur, int hauteur){
        super.setSize(largeur, hauteur);
    }
    private void toggleSimulation(){
        if(plateau.isSimulationActive()) {
            plateau.stopSimulation();
            updateGraph.stop();
        }
        else {
            plateau.startSimulation();
            updateGraph.start();
        }
    }
}
class Icone implements Icon{
    private Color couleur;
    public Icone(Color color){couleur=color;}
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(couleur);
        g.fillRect(x,y,10,10);
    }

    @Override
    public int getIconWidth() {
        return 10;
    }

    @Override
    public int getIconHeight() {
        return 10;
    }
}

