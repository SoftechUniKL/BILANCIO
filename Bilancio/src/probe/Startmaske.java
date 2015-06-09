package probe;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Startmaske {
	static JButton Button2 ;
	
	public static void main(String[] args) {
	JFrame Frame2 = new JFrame ();
	Button2 = new JButton (" Öffnen");
	Frame2.getContentPane().add(Button2);
	
	Frame2.pack();
	Frame2.setVisible(true);
	
	addBehavior();
	
	}
	public static void addBehavior() {
		// registriere den ActionListener fuer den Button als anonyme Klasse
		Button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				JOptionPane.showMessageDialog(null,
//						"Add Posten!", "Hinweis", JOptionPane.PLAIN_MESSAGE);
				
				ProbeMaske2 pmaske = new ProbeMaske2();
//				pmaske.setVisible(true);
			}
		});
	}
	

}
