import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
public class ChampReglage extends JPanel {
    public int valeur;
    private JSlider valSlider;
    private JTextField valField;
    private JButton submit;

    public ChampReglage(int v, String description, int min, int max, int defaut) {
	valSlider = new JSlider(min, max, defaut);
	valField = new JTextField(""+defaut);
	submit = new JButton(description);

	submit.addActionListener(new ActionListener(){
	    	@Override
		public void actionPerformed(ActionEvent e) {
		    try{valeur=Integer.parseInt(valField.getText());
		    System.out.println(valeur);}
		    catch(NumberFormatException e1){}
		}
	});
	valSlider.addChangeListener(new ChangeListener() {
	    @Override
	    public void stateChanged(ChangeEvent e){
	       valeur = valSlider.getValue();
	       valField.setText(""+valeur);
	    }
	});	    

	add(valSlider, BorderLayout.WEST);
	add(valField, BorderLayout.CENTER);
	add(submit, BorderLayout.EAST);
    }
}
