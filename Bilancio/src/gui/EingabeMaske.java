package gui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;
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
import javax.swing.JScrollPane;
import javax.swing.text.MaskFormatter;

import model.BudgetPlanModel;
import model.Posten;

import com.opencsv.CSVWriter;

public class EingabeMaske extends JFrame {

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

	static BudgetPlanModel budget;
	static JScrollPane scrollpane;
	static MyTableModel tableModel;

	// public static void main(String[] args) {
	//public EingabeMaske(BudgetPlanModel budget, MyTableModel tableModel) {
	public EingabeMaske(){
//		this.budget = budget;
//		this.tableModel = tableModel;

		Dimension eingabeSize = new Dimension(150, 20);
		FlowLayout fl = new FlowLayout(FlowLayout.LEFT,1,15);

		today = new Date();
		todayFormated = df.format(today.getTime());
		// System.out.println("Heute ist der "+todayFormated);

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
		//datePanel.setAlignmentX(CENTER_ALIGNMENT);
		tfDatum = new JFormattedTextField(df);
		tfDatum.setColumns(10);
		
		nameDatum = new JLabel("Datum");
		nameDatum.setPreferredSize(eingabeSize);
		datePanel.add(nameDatum);
		listeMonate = new String[] { "Januar", "Februar", "März","April","Mai","Juni","Juli"
		,"August","September","Oktober","November","Dezember" };
		
		cbMonate= new JComboBox<String>(listeMonate);
		//datePanel.add(tfDatum);
		datePanel.add(cbMonate);
		
		listeTag = new String[] { "1", "2", "3","4","5","6","7"
				,"8","9","10","11","12","13","14","15","16","17","18",
				"19","20","21","22","23","24","25","26","27",
				"28","29","30","31"};
				
				cbTag= new JComboBox<String>(listeTag);
				//datePanel.add(tfDatum);
				datePanel.add(cbTag);
				
		listeJahre= new String[] { "2010", "2011","2012","2013","2014","2015","2016"
						,"2017","2018","2018","2019","2020","2021","2022","2023","2024","2025","2026",
						"2027","2028","2029","2030","2031","2032","2033","2034","2035",
						"2036","2037","2038","2039","2040"};
						
						cbJahr= new JComboBox<String>(listeJahre);
						//datePanel.add(tfDatum);
						datePanel.add(cbJahr);
		
		
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
		//betragPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
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
		

		listeEinnahmen = new String[] { "Gehalt", "Geschenk", "Kapitalerträge",
				"sonstige" };
		listeAusgaben = new String[] { "Miete", "Lebensmittel",
				"Versicherungen", "Freizeit", "Hobbys", "Bildung",
				"Zins- und Tilgungszahlungen" };

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
		FlowLayout flForButtons = new FlowLayout(FlowLayout.LEFT,80,15);
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
			Logger.getLogger(EingabeMaske.class.getName()).log(Level.SEVERE,
					null, ex);
		}

		String str = tfDatum.getText();

		Date date = null;
		try {
			date = df.parse(str);
			// TODO: Datumeingabe validieren. Prüfe ob die Datumeingabe sinn
			// macht
			// Fehler durch POP-Up Fenster anzeigen
		} catch (ParseException e) {
			System.out.println("Date invalid");
			e.printStackTrace();
		}

	}

	public static void OnClickEinnahme() {
		// Wenn beim Radiobutton Einnahme angeklickt wird
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

	public static void OnClickAusgabe() {
		// Wenn beim Radiobutton Ausgabe angeklickt wird
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

	public static void saveInput() {
		// Wenn der saveButton gedruckt wird, dann werden alle Werte der
		// Textfelder gespeichert
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Datum:       " + tfDatum.getText());

				System.out.println("Bezeichnung: " + tfBezeichnung.getText());

				try {
					Betrag = nf.parse(tfBetrag.getText());
					// TODO: Eingabe des Betrages validieren. Prüfe ob die
					// Eingabe sinn macht
					// Fehler durch POP-Up Fenster anzeigen
				} catch (ParseException e1) {
					// TODO Auto-generated catch block

					JOptionPane.showMessageDialog(null,
							"Eingabe darf nur Dezimalzahl sein. (123,56)",
							"Error", JOptionPane.ERROR_MESSAGE);
				}

				System.out.println("Betrag:" + Betrag.doubleValue());

				if (kategoriePanel.getComponentCount() > 1
						&& RButtonEinnahme.isSelected()) {
					System.out.println("Kategorie: "
							+ cbKategorieEinnahme.getSelectedItem());
				}

				if (kategoriePanel.getComponentCount() > 1
						&& RButtonAusgabe.isSelected()) {
					System.out.println("Kategorie: "
							+ cbKategorieAusgabe.getSelectedItem());
				}

				saveEingabe();

				tableModel.addRow(new Object[5]);
				int lastRow = tableModel.getRowCount();

				tableModel.setValueAt(tfDatum.getText(), lastRow - 1, 0);
				if (RButtonEinnahme.isSelected()) {
					tableModel.setValueAt(
							cbKategorieEinnahme.getSelectedItem(), lastRow - 1,
							1);
					tableModel.setValueAt("Einnahme", lastRow - 1, 4);
				}
				if (RButtonAusgabe.isSelected()) {
					tableModel.setValueAt(cbKategorieAusgabe.getSelectedItem(),
							lastRow - 1, 1);
					tableModel.setValueAt("Ausgabe", lastRow - 1, 4);
				}
				tableModel.setValueAt(tfBezeichnung.getText(), lastRow - 1, 2);
				tableModel.setValueAt(Betrag.doubleValue(), lastRow - 1, 3);

				frame.dispose();

			}
		});
	}

	// Eingabe speichern
	public static void saveEingabe() {
		
		

		Date datum = null;
		String dat = tfDatum.getText();

		try {
			datum = df.parse(dat);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String bezeichnung = (String) tfBezeichnung.getText();
		Number betrag = null;
		try {
			betrag = nf.parse(tfBetrag.getText());
			// TODO: Eingabe des Betrages validieren. Prüfe ob die Eingabe sinn
			// macht
			// Fehler durch POP-Up Fenster anzeigen
		} catch (ParseException e1) {
			// TODO Auto-generated catch block

			JOptionPane.showMessageDialog(null,
					"Eingabe darf nur Dezimalzahl sein. (123,56)", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		String kategorie = "";
		String transaktionsArt = "";
		if (kategoriePanel.getComponentCount() > 1 && RButtonEinnahme.isSelected()) {
			kategorie = (String) cbKategorieEinnahme.getSelectedItem();
			transaktionsArt = "Einnahme";

		}

		if (kategoriePanel.getComponentCount() > 1 && RButtonAusgabe.isSelected()) {
			kategorie = (String) cbKategorieAusgabe.getSelectedItem();
			transaktionsArt = "Ausgabe";
		}

		budget.ausgaben.add(new Posten(datum, kategorie, bezeichnung, betrag
				.doubleValue(), transaktionsArt));

		
		budget.tell("New Transaction has been added.");
		
		/*
		CSVWriter writer = null;
		String[] line = new String[5];
		String str;
		try {
			writer = new CSVWriter(new FileWriter("data/budget.csv"), '#',
					CSVWriter.NO_QUOTE_CHARACTER);

			NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
			int i = 0;
			for (Posten p : budget.ausgaben) {

				line[0] = new SimpleDateFormat("dd.MM.yyyy").format(p
						.getDatum());
				line[2] = p.getBezeichnung();
				line[3] = Double.toString(p.getBetrag());
				line[1] = p.getKategorie().toString();
				line[4] = p.getTransaktionsart();

				writer.writeNext(line);
				i++;

			}

			writer.close();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
	}

	public static void deleteInput() {
		// Wenn der deleteButton gedruckt wird, dann werden alle Textfelder
		// gelöscht
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
