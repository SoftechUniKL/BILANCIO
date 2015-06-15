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

	static JRadioButton RButtonEinnahme;
	static JRadioButton RButtonAusgabe;

	static JLabel nameDatum;
	static JLabel nameBezeichnung;
	static JLabel nameBetrag;
	static JLabel nameKategorie;
	static JLabel nameEinnahmeAusgabe;
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
	public EingabeMaske(BudgetPlanModel budget, MyTableModel tableModel) {
		this.budget = budget;
		this.tableModel = tableModel;

		Dimension eingabeSize = new Dimension(150, 20);

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
		JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tfDatum = new JFormattedTextField(df);
		tfDatum.setColumns(10);
		nameDatum = new JLabel("Datum");
		nameDatum.setPreferredSize(eingabeSize);
		datePanel.add(nameDatum);
		datePanel.add(tfDatum);

		// Container und Elemente der Bezeichnung-Eingabe
		JPanel bezeichnungPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nameBezeichnung = new JLabel("Bezeichnung");
		nameBezeichnung.setPreferredSize(eingabeSize);
		tfBezeichnung = new JFormattedTextField();
		tfBezeichnung.setColumns(20);
		bezeichnungPanel.add(nameBezeichnung);
		bezeichnungPanel.add(tfBezeichnung);

		// Container und Elemente der Betrag-Eingabe
		JPanel betragPanel = new JPanel();
		betragPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		nameBetrag = new JLabel("Betrag");
		nameBetrag.setPreferredSize(eingabeSize);
		tfBetrag = new JFormattedTextField();
		tfBetrag.setColumns(5);
		tfBetrag.setText("0,00");
		betragPanel.add(nameBetrag);
		betragPanel.add(tfBetrag);

		// Container und Elemente der Kategorie-Eingabe
		kategoriePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
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
		JPanel panelEinnameAusgabe = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nameEinnahmeAusgabe = new JLabel("Wählen Sie");
		nameEinnahmeAusgabe.setPreferredSize(eingabeSize);

		panelEinnameAusgabe.add(nameEinnahmeAusgabe);
		panelEinnameAusgabe.add(RButtonEinnahme);
		panelEinnameAusgabe.add(RButtonAusgabe);

		// Container für Save und Delete Buttons
		JPanel panelForButtonSpeicherUndLoeschen = new JPanel(new FlowLayout(FlowLayout.CENTER));
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
