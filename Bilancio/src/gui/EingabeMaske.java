package gui;

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
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import model.IBudgetPlanModel;
import model.Posten;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import utility.DateLabelFormatter;

/**
 * Eingabemaske
 * @author Abraham Amanuel
 * @author Asmelash Samuel
 * @author Lamberty Tim
 *
 */

public class EingabeMaske extends JFrame {

	private static final long serialVersionUID = 1L;

	private static SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy",
			Locale.GERMANY);
	private static NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);
	private static Date today;
	static String todayFormated = "";

	static Date selectedDate;
	static JDatePickerImpl datePicker;

	static JButton saveButton;
	static JButton deleteButton;

	static JFormattedTextField tfDatum;
	static JFormattedTextField tfBezeichnung;
	static JFormattedTextField tfBetrag;
	static JComboBox<String> cbKategorieEinnahme;
	static JComboBox<String> cbKategorieAusgabe;
	static JComboBox<String> cbMonate;
	static JComboBox<String> cbTag;
	static JComboBox<String> cbJahr;
	static JRadioButton RButtonEinnahme;
	static JRadioButton RButtonAusgabe;

	static JLabel nameDatum;
	static JLabel nameBezeichnung;
	static JLabel nameBetrag;
	static JLabel nameKategorie;
	static JLabel nameEinnahmeAusgabe;
	static String[] listeMonate;
	static String[] listeTag;
	static String[] listeJahre;
	static String[] listeEinnahmen;
	static String[] listeAusgaben;
	static JPanel kategoriePanel;
	static JPanel mainPanel;
	static JFrame frame;
	static Number Betrag;

	static String transaktionsArt;

	static IBudgetPlanModel budget;
	static JScrollPane scrollpane;
	static MyTableModel tableModel;
	
	static boolean log = false;

	/**
	 * Konstruktor
	 */
	public EingabeMaske() {

		initWindow();

		// Aktionen um eingegebene Daten zu speichern oder zu löschen
		saveInput();
		deleteInput();
		OnClickEinnahme();
		OnClickAusgabe();

		frame.pack();
		frame.setVisible(true);

	}
	
	/**
	 * Initialisierung des Eingabefensters zum Hinzufügen einer neuen Transaktion 
	 */

	void initWindow() {

		Dimension eingabeSize = new Dimension(150, 20);
		FlowLayout fl = new FlowLayout(FlowLayout.LEFT, 1, 15);

		today = new Date();
		todayFormated = df.format(today.getTime());

		frame = new JFrame("Eingabe");
		frame.getContentPane().setPreferredSize(new Dimension(400, 300));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Hauptcontainer
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(6, 1));

		// Container und Elemente der Datum-Eingabe
		JPanel datePanel = new JPanel(fl);
		datePanel.setBackground(Color.LIGHT_GRAY);
		tfDatum = new JFormattedTextField(df);
		tfDatum.setColumns(10);

		nameDatum = new JLabel("Datum");
		nameDatum.setPreferredSize(eingabeSize);
		datePanel.add(nameDatum);

		// Container und Elemente der DatePicker
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePane = new JDatePanelImpl(model, p);
		datePicker = new JDatePickerImpl(datePane, new DateLabelFormatter());

		selectedDate = new Date();
		todayFormated = df.format(selectedDate.getTime());

		datePanel.add(datePicker);

		// Container und Elemente der Bezeichnung-Eingabe
		JPanel bezeichnungPanel = new JPanel(fl);
		bezeichnungPanel.setBackground(Color.LIGHT_GRAY);
		nameBezeichnung = new JLabel("Bezeichnung");
		nameBezeichnung.setPreferredSize(eingabeSize);
		tfBezeichnung = new JFormattedTextField();
		tfBezeichnung.setColumns(20);
		bezeichnungPanel.add(nameBezeichnung);
		bezeichnungPanel.add(tfBezeichnung);

		// Container und Elemente der Betrag-Eingabe
		JPanel betragPanel = new JPanel(fl);
		nameBetrag = new JLabel("Betrag");
		nameBetrag.setPreferredSize(eingabeSize);
		tfBetrag = new JFormattedTextField();
		tfBetrag.setColumns(5);
		tfBetrag.setText("0,00");
		betragPanel.add(nameBetrag);
		betragPanel.add(tfBetrag);

		// Container und Elemente der Kategorie-Eingabe
		kategoriePanel = new JPanel(fl);
		nameKategorie = new JLabel("Kategorie");
		nameKategorie.setPreferredSize(eingabeSize);
		kategoriePanel.add(nameKategorie);

		listeEinnahmen = new String[] { "Gehalt", "Geschenk", "Zinsen",
				"sonstiges" };
		listeAusgaben = new String[] { "Miete", "Lebensmittel",
				"Versicherungen", "Freizeit", "Hobbys", "Bildung",
				"Kredit", "sonstiges" };

		// Container und Elemente der Einnahme.Aisgabe-Eingabe
		RButtonEinnahme = new JRadioButton("Einnahme");
		RButtonEinnahme.setMnemonic(KeyEvent.VK_E);
		RButtonEinnahme.setActionCommand("Einnahme");

		RButtonAusgabe = new JRadioButton("Ausgabe");
		RButtonAusgabe.setMnemonic(KeyEvent.VK_A);
		RButtonAusgabe.setActionCommand("Ausgabe");

		ButtonGroup group = new ButtonGroup();
		group.add(RButtonEinnahme);
		group.add(RButtonAusgabe);
		JPanel panelEinnameAusgabe = new JPanel(fl);
		nameEinnahmeAusgabe = new JLabel("Wählen Sie");
		nameEinnahmeAusgabe.setPreferredSize(eingabeSize);

		panelEinnameAusgabe.add(nameEinnahmeAusgabe);
		panelEinnameAusgabe.add(RButtonEinnahme);
		panelEinnameAusgabe.add(RButtonAusgabe);

		// Container für Save und Delete Buttons
		FlowLayout flForButtons = new FlowLayout(FlowLayout.LEFT, 80, 15);
		JPanel panelForButtonSpeicherUndLoeschen = new JPanel(flForButtons);
		panelForButtonSpeicherUndLoeschen.setBackground(Color.LIGHT_GRAY);
		saveButton = new JButton("Speichern");
		deleteButton = new JButton("Löschen");
		panelForButtonSpeicherUndLoeschen.add(saveButton);
		panelForButtonSpeicherUndLoeschen.add(deleteButton);

		mainPanel.add(panelEinnameAusgabe);
		mainPanel.add(datePanel);
		mainPanel.add(kategoriePanel);
		mainPanel.add(bezeichnungPanel);
		mainPanel.add(betragPanel);
		mainPanel.add(panelForButtonSpeicherUndLoeschen);
		mainPanel.setBackground(Color.BLACK);

		frame.getContentPane().add(mainPanel);
	}
	
	/**
	 * Methode, die die Liste der Möglichkeiten anzeigt, wenn wir Einnahme als Transaktionsart wählen
	 */

	public static void OnClickEinnahme() {
		
		RButtonEinnahme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				cbKategorieEinnahme = new JComboBox<String>(listeEinnahmen);

				if (kategoriePanel.getComponentCount() == 1) {
					kategoriePanel.add(cbKategorieEinnahme);
				}
				
				if (kategoriePanel.getComponentCount() == 2) {
					kategoriePanel.remove(1);
					kategoriePanel.add(cbKategorieEinnahme);
				}

				frame.printAll(frame.getGraphics());
			}
		});
	}
/**
 * Methode, die die Liste der Möglichkeiten anzeigt, wenn wir Ausgabe als Transaktionsart wählen
 */
	public static void OnClickAusgabe() {
		
		RButtonAusgabe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				cbKategorieAusgabe = new JComboBox<String>(listeAusgaben);

				if (kategoriePanel.getComponentCount() == 1) {
					kategoriePanel.add(cbKategorieAusgabe);
				}
				if (kategoriePanel.getComponentCount() == 2) {
					kategoriePanel.remove(1);
					kategoriePanel.add(cbKategorieAusgabe);
				}

				frame.printAll(frame.getGraphics());

			}
		});
	}

	/**
	 * Wenn der saveButton gedruckt wird, dann werden alle Werte der Textfelder
	 * gespeichert
	 */

	public static void saveInput() {

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				selectedDate = (Date) datePicker.getModel().getValue();
				try {
					String dDate = new SimpleDateFormat("dd.MM.yyyy")
					.format(selectedDate);
					if(log)
					System.out.println("Datum: Picker :       " + dDate);
					tfDatum.setText(dDate);
					if(log)
					System.out.println("Datum:       " + tfDatum.getText());
					
				} catch (Exception e2) {
					
				}
				
				if(log)
				System.out.println("Bezeichnung: " + tfBezeichnung.getText());
				

				try {
					
					if(isNumeric(tfBetrag.getText()))
						Betrag = nf.parse(tfBetrag.getText());
					
				} catch (ParseException e1) {

					e1.printStackTrace();
				}
				if(log)
				System.out.println("Betrag:" + Betrag.doubleValue());

				if (kategoriePanel.getComponentCount() > 1
						&& RButtonEinnahme.isSelected()) {
					if(log)
					System.out.println("Kategorie: "
							+ cbKategorieEinnahme.getSelectedItem());
				}

				if (kategoriePanel.getComponentCount() > 1
						&& RButtonAusgabe.isSelected()) {
					if(log)
					System.out.println("Kategorie: "
							+ cbKategorieAusgabe.getSelectedItem());
				}

				if(budget != null  &&  isNumeric(tfBetrag.getText())) {
					saveEingabe();
					frame.dispose();
				}
				
					
				else
						
							JOptionPane.showMessageDialog(null,
									"Betrag darf nur Dezimalzahl sein. (123,56)", "Error",
									JOptionPane.ERROR_MESSAGE);
				

			}
		});
	}

	/**
	 * Methode zum Speichern einer Transaktion ins Modell
	 */
	public static void saveEingabe() {

		
		String bezeichnung = (String) tfBezeichnung.getText();
		Date datum = null;
		String dat = tfDatum.getText();
		
		Number betrag = null;
		

		try {
			datum = df.parse(dat);
			
			
				
			betrag = nf.parse(tfBetrag.getText());
			
		} catch (ParseException e1) {
//			JOptionPane.showMessageDialog(null,
//					"Geben Sie Datum ein. \n Eingabe darf nur Dezimalzahl sein. (123,56)", "Error",
//					JOptionPane.ERROR_MESSAGE);
//			e1.printStackTrace();


		}

		String kategorie = "";
		String transaktionsArt = "";
		if (kategoriePanel.getComponentCount() > 1
				&& RButtonEinnahme.isSelected()) {
			kategorie = (String) cbKategorieEinnahme.getSelectedItem();
			transaktionsArt = "Einnahme";

		}

		if (kategoriePanel.getComponentCount() > 1
				&& RButtonAusgabe.isSelected()) {
			kategorie = (String) cbKategorieAusgabe.getSelectedItem();
			transaktionsArt = "Ausgabe";
		}


		double betr = 0;
		try{
			
			 betr = betrag.doubleValue();
			 
			 budget.addPosten(new Posten(0,datum, kategorie, bezeichnung, betr
						, transaktionsArt));

				budget.tell("New Transaction has been added.");
				
		} catch (Exception e) {

		}
		
			

	}

	/**
	 * Wenn der deleteButton gedruckt wird, dann werden alle Textfelder gelöscht
	 */
	public static void deleteInput() {

		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				tfBezeichnung.setText("");
				tfBetrag.setText("0,00");
				
				if(log)
				System.out.println("Alle Eingaben gelöscht.");

			}
		});
	}
	
	
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\,\\d+)?");  //match a number with optional '-' and decimal.
	}

}
