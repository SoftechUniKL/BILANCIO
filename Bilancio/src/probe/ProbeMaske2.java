package probe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.text.MaskFormatter;

public class ProbeMaske2 extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",
			Locale.GERMANY);
	private static NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);
	private static Date today;
	static String todayFormated = "";

	static JButton saveButton;
	static JButton deleteButton;

	static JFormattedTextField tfDatum;
	static JFormattedTextField tfBezeichnung;
	static JFormattedTextField tfBetrag;
	static JComboBox<String> cbKategorieEinnahme;
	static JComboBox<String> cbKategorieAusgabe;
	
	static JRadioButton RButtonEinnahme;
	static JRadioButton RButtonAusgabe; 

	static JLabel nameDatum;
	static JLabel nameBezeichnung;
	static JLabel nameBetrag;
	static JLabel nameKategorie;
	static JLabel nameEinnahmeAusgabe;
	static String [] Einnahmeliste;
	static String [] Ausgabeliste;
	static JPanel panel4;
	static JPanel mainPanel;
	static JFrame frame;
	static Number Betrag;

	//public static void main(String[] args) {
public ProbeMaske2(){
		Dimension eingabeSize = new Dimension(150, 20);

		today = new Date();
		todayFormated = df.format(today.getTime());
		//System.out.println("Heute ist der "+todayFormated);

		frame = new JFrame("Eingabe");
		frame.getContentPane().setPreferredSize(new Dimension(400, 300));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Hauptcontainer
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(6, 1));

		// Container und Elemente der Datum-Eingabe
		JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tfDatum = new JFormattedTextField(df);
		tfDatum.setColumns(10);
		nameDatum = new JLabel("Datum");
		nameDatum.setPreferredSize(eingabeSize);
		panel1.add(nameDatum);
		panel1.add(tfDatum);

		// Container und Elemente der Bezeichnung-Eingabe
		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nameBezeichnung = new JLabel("Bezeichnung");
		nameBezeichnung.setPreferredSize(eingabeSize);
		tfBezeichnung = new JFormattedTextField();
		tfBezeichnung.setColumns(20);
		panel2.add(nameBezeichnung);
		panel2.add(tfBezeichnung);

		// Container und Elemente der Betrag-Eingabe
		JPanel panel3 = new JPanel();
		panel3.setLayout(new FlowLayout(FlowLayout.LEFT));
		nameBetrag = new JLabel("Betrag");
		nameBetrag.setPreferredSize(eingabeSize);
		tfBetrag = new JFormattedTextField();
		tfBetrag.setColumns(5);
		tfBetrag.setText("0,00");
		panel3.add(nameBetrag);
		panel3.add(tfBetrag);

		// Container und Elemente der Kategorie-Eingabe
		panel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nameKategorie = new JLabel("Kategorie");
		nameKategorie.setPreferredSize(eingabeSize);
		
		//tfKategorie.setColumns(10);
		panel4.add(nameKategorie);
		//panel4.add(tfKategorie);
		
		Einnahmeliste = new String[] { "Gehalt", "Geschenk" , "Kapitalerträge", "sonstige"};
		Ausgabeliste = new String[] { "Miete", "Lebensmittel" , "Versicherungen", "Freizeit", "Hobbys", "Bildung", "Zins- und Tilgungszahlungen"};
		
		

		// Container und Elemente der Einnahme.Aisgabe-Eingabe
		//TODO : Eingabe / Ausgabe durch Radio buttons ersetzen 
		

	    RButtonEinnahme = new JRadioButton("Einnahme");
	    RButtonEinnahme.setMnemonic(KeyEvent.VK_E);
	    RButtonEinnahme.setActionCommand("Einnahme");

	    RButtonAusgabe = new JRadioButton("Ausgabe");
	    RButtonAusgabe.setMnemonic(KeyEvent.VK_A);
	    RButtonAusgabe.setActionCommand("Ausgabe");
	    
	    ButtonGroup group = new ButtonGroup();
	    group.add(RButtonEinnahme);
	    group.add(RButtonAusgabe);
		JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nameEinnahmeAusgabe = new JLabel("Wählen Sie");
		nameEinnahmeAusgabe.setPreferredSize(eingabeSize);
		
		panel5.add(nameEinnahmeAusgabe);
		panel5.add(RButtonEinnahme);
		panel5.add(RButtonAusgabe);

		// Container für Save und Delete Buttons
		JPanel panel6 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		saveButton = new JButton("Speichern");
		deleteButton = new JButton("Löschen");
		panel6.add(saveButton);
		panel6.add(deleteButton);

		mainPanel.add(panel5);
		mainPanel.add(panel1);
		mainPanel.add(panel4);
		mainPanel.add(panel2);
		mainPanel.add(panel3);
		mainPanel.add(panel6);

		mainPanel.setBackground(Color.CYAN);
		frame.getContentPane().add(mainPanel);

		
		// Aktionen um eingegebene Daten zu speichern oder zu löschen
		saveInput();
		deleteInput();
		OnClickEinnahme();
		OnClickAusgabe();

		frame.pack();
		frame.setVisible(true);

		try {
			MaskFormatter dateMask = new MaskFormatter("##/##/####");
			dateMask.install(tfDatum);
			tfDatum.setText(todayFormated);
			

		} catch (ParseException ex) {
			Logger.getLogger(ProbeMaske.class.getName()).log(
					Level.SEVERE, null, ex);
		}

		String str = tfDatum.getText();

		Date date = null;
		try {
			date = df.parse(str);
			//TODO: Datumeingabe validieren. Prüfe ob die Datumeingabe sinn macht
			// Fehler durch POP-Up Fenster anzeigen
		} catch (ParseException e) {
			System.out.println("Date invalid");
		}
		


	}
	
	public static void OnClickEinnahme () {
		// Wenn beim Radiobutton Einnahme angeklickt wird
		RButtonEinnahme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				cbKategorieEinnahme = new JComboBox<String> (Einnahmeliste);
				
				
				if (panel4.getComponentCount()==1){
					panel4.add(cbKategorieEinnahme);
				}
				if (panel4.getComponentCount()==2){
					panel4.remove(1);
					panel4.add(cbKategorieEinnahme);}
				
				frame.printAll(frame.getGraphics());
				
			}
		});
	}
	
	public static void OnClickAusgabe () {
		// Wenn beim Radiobutton Ausgabe angeklickt wird
		RButtonAusgabe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				cbKategorieAusgabe = new JComboBox<String> (Ausgabeliste);
				
				
				if (panel4.getComponentCount()==1){
					panel4.add(cbKategorieAusgabe);
				}
				if (panel4.getComponentCount()==2){
					panel4.remove(1);
					panel4.add(cbKategorieAusgabe);}
				
				frame.printAll(frame.getGraphics());
				
			}
		});
	}

	
	public static void saveInput() {
		// Wenn der saveButton gedruckt wird, dann werden alle Werte der Textfelder gespeichert
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Datum:       " + tfDatum.getText());
				
				System.out.println("Bezeichnung: " + tfBezeichnung.getText());
				
				try {
					 Betrag =   nf.parse(tfBetrag.getText());
					//TODO: Eingabe des Betrages validieren. Prüfe ob die Eingabe sinn macht
					 //Fehler durch POP-Up Fenster anzeigen
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					
					 JOptionPane.showMessageDialog(null, "Eingabe darf nur Dezimalzahl sein. (123,56)", "Error",
                             JOptionPane.ERROR_MESSAGE);
				}
				
				
				System.out.println("Betrag:" +Betrag.doubleValue());
				
				if  (panel4.getComponentCount()>1 && RButtonEinnahme.isSelected()) {
					System.out.println("Kategorie: " + cbKategorieEinnahme.getSelectedItem());
				}
				
				if  (panel4.getComponentCount()>1 && RButtonAusgabe.isSelected()) {
					System.out.println("Kategorie: " + cbKategorieAusgabe.getSelectedItem());
				}
				
			}
		});
	}

	public static void deleteInput() {
		// Wenn der deleteButton gedruckt wird, dann werden alle Textfelder gelöscht
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tfDatum.setText(todayFormated);
				tfBezeichnung.setText("");
				tfBetrag.setText("0,00");
				
				System.out.println("Alle Eingaben gelöscht.");

			}
		});
	}

}
