import java.awt.Color;
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

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
	/**
	 * Erweiterung der Klasse DefaultModelTable
	 */
	private MyTableModel tableModel;
	/**
	 * Daten eines Posten
	 */
	private Object[][] data;
	/**
	 * Scrollelemente, das die Tabelle umfasst
	 */
	private JScrollPane scrollpane;
	/**
	 * Schaltflaeche, die beim Klicken einen Dialog anzeigt
	 */
	private JButton button;
/**
 * Button um Posten hinzuzuf�gen
 */
	private JButton addPosten;
	/**
	 * Buttung um die Tabelle zu speichern
	 */
	private JButton saveTable;
	/**
	 * 
	 * Button um einen Posten aus der Tabelle zu l�schen
	 */
	private JButton deletePosten;
	/**
	 * Button um den aktuellen Kontosatnd zu zeigen
	 */
	private JButton showkontostand;

	/**
	 * Auswahlmen� f�r Einnahme und Ausgabe
	 */
	private TableColumn einAusgabeColumn;
	/**
	 * Texte f�r Auswahlmen�
	 */
	private JComboBox einAusgabeCombobox;

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
		addPosten(); // ein neuenPosten wird hinzugef�ht
		addRow(tableModel); // Die Tabelle um eine Zeile erweitern
		saveTable(tableModel, budget); // Bei Ver�nderung wird die Tabelle neu
										// gespeichert
		sorttable(tableModel, table); // Die Tabelle wird sortierbar gemacht
		showKonto(); // zeigt aktueller Kontostand an
		deleteRow(tableModel); // eine aufgew�hlte Zeiler wird aus der Tabelle
								// gel�scht
		setBounds(10, 10, 800, 800); // Groesse des Frames
		setVisible(true); // Frame wird sichtbar

	}

	
	/**
	 * Initialisieren des Fensters
	 */
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
			data[i][0] = new SimpleDateFormat("dd.MM.yyyy")
					.format(p.getDatum());
			data[i][1] = p.getBezeichnung();
			// data[i][2] = String.format("%.2f", p.getBetrag());
			data[i][2] = p.getBetrag();
			data[i][3] = p.getKategorie();
			i++;

		}

		// Add row to table
		tableModel = new MyTableModel(data, new Object[] { "Datum",
				"Bezeichnung", "Betrag", "Eingabe/Ausgabe" });

		table = new JTable(tableModel);

		// Ein ComboBox wird hinzugef�gt, f�r die Wahl zwichen Einnahme und
		// Ausgabe

		einAusgabeColumn = table.getColumnModel().getColumn(3);
		einAusgabeCombobox = new JComboBox();
		einAusgabeCombobox.addItem("Eingabe");
		einAusgabeCombobox.addItem("Ausgabe");
		einAusgabeColumn
				.setCellEditor(new DefaultCellEditor(einAusgabeCombobox));

		table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		scrollpane = new JScrollPane(table);

		// Kreisdiagramm
		DefaultPieDataset pd = new DefaultPieDataset();
		for (Posten p : budget.ausgaben) {
			pd.setValue(p.getBezeichnung(), p.getBetrag());
		}
		JFreeChart pie = ChartFactory.createPieChart("Ausgaben", pd);
		ChartPanel panel = new ChartPanel(pie);

		// Piechart 3D
		// JFreeChart pie2 = ChartFactory.createPieChart3D("Ausgaben", pd);
		// ChartPanel panel2 = new ChartPanel(pie2);

		// Button
		button = new JButton("TestButton!");
		button.setBounds(300, 110, 100, 30);

		// AddPosten Button
		addPosten = new JButton("Add Posten!");
		addPosten.setBounds(300, 110, 100, 30);

		// SAve Table Button
		saveTable = new JButton("Save Table!");
		saveTable.setBounds(300, 110, 100, 30);

		// DeletePosten Button
		deletePosten = new JButton("Delete Posten!");
		deletePosten.setBounds(300, 110, 100, 30);
		// DeletePosten Button
		showkontostand = new JButton("Kontostand zeigen!");
		showkontostand.setBounds(300, 110, 100, 30);

		// Elemente dem Fenster hinzufuegen:
		getContentPane().add(scrollpane);
		getContentPane().add(addPosten);
		getContentPane().add(deletePosten);
		getContentPane().add(saveTable);
		getContentPane().add(showkontostand);
		getContentPane().add(panel);
		// getContentPane().add(panel2);
		getContentPane().add(button);

		// Berechnet Layout mit geringstem Platzbedarf
		pack();
	}

	// Verhalten hinzufuegen
	public void addPosten() {
		// registriere den ActionListener fuer den Button als anonyme Klasse
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(BudgetPlanGUI.this,
						"Add Posten!", "Hinweis", JOptionPane.PLAIN_MESSAGE);
			}

		});
	}

	// Tabelle um eine Zeile Erweitern hinzufuegen
	public void addRow(final DefaultTableModel tableModel) {
		// registriere den ActionListener fuer den Button als anonyme Klasse
		addPosten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableModel.addRow(new Object[] { "dd/mm/yyyy", "Bezeichnung",
						00.00, "w�hle" });

				table.putClientProperty("terminateEditOnFocusLost", true);
			}

		});
	}

	// Eine Zeile in einer Tabelle l�schen
	public void deleteRow(final DefaultTableModel tableModel) {
		// registriere den ActionListener fuer den Button als anonyme Klasse
		deletePosten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if (row != -1) {
					System.out.println("Selected row : " + row);
					// remove selected row from the model
					tableModel.removeRow(row);

				}
				// remove the last row from the table
				budget.ausgaben.remove(row);
				// update file
				budget.writeDataIntoFile();
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
				budget.writeDataIntoFile();

			}

		});
	}

	/*
	 * Zeigt aktueller Kontostand an
	 */
	public void showKonto() {
		// registriere den ActionListener fuer den Button als anonyme Klasse
		showkontostand.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(
						BudgetPlanGUI.this,
						"                 "
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