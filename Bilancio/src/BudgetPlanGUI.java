import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

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
	private DefaultTableModel tableModel;
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
		initWindow(); 		// Initialisierung des Frameinhalts
		addBehavior(); 		// Verhalten der GUI Elemente dieses Frames
		addRow(tableModel); 			// Die Tabelle um eine Zeile erweitern 
		saveTable(tableModel, budget);
		setBounds(10, 10, 800, 800); // Groesse des Frames
		setVisible(true); 		// Frame wird sichtbar
	}

	// Initialisieren des Fensters
	protected void initWindow() {
		
		try
		{
		  UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch(Exception e){}

		// Tabelle mit Uebersicht der Ausgaben
		 data = new Object[budget.ausgaben.size()][4];
		int i = 0;
		for (Posten p : budget.ausgaben) {
			data[i][0] = new SimpleDateFormat("dd.MM.yyyy")
					.format(p.getDatum());
			data[i][1] = p.getBezeichnung();
			data[i][2] = String.format("%.2f", p.getBetrag());
			data[i][3] = p.getKategorie();
			i++;
			
		}
		
		
		// Add row to table
		tableModel = new DefaultTableModel(data,new Object[]{"Datum", "Bezeichnung",
				"Betrag" ,  "Fix/Variable"});
		table = new JTable(tableModel);
		scrollpane = new JScrollPane(table);
		
		
		

		// Kreisdiagramm
		DefaultPieDataset pd = new DefaultPieDataset();
		for (Posten p : budget.ausgaben) {
			pd.setValue(p.getBezeichnung(), p.getBetrag());
		}
		JFreeChart pie = ChartFactory.createPieChart("Ausgaben", pd);
		ChartPanel panel = new ChartPanel(pie);
		
		// Piechart 3D
		JFreeChart pie2 = ChartFactory.createPieChart3D("Ausgaben", pd);
		ChartPanel panel2 = new ChartPanel(pie2);

		// Button
		button = new JButton("TestButton!");
		button.setBounds(300,110,100,30);
		
		// AddPosten Button 
		addPosten = new JButton("Add Posten!");
		addPosten.setBounds(300,110,100,30);
		
		//SAve Table Button 
		saveTable = new JButton("Save Table!");
		saveTable.setBounds(300,110,100,30);

		// Elemente dem Fenster hinzufuegen:
		getContentPane().add(scrollpane);
		getContentPane().add(addPosten);
		getContentPane().add(saveTable);
		getContentPane().add(panel);
		getContentPane().add(panel2);
		getContentPane().add(button);
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
						"Add Posten!",
						"Hinweis", JOptionPane.PLAIN_MESSAGE);
			}

		});
	}
	
	// Tabelle um eine Zeile Erweitern hinzufuegen
		public void addRow(final DefaultTableModel tableModel) {
			// registriere den ActionListener fuer den Button als anonyme Klasse
			addPosten.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					tableModel.addRow(new Object[]{"Column 1", "Column 2", "Column 3","Column 4"});
				}

			});
		}
		
	// Tabelle speichern
	public void saveTable(final DefaultTableModel tableModel, final BudgetPlanModel budget) {
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
				double betrag = Double.parseDouble((String) tableModel
						.getValueAt(lastRow - 1, 2));
				String kategorie = (String) tableModel.getValueAt(lastRow - 1,
						3);

				budget.ausgaben.add(new Posten(datum, bezeichnung, betrag,
						kategorie));

				CSVWriter writer = null;
				String[] line = new String[4];
				String str;
				try {
					writer = new CSVWriter(new FileWriter("data/myfile.csv"), '#', CSVWriter.NO_QUOTE_CHARACTER);
					
					int i = 0;
					for (Posten p : budget.ausgaben) {
						//

						line[0] = new SimpleDateFormat("dd.MM.yyyy").format(p.getDatum());
						line[1] = p.getBezeichnung() ;
						//line[2] = Double.toString( p.getBetrag());
						line[2] = String.format("%.2f", p.getBetrag());
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

}