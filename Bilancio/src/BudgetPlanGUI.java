import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import com.opencsv.CSVWriter;

/**
 * Graphische Benutzeroberflaeche des BudgetPlaners
 * 
 */
public class BudgetPlanGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	/**
	 * Tabelle mit Uebersicht der Ausgaben
	 */
	private JTable table;
	private MyTableModel tableModel;
	private Object[][] data;
	/**
	 * Scrollelemente, das die Tabelle umfasst
	 */
	private JScrollPane scrollpane;
	/**
	 * Schaltflaeche, die beim Klicken einen Dialog anzeigt
	 */
	private JButton button;

	private JButton addPosten;
	private JButton saveTable;
	private JButton deletePosten;
	private JButton showkontostand;

	private TableColumn einAusgabeColumn;
	private JComboBox einAusgabeCombobox;
	private JComboBox DiagrammAuswahl;
	private DefaultPieDataset pdEinnahme;
	private DefaultPieDataset pdAusgabe;
	private ChartPanel panelEinnahme;
	private ChartPanel panelAusgabe;
	private int row;

	/**
	 * Modell der Daten
	 */
	private BudgetPlanModel budget;

	/**
	 * Konstruktor fuer die GUI.
	 * 
	 * Hier wird das Hauptfenster initialisiert und aktiviert.
	 * 
	 * @param budget
	 *            Modell der Daten
	 */
	public BudgetPlanGUI(BudgetPlanModel budget) {
		super("BILANCIO");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new FlowLayout());

		this.budget = budget;
		initWindow(); // Initialisierung des Frameinhalts
		addBehavior(); // Verhalten der GUI Elemente dieses Frames
		addRow(tableModel); // Die Tabelle um eine Zeile erweitern
		saveTable(tableModel, budget);

		sorttable(tableModel, table);
		diagrammAuswahl();
		showKonto();
		deleteRow(tableModel);
		setBounds(10, 10, 800, 800); // Groesse des Frames
		setVisible(true); // Frame wird sichtbar

	}

	// Initialisieren des Fensters
	protected void initWindow() {

		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}

		// Tabelle mit Uebersicht der Ausgaben
		data = new Object[budget.ausgaben.size()][4];
		int i = 0;
		for (Posten p : budget.ausgaben) {
			data[i][0] = new SimpleDateFormat("yyyy.MM.dd")
					.format(p.getDatum());
			data[i][1] = p.getBezeichnung();
			// data[i][2] = String.format("%.2f", p.getBetrag());
			data[i][2] = p.getBetrag();
			data[i][3] = p.getKategorie();
			i++;

		}

		// Add row to table
		tableModel = new MyTableModel(data, new Object[] { "Datum",
				"Bezeichnung", "Betrag", "Einnahme/Ausgabe" });

		table = new JTable(tableModel);

		einAusgabeColumn = table.getColumnModel().getColumn(3);
		einAusgabeCombobox = new JComboBox();
		einAusgabeCombobox.addItem("Einnahme");
		einAusgabeCombobox.addItem("Ausgabe");
		einAusgabeColumn
				.setCellEditor(new DefaultCellEditor(einAusgabeCombobox));

		table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		scrollpane = new JScrollPane(table);
		
		// Kreisdiagramm
		pdEinnahme = new DefaultPieDataset();
		pdAusgabe = new DefaultPieDataset();

		for (Posten p : budget.ausgaben) {
			if (p.getKategorie().equals("Einnahme"))
				pdEinnahme.setValue(p.getBezeichnung(), p.getBetrag());
			if (p.getKategorie().equals("Ausgabe"))
				pdAusgabe.setValue(p.getBezeichnung(), p.getBetrag());
		}

		// Für Einnahmen
		JFreeChart pie = ChartFactory.createPieChart("Einnahme", pdEinnahme);
		panelEinnahme = new ChartPanel(pie);

		// Für Ausgaben
		JFreeChart pie2 = ChartFactory.createPieChart("Ausgaben", pdAusgabe);
		panelAusgabe = new ChartPanel(pie2);

		// Button
		button = new JButton("TestButton!");
		button.setBounds(300, 110, 150, 40);

		// AddPosten Button
		addPosten = new JButton("+ Add Posten!");
		addPosten.setBounds(300, 110, 150, 40);

		// SAve Table Button
		saveTable = new JButton("Save Table!");
		saveTable.setBounds(300, 110, 150, 40);

		// DeletePosten Button
		deletePosten = new JButton("- Delete Posten!");
		deletePosten.setBounds(300, 110, 150, 40);
		// DeletePosten Button
		showkontostand = new JButton("Kontostand zeigen!");
		showkontostand.setBounds(300, 110, 150, 100);

		// Dropliste für Diagramm
		DiagrammAuswahl = new JComboBox();
		DiagrammAuswahl.addItem("Diagramm auswählen");
		DiagrammAuswahl.addItem("Diagramm für Einnahmen");
		DiagrammAuswahl.addItem("Diagramm für Ausgaben");

		// Elemente dem Fenster hinzufuegen:
		getContentPane().add(scrollpane);
		JPanel buttonContailer = new JPanel();
		buttonContailer.setLayout(new BoxLayout(buttonContailer,BoxLayout.PAGE_AXIS));
		
		buttonContailer.add(addPosten);
		buttonContailer.add(deletePosten);
		buttonContailer.add(saveTable);
		buttonContailer.add(DiagrammAuswahl);
		buttonContailer.add(showkontostand);
		buttonContailer.add(button);
		getContentPane().add(buttonContailer);
		
//		getContentPane().add(addPosten);
//		getContentPane().add(deletePosten);
//		getContentPane().add(saveTable);
//		getContentPane().add(DiagrammAuswahl);
//		getContentPane().add(showkontostand);
		// getContentPane().add(panelEinnahme);
		// getContentPane().add(panelAusgabe);
//		getContentPane().add(button);

		// Berechnet Layout mit geringstem Platzbedarf
		pack();
	}

	// Verhalten hinzufuegen
	public void addBehavior() {
		// registriere den ActionListener fuer den Button als anonyme Klasse
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(BudgetPlanGUI.this,
						"Add Posten!", "Hinweis", JOptionPane.PLAIN_MESSAGE);

			}

		});
	}

	// Diagrammauswahl
/**
 * DiagramAuswahl: 
 */
	public void diagrammAuswahl() {
		// registriere den ActionListener fuer den Button als anonyme Klasse
		DiagrammAuswahl.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();

				if (cb.getSelectedIndex() == 1) {
					if ( getContentPane().getComponentCount()>0)
						getContentPane().remove(panelAusgabe);
					getContentPane().add(panelEinnahme);
					printAll(getGraphics());
				}
				if (cb.getSelectedIndex() == 2) {
					if ( getContentPane().getComponentCount()>0)
						getContentPane().remove(panelEinnahme);
					getContentPane().add(panelAusgabe);
					printAll(getGraphics());
				}
			}
		});
	}

	// Tabelle um eine Zeile Erweitern hinzufuegen
	public void addRow(final DefaultTableModel tableModel) {
		// registriere den ActionListener fuer den Button als anonyme Klasse
		addPosten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableModel.addRow(new Object[] { "YYYY/MM/DD", "Bezeichnung",
						00.00, "wähle" });

				table.putClientProperty("terminateEditOnFocusLost", true);
			}

		});
	}

	// Eine Zeile in einer Tabelle löschen
	public void deleteRow(final DefaultTableModel tableModel) {
		// registriere den ActionListener fuer den Button als anonyme Klasse
		deletePosten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if (row != -1) {
					System.out.println("Selecter row : " + row);
					// remove selected row from the model
					tableModel.removeRow(row);

				}
				budget.ausgaben.remove(row);

				//
				CSVWriter writer = null;
				String[] line = new String[4];
				String str;
				try {
					writer = new CSVWriter(new FileWriter("data/budget.csv"),
							'#', CSVWriter.NO_QUOTE_CHARACTER);

					int i = 0;
					for (Posten p : budget.ausgaben) {
						//

						line[0] = new SimpleDateFormat("dd.MM.yyyy").format(p
								.getDatum());
						line[1] = p.getBezeichnung();
						line[2] = Double.toString(p.getBetrag());
						// line[2] = String.format("%.2f", p.getBetrag());
						line[3] = p.getKategorie().toString();

						writer.writeNext(line);
						i++;

					}

					writer.close();

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		});
	}

	// Tabelle sortieren
	public void sorttable(final MyTableModel tableModel, final JTable table) {
		TableRowSorter<MyTableModel> sorter = new TableRowSorter<MyTableModel>();
		table.setRowSorter(sorter);
		sorter.setModel(tableModel);

	}

	// Tabelle speichern
	public void saveTable(final DefaultTableModel tableModel,
			final BudgetPlanModel budget) {
		// registriere den ActionListener fuer den Button als anonyme Klasse
		saveTable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Object[][] tmp = new Object[data.length+1][4];
				System.out.println("Anzahl der Zeilen = "
						+ tableModel.getRowCount());
				int lastRow = tableModel.getRowCount();

				DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,
						Locale.GERMAN);
				Date datum = null;
				String dat = (String) tableModel.getValueAt(lastRow - 1, 0);
				try {
					datum = df.parse(dat);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String bezeichnung = (String) tableModel.getValueAt(
						lastRow - 1, 1);
				// double betrag = Double.parseDouble( (String)
				// tableModel.getValueAt(lastRow - 1, 2));
				double betrag = (double) tableModel.getValueAt(lastRow - 1, 2);
				String kategorie = (String) tableModel.getValueAt(lastRow - 1,
						3);

				budget.ausgaben.add(new Posten(datum, bezeichnung, betrag,
						kategorie));

				CSVWriter writer = null;
				String[] line = new String[4];
				String str;
				try {
					writer = new CSVWriter(new FileWriter("data/budget.csv"),
							'#', CSVWriter.NO_QUOTE_CHARACTER);

					NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
					int i = 0;
					for (Posten p : budget.ausgaben) {
						//

						line[0] = new SimpleDateFormat("dd.MM.yyyy").format(p
								.getDatum());
						line[1] = p.getBezeichnung();
						// line[2] = ;
						line[2] = Double.toString(p.getBetrag());
						// line[2] = String.format("%.2f", p.getBetrag());
						line[3] = p.getKategorie().toString();

						writer.writeNext(line);
						i++;

					}

					writer.close();

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		});
	}

	public void showKonto() {
		// registriere den ActionListener fuer den Button als anonyme Klasse
		showkontostand.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(
						BudgetPlanGUI.this,
						"                             "
								+ String.format("%.2f", budget.getKontostand())
								+ " Euro", "Kontostand",
						JOptionPane.PLAIN_MESSAGE);

			}
		});

	}

	class MyTableModel extends DefaultTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MyTableModel(Object rowData[][], Object columnNames[]) {
			super(rowData, columnNames);
		}

		@Override
		public Class getColumnClass(int col) {
			if (col == 2) // second column accepts only Integer values
				return Double.class;

			else
				return String.class; // other columns accept String values
		}

	}
}