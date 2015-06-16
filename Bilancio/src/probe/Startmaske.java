package probe;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

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
	
	Date d = new Date();
	System.out.println(getDayDiff(d));
	
	
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
	
	 public static   int getDayDiff(Date d1){
	        Calendar cal1 = Calendar.getInstance();
	        Calendar cal = Calendar.getInstance();
	        cal.set(Calendar.DAY_OF_MONTH, 01);
	        cal.set(Calendar.MONTH,00);  // 01 ist Jan, 01 Feb , ...   
	        
	        cal.set(Calendar.YEAR, 2015);
	        int month = cal.get(Calendar.MONTH);
	        int day = cal.get(Calendar.DAY_OF_MONTH);
	        int year = cal.get(Calendar.YEAR);
	        
	        System.out.println("Compare Calender Dat and cal1: "+ d1.before(cal.getTime()));
	        
	       System.out.println("Compare Calender cal and cal1: "+ cal.compareTo(cal1));
	       System.out.println("Compare Calender cal1 and cal: "+ cal1.compareTo(cal));
	        //1000 ms = 1s, 60s = 1 min, 60 min = 1h, 24h = 1 day
	        return (int) ((cal.getTimeInMillis() -d1.getTime())/(1000*60*60*24));
	    }
	

}
