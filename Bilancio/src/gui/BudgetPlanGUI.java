package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.time.temporal.JulianFields;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Scanner;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import model.BudgetPlanModel;
import model.Posten;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
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
import utility.DateLabelFormatter;
import utility.WriteFile;

import com.opencsv.CSVWriter;

/**
 * Graphische Benutzeroberflaeche des BudgetPlaners
 * 
 */
public class BudgetPlanGUI extends JFrame implements Observer {
	private static final long serialVersionUID = 1L;
	/**
	 * Tabelle mit Uebersicht der Ausgaben
	 */
	private static JTable table;
	public static MyTableModel tableModel;
	private Object[][] data;
	/**
	 * Scrollelemente, das die Tabelle umfasst
	 */
	private static JScrollPane scrollpane;
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
	static private DefaultPieDataset pdEinnahme;
	static private DefaultPieDataset pdAusgabe;
	private JFreeChart pieChartEinnahme;
	private JFreeChart pieChartAusgabe;
	private ChartPanel panelEinnahme;
	private ChartPanel panelAusgabe;
	private ChartPanel panelPrognose;
	private int row;
	static JMenuItem addPostenMenu;
	
	static JMenuItem deletePostenMenu;

	static private JTextField testTextField;
	static private Date selectedDate1; 
	static private JDatePickerImpl datePicker1;
	
	static private Date selectedDate2; 
	static private JDatePickerImpl datePicker2;
	
	static private JPanel contentPanel ;
	static private JPanel controlPanel ;

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
	public BudgetPlanGUI(BudgetPlanModel budget)  {

		super("BILANCIO");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.setResizable(false);
	
		getContentPane().setLayout(new FlowLayout());
		
		getContentPane().setBackground(Color.WHITE);
		
		contentPanel = new JPanel ();
		controlPanel= new JPanel ();
		contentPanel.setLayout(new FlowLayout());
		//TODO : Delete after testing
		contentPanel.setBackground(Color.BLUE);
		
		getContentPane().add(controlPanel);
		getContentPane().add(contentPanel);
		
		if (budget != null){
		BudgetPlanGUI.budget = budget;
		budget.addObserver(this);
		}
		// this.budget = new BudgetPlanModel();

		initWindow(); // Initialisierung des Frameinhalts
		addBehavior(); // Verhalten der GUI Elemente dieses Frames
		//addPosten(tableModel); // Die Tabelle um eine Zeile erweitern
		//saveTable(tableModel, budget);

		//sorttable(tableModel, table);
		diagrammAuswahl();
		showKonto();
		showPrognose();
		// saveTableChange ();
		deletePosten();
		setBounds(10, 10, 800, 800); // Groesse des Frames
		setVisible(true); // Frame wird sichtbar

	}
	
	public BudgetPlanGUI()  {

		super("BILANCIO");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.setResizable(false);
	
		getContentPane().setLayout(new FlowLayout());
		
		getContentPane().setBackground(Color.WHITE);
		
		contentPanel = new JPanel ();
		controlPanel= new JPanel ();
		contentPanel.setLayout(new FlowLayout());
		//TODO : Delete after testing
		contentPanel.setBackground(Color.BLUE);
		
		getContentPane().add(controlPanel);
		getContentPane().add(contentPanel);
		
		initWindow();
		
		setVisible(true); // Frame wird sichtbar

	}
	
	

	// Initialisieren des Fensters
	protected void initWindow() {

		try {
			   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		
		UtilDateModel model1 = new UtilDateModel();
		UtilDateModel model2 = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		
		JDatePanelImpl datePane1 = new JDatePanelImpl(model1, p);
		datePicker1 = new JDatePickerImpl(datePane1, new DateLabelFormatter());
		selectedDate1 = new Date();
		
		JDatePanelImpl datePane2 = new JDatePanelImpl(model2, p);
		datePicker2 = new JDatePickerImpl(datePane2, new DateLabelFormatter());
		selectedDate2 = new Date();
		
		controlPanel.add(datePicker1);
		controlPanel.add(datePicker2);
		
		JMenuBar menubar = new JMenuBar();

		this.setJMenuBar(menubar);

		JMenu datei = new JMenu("Datei");
		datei.setMnemonic(KeyEvent.VK_D);
		JMenuItem dateiOeffnen = new JMenuItem("Datei öffnen");
		JMenuItem dateiSpeichern = new JMenuItem("Datei speichern");
		JMenuItem exit = new JMenuItem("Programm beenden");
		datei.add(dateiOeffnen);
		datei.add(dateiSpeichern);
		datei.add(exit);

		menubar.add(datei);

		JMenu ansicht = new JMenu("Ansicht");
		ansicht.setMnemonic(KeyEvent.VK_A);
		JMenuItem tabellenÜbersicht = new JMenuItem("Tabellenansicht");
		JMenuItem diagrammAusgaben = new JMenuItem("Ausgabendiagramm anzeigen");
		JMenuItem diagrammEinnahmen = new JMenuItem(
				"Einnahmendiagramm anzeigen");
		ansicht.add(diagrammEinnahmen);
		ansicht.add(diagrammAusgaben);
		ansicht.add(tabellenÜbersicht);

		menubar.add(ansicht);

		JMenu posten = new JMenu("Posten");
		posten.setMnemonic(KeyEvent.VK_P);
		addPostenMenu = new JMenuItem("Posten hinzufügen");
		deletePostenMenu = new JMenuItem("Posten löschen");
		deletePostenMenu.setEnabled(false);
		posten.add(addPostenMenu);
		posten.add(deletePostenMenu);
		menubar.add(posten);

		JMenu prognosebutton = new JMenu("Prognose");
		prognosebutton.setMnemonic(KeyEvent.VK_R);
		JMenuItem dreiMonate = new JMenuItem("3 Monate");
		JMenuItem sechsMonate = new JMenuItem("6 Monate");
		JMenuItem neunMonate = new JMenuItem("9 Monate");
		JMenuItem zwölfMonate = new JMenuItem("12 Monate");
		prognosebutton.add(dreiMonate);
		prognosebutton.add(sechsMonate);
		prognosebutton.add(neunMonate);
		prognosebutton.add(zwölfMonate);

		menubar.add(prognosebutton);

		JMenu hilfe = new JMenu("Hilfe");
		hilfe.setMnemonic(KeyEvent.VK_H);
		JMenuItem about = new JMenuItem("Über uns");
		JMenuItem kontakt = new JMenuItem("Kontakt");
		hilfe.add(about);
		hilfe.add(kontakt);
		menubar.add(hilfe);

		class exitaction implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);

			}
		}

		exit.addActionListener(new exitaction());

		

		class Prognose implements ActionListener {
			int k;

			public Prognose(int k) {
				this.k = k;
			}

			public void actionPerformed(ActionEvent e) {

				int zeit = k; // monate
				// double prognose = budget.getPrognose(budget.ausgaben, zeit);
				double kontostand = budget.getKontostand();

				System.out.println("Prognose für die nächste" + zeit
						+ " Monate : "
						+ (kontostand + zeit * kontostand / zeit));

				// Chart für Prognose
				// TODO: Durch Werte aus der Datei ersetzen.
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				for (int i = 0; i < zeit; i++) {

					dataset.addValue(kontostand + (i + 1) * kontostand / zeit,
							"Kontostand", i + 1 + ".");

				}

				JFreeChart lineChart = ChartFactory.createLineChart("Prognose",
						"Monate", "EURO", dataset, PlotOrientation.VERTICAL,
						true, true, false);

				panelPrognose = new ChartPanel(lineChart);
				panelPrognose
						.setPreferredSize(new java.awt.Dimension(700, 367));


				((JPanel)getContentPane().getComponent(1)).removeAll(); 
				((JPanel)getContentPane().getComponent(1)).add(panelPrognose);
				getContentPane().getComponent(1).revalidate();
				
			}
		}
		
		 dreiMonate.addActionListener(new Prognose(3));
		sechsMonate.addActionListener(new Prognose(6));
		neunMonate.addActionListener(new Prognose(9));
		zwölfMonate.addActionListener(new Prognose(12));


		

		class eingabenDiagramm implements ActionListener {
			public void actionPerformed(ActionEvent e) {
		
				getDataEinnahme();
				
				((JPanel)getContentPane().getComponent(1)).removeAll(); 
				((JPanel)getContentPane().getComponent(1)).add(panelEinnahme);
				
				getContentPane().getComponent(1).revalidate();
			}

		}

		diagrammEinnahmen.addActionListener(new eingabenDiagramm());

		class übersichtTabelle implements ActionListener {
		 public void actionPerformed(ActionEvent e) {
			
				((JPanel)getContentPane().getComponent(1)).removeAll(); 
				((JPanel)getContentPane().getComponent(1)).add(scrollpane);
				
	getContentPane().getComponent(1).revalidate();
			

		 }

		}
		tabellenÜbersicht.addActionListener(new übersichtTabelle());

		class ausgabenDiagramm implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				
				getDataAusgabe();
				
				((JPanel)getContentPane().getComponent(1)).removeAll(); 
				((JPanel)getContentPane().getComponent(1)).add(panelAusgabe);
				
				
				getContentPane().getComponent(1).revalidate();
			}

		}

		diagrammAusgaben.addActionListener(new ausgabenDiagramm());

		class addPosten implements ActionListener {
			public void actionPerformed(ActionEvent e) {

				final EingabeMaske pMaske = new EingabeMaske();
				pMaske.budget = budget;
				pMaske.tableModel = BudgetPlanGUI.tableModel;

			
				//getContentPane().repaint();
				getContentPane().getComponent(1).revalidate();

			}

		}

		addPostenMenu.addActionListener(new addPosten());

		class enableDeletePosten implements ActionListener {
			public void actionPerformed(ActionEvent e) {

			}
		}

		class deletePosten implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Anzahl der Posten, vor Löschen" + budget.ausgaben.size());
				int row = table.getSelectedRow();
				if (row != -1) {
					System.out.println("Selected row : " + row);
					// was wurde gelöscht: einnahme ode ausgabe
					removedPosten = (String) tableModel.getValueAt(row, 4);
					System.out.println("removedPosten   " + removedPosten);
					// remove selected row from the model
					tableModel.removeRow(row);

				}

				// PieChart für Einnahme
				// pdEinnahme-inhalt vor löschen
//				System.out.println("Vor dem Löschen: ");
//				System.out.println("Einnahmen= " + pdEinnahme.getKeys());
//				System.out.println("Ausgaben= " + pdAusgabe.getKeys());

				// Posten aus der Liste löschen
				budget.ausgaben.remove(row);
				
				System.out.println("Anzahl der Posten, nach Löschen" + budget.ausgaben.size());

				// TODO: bessere Lösung finden
				// PieChart leeren
//				if (removedPosten.equals("Einnahme")) {
//
//					pdEinnahme.clear();
//					panelEinnahme.removeAll();
//					panelEinnahme.revalidate();
//
//					// Daten für PieChart aus der Tabelle lesen
//					for (Posten p : budget.ausgaben) {
//						if (p.getTransaktionsart().equals("Einnahme"))
//							pdEinnahme.setValue(p.getBezeichnung(),
//									p.getBetrag());
//					}
//
//					panelEinnahme.repaint();
//				}
//
//				if (removedPosten.equals("Ausgabe")) {
//					pdAusgabe.clear();
//					panelAusgabe.removeAll();
//					panelAusgabe.revalidate();
//
//					// Daten für PieChart aus der Tabelle lesen
//					for (Posten p : budget.ausgaben) {
//						if (p.getTransaktionsart().equals("Ausgabe"))
//							pdAusgabe.setValue(p.getBezeichnung(),
//									p.getBetrag());
//					}
//
//					panelAusgabe.repaint();
//
//				}
//
//				// TODO: nach dem Test löschen
//				// pdEinnahme-inhalt nach löschen
//				System.out.println("Nach dem Löschen: ");
//				System.out.println("Einahmen: " + pdEinnahme.getKeys());
//				System.out.println("Ausgaben: " + pdAusgabe.getKeys());
				

				budget.tell("A Transaction has been deleted.");

			}

		}

		deletePostenMenu.addActionListener(new deletePosten());
		
		class openFile implements ActionListener {
			public void actionPerformed(ActionEvent e) {

				JFileChooser fc = new JFileChooser();
				
				 FileNameExtensionFilter filter = new FileNameExtensionFilter( "CSV files", "csv","txt");
				 fc.showOpenDialog(null);
				 File file = fc.getSelectedFile();
				fc.addChoosableFileFilter(filter);
	
				BudgetPlanModel myBudget = new BudgetPlanModel(file.toString());				
				BudgetPlanGUI gui =new BudgetPlanGUI(myBudget);
				updateTableFromModel(myBudget);

				System.out.println(file);
				
			}

		}

		dateiOeffnen.addActionListener(new openFile());

		//updateTableFromModel(null);
		System.out.println("Firts time updateTableFromModel called.");


		// Kreisdiagramm
		

		// Für Ausgaben
		pieChartAusgabe = ChartFactory.createPieChart("Ausgaben", pdAusgabe);
		panelAusgabe = new ChartPanel(pieChartAusgabe);

		// Button
		button = new JButton("TestButton!");
		button.setBounds(300, 110, 150, 40);

		// AddPosten Button
		addPosten = new JButton(" +    Add Posten       ")
		;
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
		// TODO: Durch Werte aus der Datei ersetzen.
		// DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		// dataset.addValue(-30, "Kontostand", "1. Mon");
		// dataset.addValue(-60, "Kontostand", "2. Mon");
		// dataset.addValue(-90, "Kontostand", "3. Mon");
		// dataset.addValue(-120, "Kontostand", "4. Mon");
		// JFreeChart lineChart = ChartFactory.createLineChart("Prognose",
		// "Monate", "EURO", dataset, PlotOrientation.VERTICAL, true,
		// true, false);
		//
		// panelPrognose = new ChartPanel(lineChart);
		// panelPrognose.setPreferredSize(new java.awt.Dimension(560, 367));

		// Elemente dem Fenster hinzufuegen:
		
		getContentPane().add(controlPanel);
		getContentPane().add(contentPanel);
		//getContentPane().add(scrollpane);
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

		//getContentPane().add(buttonContailer);

		// Berechnet Layout mit geringstem Platzbedarf
		pack();
		
		System.out.println(getContentPane().getComponentCount());
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

	/*
	 * public void saveTableChange () { // Änderung der einzelnen Zeilen in der
	 * Tabelle werden direkt übernommen
	 * table.getSelectionModel().addListSelectionListener(new
	 * ListSelectionListener() { public void valueChanged(ListSelectionEvent
	 * event) { boolean evt = event.getValueIsAdjusting(); int selectedRow =
	 * table.getSelectedRow(); if ( selectedRow >-1 && evt==false) { // print
	 * first column value from selected row
	 * System.out.println("Ausgewählte Zeile:" + table.getSelectedRow());
	 * System.out.print(table.getValueAt(table.getSelectedRow(), 0).toString());
	 * System.out.print("\t"+ table.getValueAt(table.getSelectedRow(),
	 * 1).toString());
	 * System.out.print("\t"+table.getValueAt(table.getSelectedRow(),
	 * 2).toString());
	 * System.out.print("\t"+table.getValueAt(table.getSelectedRow(),
	 * 3).toString());
	 * System.out.println("\t"+table.getValueAt(table.getSelectedRow(),
	 * 4).toString());
	 * 
	 * }
	 * 
	 * } });
	 * 
	 * }
	 */

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
					if (getContentPane().getComponentCount() > 0) {
						getContentPane().remove(panelAusgabe);
						if (panelPrognose != null)
							getContentPane().remove(panelPrognose);

					}
					getContentPane().add(panelEinnahme);
					printAll(getGraphics());
				}
				if (cb.getSelectedIndex() == 2) {
					if (getContentPane().getComponentCount() > 0) {
						getContentPane().remove(panelEinnahme);
						if (panelPrognose != null)
							getContentPane().remove(panelPrognose);
					}
					getContentPane().add(panelAusgabe);
					printAll(getGraphics());
				}
			}
		});
	}

	 EingabeMaske pMaske = null;
	// Tabelle um eine Zeile Erweitern hinzufuegen
	public void addPosten(final DefaultTableModel tableModel) {
		// registriere den ActionListener fuer den Button als anonyme Klasse
		addPosten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// tableModel.addRow(new Object[] { "YYYY/MM/DD", "Kategorie",
				// "Bezeichnung",
				// 00.00, "wähle" });
				//
				// table.putClientProperty("terminateEditOnFocusLost", true);

				System.out.println("Anzahl der Zeilen vor update = "
						+ table.getRowCount());
				System.out.println("Anzahl der Liste vor update = "
						+ budget.ausgaben.size());
				// final EingabeMaske pMaske = new EingabeMaske (budget,
				// (MyTableModel) tableModel);
			
				pMaske = new EingabeMaske();
				pMaske.budget = budget;
				pMaske.tableModel = BudgetPlanGUI.tableModel;

				// table.putClientProperty("terminateEditOnFocusLost", true);

				pMaske.budget.tell("Posten has been added.");
				//updateTable(pMaske);

				System.out.println("Anzahl der Zeilen nach update = "
						+ table.getRowCount());
				System.out.println("Anzahl der Liste nach update = "
						+ budget.ausgaben.size());

				getContentPane().repaint();
			}

		});
	}

	String removedPosten;

	// Eine Zeile in einer Tabelle löschen
	public void deletePosten() {
		// registriere den ActionListener fuer den Button als anonyme Klasse
		deletePosten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if (row != -1) {
					System.out.println("Selecter row : " + row);
					// was wurde gelöscht: einnahme ode ausgabe
					removedPosten = (String) tableModel.getValueAt(row, 4);
					System.out.println("removedPosten   " + removedPosten);
					// remove selected row from the model
					tableModel.removeRow(row);

					// Posten aus der Liste löschen
					budget.ausgaben.remove(row);

					// PieChart für Einnahme
					// pdEinnahme-inhalt vor löschen
					System.out.println("Vor dem Löschen: ");
					System.out.println("Einnahmen= " + pdEinnahme.getKeys());
					System.out.println("Ausgaben= " + pdAusgabe.getKeys());

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
				}

				// TODO: nach dem Test löschen
				// pdEinnahme-inhalt nach löschen
				System.out.println("Nach dem Löschen: ");
				System.out.println("Einahmen: " + pdEinnahme.getKeys());
				System.out.println("Ausgaben: " + pdAusgabe.getKeys());

				budget.tell("A Transaction has been deleted.");

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
				String Transaktionsart = (String) tableModel.getValueAt(
						lastRow - 1, 4);

				budget.ausgaben.add(new Posten(datum, kategorie, bezeichnung,
						betrag, Transaktionsart));

				budget.tell("Save Table.");

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

	private void updateTable(EingabeMaske pMaske) {

		// Tabelle mit Uebersicht der Ausgaben
		BudgetPlanModel budget = EingabeMaske.budget;
		data = new Object[budget.ausgaben.size()][5];
		int i = 0;
		for (Posten p : budget.ausgaben) {
			data[i][0] = new SimpleDateFormat("dd.MM.yyyy")
					.format(p.getDatum());
			data[i][2] = p.getBezeichnung();
			// data[i][2] = String.format("%.2f", p.getBetrag());
			data[i][3] = p.getBetrag();
			data[i][1] = p.getKategorie();
			data[i][4] = p.getTransaktionsart();
			i++;

		}

		// Add row to table
		tableModel = new MyTableModel(data, new Object[] { "Datum",
				"Kategorie", "Bezeichnung", "Betrag", "Transaktionsart" });

		table = new JTable(tableModel);

		einAusgabeColumn = table.getColumnModel().getColumn(4);
		einAusgabeCombobox = new JComboBox();
		einAusgabeCombobox.addItem("Einnahme");
		einAusgabeCombobox.addItem("Ausgabe");
		einAusgabeColumn
				.setCellEditor(new DefaultCellEditor(einAusgabeCombobox));

		table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

		scrollpane = new JScrollPane(table);
		System.out.println("Tabele wurde gerade geupdatet: Zeilenanzahl = "
				+ tableModel.getRowCount());
	}

	public void showPrognose() {
		// registriere den ActionListener fuer den Button als anonyme Klasse
		prognose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				int zeit = 12; // monate
				// double prognose = budget.getPrognose(budget.ausgaben, zeit);
				double kontostand = budget.getKontostand();

				System.out.println("Prognose für die nächste 12 Monate : "
						+ (kontostand + zeit * kontostand / zeit));

				// Chart für Prognose
				// TODO: Durch Werte aus der Datei ersetzen.
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				for (int i = 0; i < zeit; i++) {

					dataset.addValue(kontostand + (i + 1) * kontostand / zeit,
							"Kontostand", i + 1 + ".");

				}

				JFreeChart lineChart = ChartFactory.createLineChart("Prognose",
						"Monate", "EURO", dataset, PlotOrientation.VERTICAL,
						true, true, false);

				panelPrognose = new ChartPanel(lineChart);
				panelPrognose
						.setPreferredSize(new java.awt.Dimension(700, 367));

				if (getContentPane().getComponentCount() > 0)
					getContentPane().remove(panelAusgabe);

				if (getContentPane().getComponentCount() > 0)
					getContentPane().remove(panelEinnahme);

				if (getContentPane().getComponent(0).equals(panelPrognose))
					getContentPane().remove(panelPrognose);

				getContentPane().add(panelPrognose);
				printAll(getGraphics());

			}
		});
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		System.out.println(this.toString() +" : Message from Model  = "+(String ) arg1);
		
		updateTableFromModel(null);
		
		
	}
	
	public void updateTableFromModel(BudgetPlanModel model){
		
		//tableModel.addRow(new Object[5]);
		if (model != null)
			budget = model;
		
		System.out.println("Update Table From Model has been called.");
		System.out.println("Budget list = " + budget.ausgaben.size());
		
		// Tabelle mit Uebersicht der Ausgaben
				data = new Object[budget.ausgaben.size()][5];
				int i = 0;
				for (Posten p : budget.ausgaben) {
					data[i][0] = new SimpleDateFormat("dd.MM.yyyy")
							.format(p.getDatum());
					data[i][2] = p.getBezeichnung();
					// data[i][2] = String.format("%.2f", p.getBetrag());
					data[i][3] = p.getBetrag();
					data[i][1] = p.getKategorie();
					data[i][4] = p.getTransaktionsart();
					i++;

				}

				// Add row to table
					tableModel = new MyTableModel(data, new Object[] { "Datum",
						"Kategorie", "Bezeichnung", "Betrag", "Transaktionsart" });

					table = new JTable(tableModel) {

					/**
					 * Tabelle für die Liste der Transaktionenen
					 */
					private static final long serialVersionUID = 1L;

					public Component prepareRenderer(TableCellRenderer renderer,
							int row, int column) {
						Component c = super.prepareRenderer(renderer, row, column);

						// Alternate row color

						if (!isRowSelected(row))
							c.setBackground(row % 2 != 0 ? getBackground()
									: Color.LIGHT_GRAY);

						return c;
					}

				};
				
				sorttable(tableModel, table);

				table.setRowSelectionAllowed(true);

				table.getSelectionModel().addListSelectionListener(
						new ListSelectionListener() {
							public void valueChanged(ListSelectionEvent e) {
								String selectedData = null;

								int selectedRow = table.getSelectedRow();
								if (selectedRow > -1) {
									deletePostenMenu.setEnabled(true);
									System.out.println("Selected row in table: " + selectedRow);
								} else

									deletePostenMenu.setEnabled(false);

							}

						});

				table.setRowHeight(25);

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
					
				
				
				
				
				System.out.println("Table länge = " + table.getRowCount());
				
//				getContentPane().removeAll();
//				getContentPane().add(scrollpane);
//				printAll(getGraphics());
				
				contentPanel.removeAll();
				contentPanel.add(scrollpane);
				printAll(getGraphics());
				
//				((JPanel)getContentPane().getComponent(1)).removeAll(); 
//				((JPanel)getContentPane().getComponent(1)).add(scrollpane);
//				((JPanel)getContentPane().getComponent(1)).printAll(getGraphics());
				
				
				
	}
	
	private void getDataAusgabe (){
		pdAusgabe = new DefaultPieDataset();
//		pieChartAusgabe = ChartFactory.createPieChart("Ausgaben", pdAusgabe);
//		panelAusgabe = new ChartPanel(pieChartAusgabe);
		
		for (Posten p : budget.ausgaben) {
			
			if (p.getTransaktionsart().equals("Ausgabe"))
				pdAusgabe.setValue(p.getBezeichnung(), p.getBetrag());
			
		}
		
		pieChartAusgabe = ChartFactory.createPieChart("Ausgaben", pdAusgabe);
		panelAusgabe = new ChartPanel(pieChartAusgabe);
		
	}
	
	private void getDataEinnahme(){
		pdEinnahme = new DefaultPieDataset();
		
		for (Posten p : budget.ausgaben) {
			if (p.getTransaktionsart().equals("Einnahme"))
				pdEinnahme.setValue(p.getBezeichnung(), p.getBetrag());
		
		}

		// Für Einnahmen
		pieChartEinnahme = ChartFactory.createPieChart("Einnahme", pdEinnahme);
		panelEinnahme = new ChartPanel(pieChartEinnahme);
		}
	
	 public static Scanner selectTextFile() {
		   do {
		      JFileChooser chooser = new JFileChooser();
	         FileNameExtensionFilter filter = new FileNameExtensionFilter(
	            "Text/Java files", "csv", "java");
	         chooser.setFileFilter(filter);
	         int returnVal = chooser.showOpenDialog(null);
				try {
	            if(returnVal == JFileChooser.APPROVE_OPTION) {
			         return new Scanner(chooser.getSelectedFile());
	            } 
	   		   else {
			         return null;
				   }
				}
				catch (FileNotFoundException e) {
				   JOptionPane.showMessageDialog(null, "Invalid file!",
					   "error", JOptionPane.ERROR_MESSAGE); 
				}
			} while (true);
		}

}