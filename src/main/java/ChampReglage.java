import java.awt.*;
import javax.swing.*;
public class ChampReglage extends JPanel {
	public int valeur;
	public JSlider valSlider;
	public JTextField valField;
	public JButton submit;

	public ChampReglage(int v, String description, int min, int max, int defaut) {
		valSlider = new JSlider(min, max, defaut);
		valField = new JTextField(""+defaut);
		submit = new JButton(description);

		submit.setEnabled(false);

		add(valSlider, BorderLayout.WEST);
		add(valField, BorderLayout.CENTER);
		add(submit, BorderLayout.EAST);
	}
}