package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.time.temporal.JulianFields;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import model.BudgetPlanModel;
import model.Posten;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.DefaultXYDataset;

import probe.ProbeMaske;
import probe.ProbeMaske2;
import utility.WriteFile;

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
	public static MyTableModel tableModel;
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
	private JButton prognose;
	private TableColumn einAusgabeColumn;
	private JComboBox einAusgabeCombobox;
	private JComboBox DiagrammAuswahl;
	private DefaultPieDataset pdEinnahme;
	private DefaultPieDataset pdAusgabe;
	private JFreeChart pieChartEinnahme;
	private JFreeChart pieChartAusgabe;
	private ChartPanel panelEinnahme;
	private ChartPanel panelAusgabe;
	private   ChartPanel panelPrognose;
	private int row;
	
	static private JTextField  testTextField;

	/**
	 * Modell der Daten
	 */
	static public BudgetPlanModel budget;

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

		BudgetPlanGUI.budget = budget;
//		 this.budget = new BudgetPlanModel();
		
		initWindow(); // Initialisierung des Frameinhalts
		addBehavior(); // Verhalten der GUI Elemente dieses Frames
		addPosten(tableModel); // Die Tabelle um eine Zeile erweitern
		saveTable(tableModel, budget);

		sorttable(tableModel, table);
		diagrammAuswahl();
		showKonto();
		showPrognose();
		saveTableChange ();
		deletePosten(tableModel);
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
		data = new Object[budget.ausgaben.size()][5];
		int i = 0;
		for (Posten p : budget.ausgaben) {
			data[i][0] = new SimpleDateFormat("yyyy.MM.dd")
					.format(p.getDatum());
			data[i][2] = p.getBezeichnung();
			// data[i][2] = String.format("%.2f", p.getBetrag());
			data[i][3] = p.getBetrag();
			data[i][1] = p.getKategorie();
			data[i][4]= p.getTransaktionsart();
			i++;

		}

		// Add row to table
		tableModel = new MyTableModel(data, new Object[] { "Datum", "Kategorie",
				"Bezeichnung", "Betrag", "Transaktionsart" });

		table = new JTable(tableModel){

			/**
			 *  Tabelle für die Liste der Transaktionenen
			 */
			private static final long serialVersionUID = 1L;
			
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
			{
				Component c = super.prepareRenderer(renderer, row, column);

				//  Alternate row color

				if (!isRowSelected(row))
					c.setBackground(row % 2 == 0 ? getBackground() : Color.LIGHT_GRAY);

				return c;
			}
			
		};
		
		table.getTableHeader().setOpaque(false);
		table.getTableHeader().setBackground(Color.GRAY);
		table.getTableHeader().setForeground(Color.WHITE);
		
		einAusgabeColumn = table.getColumnModel().getColumn(4);
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
			if (p.getTransaktionsart().equals("Einnahme"))
				pdEinnahme.setValue(p.getBezeichnung(), p.getBetrag());
			if (p.getTransaktionsart().equals("Ausgabe"))
				pdAusgabe.setValue(p.getBezeichnung(), p.getBetrag());
		}

		// Für Einnahmen
		pieChartEinnahme = ChartFactory.createPieChart("Einnahme", pdEinnahme);
		panelEinnahme = new ChartPanel(pieChartEinnahme);

		// Für Ausgaben
		pieChartAusgabe = ChartFactory.createPieChart("Ausgaben", pdAusgabe);
		panelAusgabe = new ChartPanel(pieChartAusgabe);

		// Button
		button = new JButton("TestButton!");
		button.setBounds(300, 110, 150, 40);

		// AddPosten Button
		addPosten = new JButton(" +    Add Posten       ");			
		addPosten.setBounds(300, 110, 150, 40);

		// SAve Table Button
		saveTable = new JButton("       Save Table        ");
		saveTable.setBounds(300, 110, 150, 40);

		// DeletePosten Button
		deletePosten = new JButton(" -     Delete Posten   ");
		
								      
		deletePosten.setBounds(300, 110, 150, 40);
		// DeletePosten Button
		showkontostand = new JButton("    Kontostand zeigen   ");
		showkontostand.setBounds(300, 110, 150, 40);

		
		// Dropliste für Diagramm
		DiagrammAuswahl = new JComboBox();
		DiagrammAuswahl.addItem("Diagramm auswählen");
		DiagrammAuswahl.addItem("Diagramm für Einnahmen");
		DiagrammAuswahl.addItem("Diagramm für Ausgaben");

		// Button für Prognose
	    prognose = new JButton("       Prognose        ");
	    
	    // Chart für Prognose
	    //TODO: Durch Werte aus der Datei ersetzen.
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
	    dataset.addValue( -30 , "Kontostand" , "1. Mon" );
	    dataset.addValue( -60 , "Kontostand" , "2. Mon" );
	    dataset.addValue( -90 , "Kontostand" , "3. Mon" );
	    dataset.addValue( -120 , "Kontostand" , "4. Mon" );
	    JFreeChart lineChart = ChartFactory.createLineChart(
	            "Prognose",
	            "Monate","EURO",
	            dataset,
	            PlotOrientation.VERTICAL,
	            true,true,false);
	            
	          panelPrognose = new ChartPanel( lineChart );
	         panelPrognose.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
		
		// Elemente dem Fenster hinzufuegen:
		getContentPane().add(scrollpane);
		JPanel buttonContailer = new JPanel();
		buttonContailer.setLayout(new BoxLayout(buttonContailer,
				BoxLayout.PAGE_AXIS));

		buttonContailer.add(addPosten);
		buttonContailer.add(deletePosten);
		buttonContailer.add(saveTable);
		buttonContailer.add(DiagrammAuswahl);
		buttonContailer.add(showkontostand);
		buttonContailer.add(button);
		buttonContailer.add(prognose);
		
		buttonContailer.add(testTextField);
		
		getContentPane().add(buttonContailer);
	
		//getContentPane().add(prognosePanel);

		// getContentPane().add(addPosten);
		// getContentPane().add(deletePosten);
		// getContentPane().add(saveTable);
		// getContentPane().add(DiagrammAuswahl);
		// getContentPane().add(showkontostand);
		// getContentPane().add(panelEinnahme);
		// getContentPane().add(panelAusgabe);
		// getContentPane().add(button);

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

	public void saveTableChange () {
		// Änderung der einzelnen Zeilen in der Tabelle werden direkt übernommen 
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		    public void valueChanged(ListSelectionEvent event) {
		     boolean evt =	event.getValueIsAdjusting();
		     int selectedRow = table.getSelectedRow();
		        if ( selectedRow >-1 && evt==false) {
		            // print first column value from selected row
		        	System.out.println("Ausgewählte Zeile:" + table.getSelectedRow());
		            System.out.print(table.getValueAt(table.getSelectedRow(), 0).toString());
		            System.out.print("\t"+ table.getValueAt(table.getSelectedRow(), 1).toString());
		            System.out.print("\t"+table.getValueAt(table.getSelectedRow(), 2).toString());
		            System.out.print("\t"+table.getValueAt(table.getSelectedRow(), 3).toString());
		            System.out.println("\t"+table.getValueAt(table.getSelectedRow(), 4).toString());
		            
		        }
		        
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
					if (getContentPane().getComponentCount() > 0){
						getContentPane().remove(panelAusgabe);
						getContentPane().remove(panelPrognose);
					}
					getContentPane().add(panelEinnahme);
					printAll(getGraphics());
				}
				if (cb.getSelectedIndex() == 2) {
					if (getContentPane().getComponentCount() > 0){
						getContentPane().remove(panelEinnahme);
						getContentPane().remove(panelPrognose);
					}
					getContentPane().add(panelAusgabe);
					printAll(getGraphics());
				}
			}
		});
	}

	// Tabelle um eine Zeile Erweitern hinzufuegen
	public void addPosten(final DefaultTableModel tableModel) {
		// registriere den ActionListener fuer den Button als anonyme Klasse
		addPosten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				tableModel.addRow(new Object[] { "YYYY/MM/DD", "Kategorie", "Bezeichnung",
//						00.00, "wähle" });
//
//				table.putClientProperty("terminateEditOnFocusLost", true);
				
				System.out.println("Anzahl der Zeilen vor update = "+table.getRowCount());
				System.out.println("Anzahl der Liste vor update = "+budget.ausgaben.size());
				//final EingabeMaske pMaske = new EingabeMaske (budget, (MyTableModel) tableModel);
				final EingabeMaske pMaske = new EingabeMaske ();
				pMaske.budget = budget;
				pMaske.tableModel = BudgetPlanGUI.tableModel;
				
				/*tableModel.addRow(new Object[5]);
				int lastRow =tableModel.getRowCount();
				*/
				
				/*
				pMaske.tfDatum.getText();
//				pMaske.cbKategorieAusgabe.getSelectedIndex();
				pMaske.tfBezeichnung.getText();
				pMaske.Betrag.doubleValue();
				pMaske.transaktionsArt.toString();
				*/
				
				/*
				tableModel.setValueAt(55, lastRow-1, 3);
				tableModel.setValueAt(55, lastRow-1, 3);
				tableModel.setValueAt(55, lastRow-1, 3);
				
				*/
				
//
				table.putClientProperty("terminateEditOnFocusLost", true);
				
				updateTable( pMaske);
				
				System.out.println("Anzahl der Zeilen nach update = "+table.getRowCount());
				System.out.println("Anzahl der Liste nach update = "+budget.ausgaben.size());
				
				getContentPane().repaint();
			}

		});
	}

	String removedPosten;

	// Eine Zeile in einer Tabelle löschen
	public void deletePosten(final DefaultTableModel tableModel) {
		// registriere den ActionListener fuer den Button als anonyme Klasse
		deletePosten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if (row != -1) {
					System.out.println("Selecter row : " + row);
					// was wurde gelöscht: einnahme ode ausgabe
					removedPosten = (String) tableModel.getValueAt(row, 4);
					System.out.println("removedPosten   "+removedPosten);
					// remove selected row from the model
					tableModel.removeRow(row);

				}

				// PieChart für Einnahme
				// pdEinnahme-inhalt vor löschen
				System.out.println("Vor dem Löschen: ");
				System.out.println("Einnahmen= " + pdEinnahme.getKeys());
				System.out.println("Ausgaben= " + pdAusgabe.getKeys());

				// Posten aus der Liste löschen
				budget.ausgaben.remove(row);

				// TODO: bessere Lösung finden
				// PieChart leeren
				if (removedPosten.equals("Einnahme")) {

					pdEinnahme.clear();
					panelEinnahme.removeAll();
					panelEinnahme.revalidate();

					// Daten für PieChart aus der Tabelle lesen
					for (Posten p : budget.ausgaben) {
						if (p.getTransaktionsart().equals("Einnahme"))
							pdEinnahme.setValue(p.getBezeichnung(),
									p.getBetrag());
					}

					panelEinnahme.repaint();
				}

				if (removedPosten.equals("Ausgabe")) {
					pdAusgabe.clear();
					panelAusgabe.removeAll();
					panelAusgabe.revalidate();

					// Daten für PieChart aus der Tabelle lesen
					for (Posten p : budget.ausgaben) {
						if (p.getTransaktionsart().equals("Ausgabe"))
							pdAusgabe.setValue(p.getBezeichnung(),
									p.getBetrag());
					}

					panelAusgabe.repaint();

				}

				// TODO: nach dem Test löschen
				// pdEinnahme-inhalt nach löschen
				System.out.println("Nach dem Löschen: ");
				System.out.println("Einahmen: " + pdEinnahme.getKeys());
				System.out.println("Ausgaben: " + pdAusgabe.getKeys());

				// in einer datei schreiben
				//();budget.writeDataIntoFile
				//WriteFile wFile = new WriteFile(budget.filename, budget.ausgaben);
				
				budget.tell("A Transaction has been deleted.");
				// CSVWriter writer = null;
				// String[] line = new String[4];
				// String str;
				// try {
				// writer = new CSVWriter(new FileWriter("data/budget.csv"),
				// '#', CSVWriter.NO_QUOTE_CHARACTER);
				//
				// int i = 0;
				// for (Posten p : budget.ausgaben) {
				// //
				//
				// line[0] = new SimpleDateFormat("dd.MM.yyyy").format(p
				// .getDatum());
				// line[1] = p.getBezeichnung();
				// line[2] = Double.toString(p.getBetrag());
				// // line[2] = String.format("%.2f", p.getBetrag());
				// line[3] = p.getKategorie().toString();
				//
				// writer.writeNext(line);
				// i++;
				//
				// }
				//
				// writer.close();
				//
				// } catch (IOException e1) {
				// // TODO Auto-generated catch block
				// e1.printStackTrace();
				// }

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
						lastRow - 1, 2);
				// double betrag = Double.parseDouble( (String)
				// tableModel.getValueAt(lastRow - 1, 2));
				double betrag = (double) tableModel.getValueAt(lastRow - 1, 3);
				String kategorie = (String) tableModel.getValueAt(lastRow - 1,
						1);
				String Transaktionsart = (String) tableModel.getValueAt(lastRow - 1,
						4);

				budget.ausgaben.add(new Posten(datum, kategorie, bezeichnung, betrag,
						Transaktionsart));
				
				budget.tell("Save Table.");

//				CSVWriter writer = null;
//				String[] line = new String[5];
//				String str;
//				try {
//					writer = new CSVWriter(new FileWriter("data/budget.csv"),
//							'#', CSVWriter.NO_QUOTE_CHARACTER);
//
//					NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
//					int i = 0;
//					for (Posten p : budget.ausgaben) {
//						//
//
//						line[0] = new SimpleDateFormat("dd.MM.yyyy").format(p
//								.getDatum());
//						line[2] = p.getBezeichnung();
//						// line[2] = ;
//						line[3] = Double.toString(p.getBetrag());
//						// line[2] = String.format("%.2f", p.getBetrag());
//						line[1] = p.getKategorie().toString();
//						line [4]= p.getTransaktionsart();
//
//						writer.writeNext(line);
//						i++;
//
//					}
//
//					writer.close();
//
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}

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


	private void updateTable(EingabeMaske pMaske){
		
		// Tabelle mit Uebersicht der Ausgaben
		BudgetPlanModel budget = EingabeMaske.budget;
				data = new Object[budget.ausgaben.size()][5];
				int i = 0;
				for (Posten p : budget.ausgaben) {
					data[i][0] = new SimpleDateFormat("yyyy.MM.dd")
							.format(p.getDatum());
					data[i][2] = p.getBezeichnung();
					// data[i][2] = String.format("%.2f", p.getBetrag());
					data[i][3] = p.getBetrag();
					data[i][1] = p.getKategorie();
					data[i][4]= p.getTransaktionsart();
					i++;

				}

				// Add row to table
				tableModel = new MyTableModel(data, new Object[] { "Datum", "Kategorie",
						"Bezeichnung", "Betrag", "Transaktionsart" });

				table = new JTable(tableModel);

				einAusgabeColumn = table.getColumnModel().getColumn(4);
				einAusgabeCombobox = new JComboBox();
				einAusgabeCombobox.addItem("Einnahme");
				einAusgabeCombobox.addItem("Ausgabe");
				einAusgabeColumn
						.setCellEditor(new DefaultCellEditor(einAusgabeCombobox));

				table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
				
				
				
				scrollpane = new JScrollPane(table);
				System.out.println("Tabele wurde gerade geupdatet: Zeilenanzahl = " + tableModel.getRowCount());
	}
	
	public void showPrognose() {
		// registriere den ActionListener fuer den Button als anonyme Klasse
		prognose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
					if (getContentPane().getComponentCount() > 0)
						getContentPane().remove(panelAusgabe);
					getContentPane().add(panelPrognose);
					printAll(getGraphics());
			
				
					if (getContentPane().getComponentCount() > 0)
						getContentPane().remove(panelEinnahme);
					getContentPane().add(panelPrognose);
					printAll(getGraphics());
					
					System.out.println("Prognose für die nächste 12 Monate : "+ budget.getPrognose(budget.ausgaben, 12));
				
			}
		});
	}

	
}