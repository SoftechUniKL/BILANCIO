package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import model.BudgetPlanModel;
import model.IBudgetPlanModel;
import model.Posten;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import utility.DateLabelFormatter;

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
	
	private static Object[] selectedObject;
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
	private JButton prognose;


	static private DefaultPieDataset pdEinnahme;
	static private DefaultPieDataset pdAusgabe;
	static private DefaultCategoryDataset pdAusgabeBar;
	private JFreeChart pieChartEinnahme;
	private JFreeChart pieChartAusgabe;
	private JFreeChart barAusgabe;
	private ChartPanel panelEinnahme;
	private ChartPanel panelAusgabe;
	private ChartPanel panelPrognose;

	static JMenuItem addPostenMenu;

	static JMenuItem deletePostenMenu;

	static private JDatePickerImpl datePicker1;


	static private JDatePickerImpl datePicker2;

	static private JPanel contentPanel;
	static private JPanel controlPanel;
	static private JPanel kontostandPanel;
	private static JButton kontostandBtn;
	private static JLabel labelKontostand;
	private static JRadioButton rButtonFilterEin;
	private static JRadioButton rButtonFilterAus;
	
	// Log ausgabe ein =1 / aus = 0
	private static boolean log = false;

	/**
	 * Modell der Daten
	 */
	static public IBudgetPlanModel budget;

	/**
	 * Konstruktor fuer die GUI.
	 * 
	 * Hier wird das Hauptfenster initialisiert und aktiviert.
	 * 
	 * @param budget
	 *            Modell der Daten
	 */
	public BudgetPlanGUI(IBudgetPlanModel budget) {

		super("BILANCIO");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(true);
		
		

		getContentPane().setLayout(new FlowLayout());

		getContentPane().setBackground(Color.WHITE);
		kontostandPanel = new JPanel();
		kontostandPanel.setPreferredSize(new Dimension(680, 40));

		
		controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(680, 40));
		
		contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setPreferredSize(new Dimension(680, 600));
		
		JLabel label = new JLabel("Öffne eine  CSV Datei.");
		contentPanel.add(label, BorderLayout.NORTH);


		getContentPane().add(controlPanel);
		getContentPane().add(contentPanel);

		if (budget != null) {
			BudgetPlanGUI.budget = budget;
			budget.addObserver(this);
		}
		

		initWindow(); // Initialisierung des Frameinhalts
		

		showKonto();

	//	showPrognose();


		filterDate();

		setBounds(10, 10, 700, 800); // Groesse des Frames
		setVisible(true); // Frame wird sichtbar

	}
	/**
	 * Konstruktor für das GUI
	 */

	public BudgetPlanGUI() {

		super("BILANCIO");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getContentPane().setLayout(new FlowLayout());
		getContentPane().setBackground(Color.WHITE);

		kontostandPanel = new JPanel();
		kontostandPanel.setPreferredSize(new Dimension(700, 50));
		
		controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(700, 50));
		
		
		contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setPreferredSize(new Dimension(700, 500));
		
		JLabel label = new JLabel("Öffe eine  CSV Datei.");
		contentPanel.add(label);
		
		
		getContentPane().add(kontostandPanel);
		getContentPane().add(controlPanel);
		getContentPane().add(contentPanel);

		initWindow();
		setVisible(true); // Frame wird sichtbar
		

	}

	/**
	 * Initialiserung des Fensters
	 */
	
	protected void initWindow() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		UtilDateModel model1 = new UtilDateModel();
		UtilDateModel model2 = new UtilDateModel();
		Properties propperty = new Properties();
		propperty.put("text.today", "Today");
		propperty.put("text.month", "Month");
		propperty.put("text.year", "Year");

		JDatePanelImpl datePane1 = new JDatePanelImpl(model1, propperty);
		datePicker1 = new JDatePickerImpl(datePane1, new DateLabelFormatter());
		

		JDatePanelImpl datePane2 = new JDatePanelImpl(model2, propperty);
		datePicker2 = new JDatePickerImpl(datePane2, new DateLabelFormatter());
		

		controlPanel.add(datePicker1);
		controlPanel.add(datePicker2);

		
		/**
		 * MENU
		 * 
		 *  			**************************************************** //
		 */
		
		JMenuBar menubar = new JMenuBar();

		this.setJMenuBar(menubar);

		JMenu datei = new JMenu("Datei");
		datei.setMnemonic(KeyEvent.VK_D);
		JMenuItem dateiOeffnen = new JMenuItem("Datei öffnen");
		//JMenuItem dateiSpeichern = new JMenuItem("Datei speichern");
		JMenuItem exit = new JMenuItem("Programm beenden");
		datei.add(dateiOeffnen);
		//datei.add(dateiSpeichern);
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

		
		JMenu about = new JMenu("About");
		about.setMnemonic(KeyEvent.VK_A);
		
		menubar.add(about);
		
		/**
		 * ActionListener für "about" 
		 * @author Tim
		 *
		 */
		
		class About implements MenuListener {

			@Override
			public void menuSelected(MenuEvent arg0) {
				
				String about = "Dieses Programm wurde im Rahmen des Programmierprojekts entwickelt.\n"
						+ "Die Entwicklergruppe bestand aus Amanuel Abraham, Samuel Asmelash und \n"
						+ "Tim Lamberty. Die Betreuung des Projekts lag bei Frau Dr. Bieniusa (AG Softwaretechnik).\n";
				
				JOptionPane.showMessageDialog(BudgetPlanGUI.this,
						about, "BILANCIO", JOptionPane.PLAIN_MESSAGE);
				
			}

			@Override
			public void menuCanceled(MenuEvent e) {
			}

			@Override
			public void menuDeselected(MenuEvent e) {
			}
		}
		
		about.addMenuListener(new About());
		
		/**
		 * 
		 * ActionListener für Programm-Beenden
		 *
		 */
		class exitaction implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);

			}
		}

		exit.addActionListener(new exitaction());
		
		/**
		 * ActionListener für die verschiedenen Prognosemonate
		 * @author Tim
		 *
		 */

		class Prognose implements ActionListener {
			int k;

			public Prognose(int k) {
				this.k = k;
			}

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {

				int prognosePeriode = k; // Prognose-Periode in Monate
				
				double kontostand = budget.getKontostand();
				if(log)
				System.out.println("Prognose für die nächste" + prognosePeriode
						+ " Monate : "
						+ budget.getPrognose(prognosePeriode));
				
						double pp = budget.getPrognose(prognosePeriode)-kontostand;
						
						if(log) System.out.println("pp;"+pp);
						
				// Chart für Prognose
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				for (int i = 0; i <= prognosePeriode; i++) {

					dataset.addValue(kontostand + (i) * (pp/prognosePeriode),
							"Kontostand", i  + ".");

				}

				JFreeChart lineChart = ChartFactory.createLineChart("Prognose",
						"Monate", "EURO", dataset, PlotOrientation.VERTICAL,
						true, true, false);

				panelPrognose = new ChartPanel(lineChart);
				panelPrognose
						.setPreferredSize(new java.awt.Dimension(700, 367));
				
				//Do not show Control Panel
				getContentPane().getComponent(1).show(false);

				((JPanel) getContentPane().getComponent(2)).removeAll();
				((JPanel) getContentPane().getComponent(2)).add(panelPrognose,BorderLayout.PAGE_START);
				
				double vprognose = budget.getPrognose(prognosePeriode);
				String vprognoseStr =  String.format(Locale.GERMANY, "%.2f", vprognose);
				
				JLabel labelprognose = new JLabel ("  Voraussichtlicher Kontostand nach " + prognosePeriode + " Monaten: EUR " +  vprognoseStr);
				labelprognose.setPreferredSize(new java.awt.Dimension(10,30));
				labelprognose.setFont(labelKontostand.getFont().deriveFont(16.0f));
				
				labelprognose.setOpaque(true);
				if(vprognose>0) labelprognose.setBackground(Color.GREEN);
				if(vprognose<0)  labelprognose.setBackground(Color.RED);
					
				((JPanel) getContentPane().getComponent(2)).add(labelprognose, BorderLayout.SOUTH);
				
				getContentPane().getComponent(2).revalidate();
							
			}
		}
		
		// Prognose Periode = 3 Monate
		dreiMonate.addActionListener(new Prognose(3));
		// Prognose Periode = 6 Monate
		sechsMonate.addActionListener(new Prognose(6));
		// Prognose Periode = 9 Monate
		neunMonate.addActionListener(new Prognose(9));
		// Prognose Periode = 12 Monate
		zwölfMonate.addActionListener(new Prognose(12));
		
		/**
		 * Actionlistener für Einnahmen Diagramm
		 * @author Tim
		 *
		 */

		class eingabenDiagramm implements ActionListener {
			public void actionPerformed(ActionEvent e) {

				getDataEinnahme();
				
				// Do not show Control Panel
				getContentPane().getComponent(1).show(false);

				((JPanel) getContentPane().getComponent(2)).removeAll();
				((JPanel) getContentPane().getComponent(2)).add(panelEinnahme);

				getContentPane().getComponent(1).revalidate();
			}

		}

		diagrammEinnahmen.addActionListener(new eingabenDiagramm());
		
		/** 
		 * ActionListener für Tabellenübersicht
		 * @author Tim
		 *
		 */

		class übersichtTabelle implements ActionListener {
			public void actionPerformed(ActionEvent e) {

				((JPanel) getContentPane().getComponent(2)).removeAll();
				((JPanel) getContentPane().getComponent(2)).add(scrollpane);

				getContentPane().getComponent(1).show(true);
				getContentPane().getComponent(2).revalidate();

			}

		}
		tabellenÜbersicht.addActionListener(new übersichtTabelle());
		
		/**
		 * ActionListener für Ausgabediagramm
		 * @author Tim
		 *
		 */

		class ausgabenDiagramm implements ActionListener {
			public void actionPerformed(ActionEvent e) {

				//getDataAusgabe();
				getDataAusgabeBAR();
				
				// Do not show Control Panel
				getContentPane().getComponent(1).show(false);
				
				((JPanel) getContentPane().getComponent(2)).removeAll();
				((JPanel) getContentPane().getComponent(2)).add(panelAusgabe);
				getContentPane().getComponent(1).revalidate();
			}

		}

		diagrammAusgaben.addActionListener(new ausgabenDiagramm());
		
		/** 
		 * ActionListener zum Hinzufügen einer Transaktion
		 * @author Tim
		 *
		 */

		class addPosten implements ActionListener {
			public void actionPerformed(ActionEvent e) {

				new EingabeMaske();
				EingabeMaske.budget = budget;
				EingabeMaske.tableModel = BudgetPlanGUI.tableModel;
				getContentPane().getComponent(2).revalidate();

			}

		}

		addPostenMenu.addActionListener(new addPosten());
		
		/**
		 * Actionlistener zum Löschen einer Transaktion
		 * @author Tim
		 *
		 */

		class deletePosten implements ActionListener {
			
			public void actionPerformed(ActionEvent e) {
				if(log)
				System.out.println("class deletePosten : Anzahl der Posten, vor Löschen: "
						+ budget.getSize());
				if(log)
				System.out.println("Deleted Object : " + selectedObject[5]);
				
				int i= 0;
				for (Posten p : budget.getTransaction()) {
					
					int keyTable = Integer.parseInt( selectedObject[5].toString());
					
					int keyPosten =  p.getKey();
									
					if(keyTable == keyPosten) {
						if(log)
						System.out.println("Key  in file : " + keyPosten);
						budget.removeAusgabe(keyPosten);
						budget.tell("A Transaction has been deleted.");
						break;
					}
		
					else
						i=i+1;

				}
				if(log)
				System.out.println("Anzahl der Posten, nach Löschen "
						+ budget.getSize());

				deletePostenMenu.setEnabled(false);
			}
		}

		deletePostenMenu.addActionListener(new deletePosten());
		
		/**
		 * ActionListener zum Öffnen der CSV Datei
		 * @author Tim
		 *
		 */

		class openFile implements ActionListener {
			public void actionPerformed(ActionEvent e) {

				JFileChooser fc = new JFileChooser();

				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"CSV files", "csv", "txt");
				fc.showOpenDialog(null);
				File file = fc.getSelectedFile();
				fc.addChoosableFileFilter(filter);

				BudgetPlanModel myBudget = new BudgetPlanModel(file.toString());
				BudgetPlanGUI gui = new BudgetPlanGUI(myBudget);
				gui.setTitle("BILANCIO: " + file.getName());
				updateTableFromModel(myBudget);

				if(log)
				System.out.println(file);

			}
		}

		dateiOeffnen.addActionListener(new openFile());

		// updateTableFromModel(null);
		if(log)
		System.out.println("Firts time updateTableFromModel called.");

		// Kreisdiagramm für Ausgaben
		pieChartAusgabe = ChartFactory.createPieChart("Ausgaben", pdAusgabe);
		panelAusgabe = new ChartPanel(pieChartAusgabe);
		
//		// Balkendiagram für Ausgaben
//				barAusgabe = ChartFactory.createBarChart("Ausgabe", "", "EUR", pdAusgabeBar);
//				panelAusgabe = new ChartPanel(barAusgabe);
		
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

		// Button für Prognose
		prognose = new JButton("       Prognose        ");

		// Chart für Prognose
		// TODO: Durch Werte aus der Datei ersetzen.
		// Elemente dem Fenster hinzufuegen:

		rButtonFilterEin = new JRadioButton("Ein");
		rButtonFilterEin.setActionCommand("Ein");

		rButtonFilterAus = new JRadioButton("Aus");
		rButtonFilterAus.setActionCommand("Aus");
		rButtonFilterAus.setSelected(true);

		ButtonGroup groupFilter = new ButtonGroup();
		groupFilter.add(rButtonFilterEin);
		groupFilter.add(rButtonFilterAus);

		controlPanel.add(rButtonFilterEin);
		controlPanel.add(rButtonFilterAus);
		
		kontostandBtn = new JButton("    Kontostand zeigen   ");
		kontostandBtn.setPreferredSize(new Dimension(200, 30));
		kontostandPanel.add(kontostandBtn,BorderLayout.WEST);
		
		labelKontostand = new JLabel("KONTOSTAND");
		labelKontostand.setPreferredSize(new Dimension(470, 30));
		
		labelKontostand.setHorizontalAlignment(SwingConstants.RIGHT);
		labelKontostand.setFont(labelKontostand.getFont().deriveFont(20.0f));
		
		kontostandPanel.add(labelKontostand,BorderLayout.EAST);

		getContentPane().add(kontostandPanel);

		getContentPane().add(controlPanel);
		getContentPane().add(contentPanel);
		
		JPanel buttonContailer = new JPanel();
		buttonContailer.setLayout(new BoxLayout(buttonContailer,
				BoxLayout.PAGE_AXIS));

		buttonContailer.add(addPosten);
		buttonContailer.add(deletePosten);
		buttonContailer.add(saveTable);
		buttonContailer.add(button);
		buttonContailer.add(prognose);

		pack();

		
	}

	

	EingabeMaske pMaske = null;

	String removedPosten;


	
	/**
	 * Spalte der Betragshöhe wird von String in Double umgewandelt und sortierbar gemacht
	 * @param tableModel
	 * 		TableModel
	 * @param table
	 * 		Table
	 */
	public void sorttable(final MyTableModel tableModel, final JTable table) {
		TableRowSorter<MyTableModel> sorter = new TableRowSorter<MyTableModel>();
		
		
		table.setRowSorter(sorter);
		sorter.setModel(tableModel);
		
		sorter.setComparator(3, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				 NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);
				 
				
				double x = 0;
				double y = 0;
				try {
					x =  nf.parse(o1).doubleValue();
					y  =  nf.parse(o2).doubleValue();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return (int) (y-x);
			}
			
			
			
		});

	}
/**
 * Zeigt Kontostand an, falls positiv=grüner Hintergrund, negativ=roter Hintergrund
 */

	public void showKonto() {
		// registriere den ActionListener fuer den Button als anonyme Klasse
		kontostandBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (budget !=null) {
				
				String kntoStd = String.format(Locale.GERMANY, "%.2f", budget.getKontostand());

				labelKontostand.setText("EUR "+ kntoStd);

				if (budget.getKontostand() >= 0)

					kontostandPanel.setBackground(Color.GREEN);
				else

					kontostandPanel.setBackground(Color.RED);
				
				}

			}
		});

	}
	
	

	/**
	 * Änderungen des Modells im GUI anzeigen
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		if(log)
		System.out.println(this.toString() + " : Message from Model  = "
				+ (String) arg1);

		updateTableFromModel(null);
		

	}
	
	/**
	 * Erzeugnung der Tabelle im GUI auf Basis des Modells
	 * @param model
	 * Model
	 */

	public void updateTableFromModel(IBudgetPlanModel model) {

		// tableModel.addRow(new Object[5]);
		if (model != null)
			budget = model;
		if(log)
		System.out.println("Update Table From Model has been called.");
		if(log)
		System.out.println("Budget list = " + budget.getSize());

		// Tabelle mit Uebersicht der Ausgaben
		data = new Object[budget.getSize()][6];
		extractTransactionData(data, budget.getTransaction());
		

		// Add row to table
		final MyTableModel tableModel = new MyTableModel(data, new Object[] {
				"Datum", "Kategorie", "Bezeichnung", "Betrag",
				"Transaktionsart", "Schlüssel" });
		
		
		final JTable table = new JTable(tableModel) {

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

		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

		sorttable(tableModel, table);
		table.setRowSelectionAllowed(true);
		
		table.getColumnModel().getColumn(5).setMinWidth(0);
		table.getColumnModel().getColumn(5).setMaxWidth(0);

		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {

						int selectedRow = table.getSelectedRow();
						int selectedRowModel = table.convertRowIndexToModel(selectedRow);
						
						if (selectedRow > -1) {
							deletePostenMenu.setEnabled(true);
							if(log)
							System.out.println("Selected row in table mmm: "
									+ selectedRow);
						
						 selectedObject = new Object[tableModel.getColumnCount()];
						 
						 if(log)
						 System.out.println("tableModel.rowData  : " +  tableModel.getColumnCount());
							
							for(int i=0 ; i<selectedObject.length;i++){
								
								selectedObject[i] = (Object) table.getModel().getValueAt(selectedRowModel, i).toString();
								
								if(log)
								System.out.println("Obj : " + i+". "+ selectedObject[i]);
									
							}

						}
						else 
							deletePostenMenu.setEnabled(false);

					}
				});

		table.setRowHeight(25);

		table.getTableHeader().setOpaque(false);
		table.getTableHeader().setBackground(Color.GRAY);
		table.getTableHeader().setForeground(Color.WHITE);

		table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		scrollpane = new JScrollPane(table);

		if(log)
		System.out.println("Table länge = " + table.getRowCount());

		contentPanel.removeAll();
		contentPanel.add(scrollpane);
		printAll(getGraphics());

	}
	
	/**
	 * Ausgabe-Daten fürKreisdiagram aufbereiten
	 */

	private void getDataAusgabe() {
		pdAusgabe = new DefaultPieDataset();
		for (Posten p : budget.getTransaction()) {

			if (p.getTransaktionsart().equals("Ausgabe"))
				pdAusgabe.setValue(p.getBezeichnung(), p.getBetrag());

		}

		pieChartAusgabe = ChartFactory.createPieChart("Ausgaben", pdAusgabe);
		panelAusgabe = new ChartPanel(pieChartAusgabe);

	}
	
	/**
	 * Ausgabe-Daten für Balkendiagramm aufbereiten
	 */
	private void getDataAusgabeBAR() {
		pdAusgabeBar = new DefaultCategoryDataset();
		
		double sum [] = new double[8];
		
		String bez[] = new String[] { "Miete", "Lebensmittel",
				"Versicherungen", "Freizeit", "Hobbys", "Bildung",
				"Kredit" ,"Sonstiges"};
		
		for (Posten p : budget.getTransaction()) {
			
			String kategory = p.getKategorie().toLowerCase();
			
			switch (kategory) {
			case "miete":
				sum [0] += p.getBetrag();
				break;

			case "lebensmittel":
				sum [1] += p.getBetrag();
				break;

			case "versicherungen":
				sum [2] += p.getBetrag();
				break;

			case "freizeit":
				sum [3] += p.getBetrag();
				break;

			case "hobbys":
				sum [4] += p.getBetrag();
				break;
			case "bildung":
				sum [5] += p.getBetrag();
				break;

			case "kredit":
				sum [6] += p.getBetrag();
				break;
			
			case "sonstiges":
				sum [7] += p.getBetrag();
				break;

			default:
				;
				break;
			}

		}
		
	
		
		for (int i=0; i< sum.length; i++)
			pdAusgabeBar.setValue(sum[i], "EUR", bez[i]);

		// Balkendiagram für Ausgaben
		barAusgabe = ChartFactory.createBarChart("Übersicht der Ausgabekategorien",  "Kategorien","EUR" , pdAusgabeBar);
		panelAusgabe = new ChartPanel(barAusgabe);
						

	}
	
	/**
	 * Einnahme-Daten für Kreisdiagramm aufbereiten
	 */

	private void getDataEinnahme() {
		pdEinnahme = new DefaultPieDataset();

		double sum[] = new double [4]; 
		String kat[] = new String[] { "Gehalt", "Geschenk", "Zinsen",
		"sonstige" };
		
		
		for (Posten p : budget.getTransaction()) {
			String kategory = p.getKategorie().toLowerCase();
			
			if(log)
			System.out.println(kategory);
			switch (kategory) {
			case "gehalt":
				sum [0] += p.getBetrag();
				break;

			case "geschenk":
				sum [1] += p.getBetrag();
				break;

			case "zinsen":
				sum [2] += p.getBetrag();
				break;

			case "sonstiges":
				sum [3] += p.getBetrag();
				break;

			default:
				sum [3] += p.getBetrag();
				break;
			}

		}
		
		for (int i=0; i< sum.length; i++)
			pdEinnahme.setValue(kat[i],sum[i]);
		
		
		
		// Für Einnahmen
		pieChartEinnahme = ChartFactory.createPieChart("Einnahme", pdEinnahme);

		panelEinnahme = new ChartPanel(pieChartEinnahme);
	}

	
	/**
	 * Daten, die nur innerhalb der eingestellten Datumfilter leigen, 
	 * werden herausgefiltert und dargestellt
	 */
	public void filterDate() {

		rButtonFilterEin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Date date1, date2 = new Date();
				date1 = (Date) datePicker1.getModel().getValue();
				date2 = (Date) datePicker2.getModel().getValue();

				if (rButtonFilterEin.isSelected()) {
					
					if(log)
					System.out.println("Filter on.");
					if(log)
					System.out.println("Date 1"
							+ datePicker1.getModel().getValue());
					if(log)
					System.out.println("Date 2"
							+ datePicker2.getModel().getValue());

					updateTableForFilterdeData(budget, date1, date2);

				}

			}

		});

		rButtonFilterAus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (rButtonFilterAus.isSelected()) {
					if(log)
					System.out.println("Filter off.");
					updateTableFromModel(budget);
				}
			}
		});

	}
	
	/**
	 * Update der Tabelle mit den gefilterten Transaktionen
	 * @param model
	 * Das Modell
	 * @param selectedDate1
	 * Anfagnsdatum
	 * @param selectedDate2
	 * Enddatum
	 */

	public void updateTableForFilterdeData(IBudgetPlanModel model,
			Date selectedDate1, Date selectedDate2) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(selectedDate1);
		cal.add(Calendar.DATE, -1);

		selectedDate1 = cal.getTime();

		if (model != null)
			budget = model;

		if(log)
		System.out.println("Update Table From Model has been called. FILTER");
		if(log)
		System.out.println("Budget list = " + budget.getSize());

		// Tabelle mit Uebersicht der Ausgaben

		List<Posten> tran = new ArrayList<Posten>();

		for (Posten p : budget.getTransaction()) {
			int after = selectedDate2.compareTo(p.getDatum());
			int before = selectedDate1.compareTo(p.getDatum());

			if ((before < 0) && (after > 0)) {
				tran.add(p);
			}
		}

		data = new Object[budget.getSize()][6];
		extractTransactionDataForFilter(data, tran);

		// Add row to table
		final MyTableModel tableModel = new MyTableModel(data, new Object[] {
				"Datum", "Kategorie", "Bezeichnung", "Betrag",
				"Transaktionsart" , "Schlüssel"});

		final JTable table = new JTable(tableModel) {

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
		
		table.getColumnModel().getColumn(5).setMinWidth(0);
		table.getColumnModel().getColumn(5).setMaxWidth(0);
		
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						// String selectedData = null;

						int selectedRow = table.getSelectedRow();
						if (selectedRow > -1) {
							deletePostenMenu.setEnabled(true);
							if(log)
							System.out.println("Selected row in table mmm: "
									+ selectedRow);
						
						
						 selectedObject = new Object[tableModel.getColumnCount()];
						 
						 if(log)
						 System.out.println("tableModel.rowData  : " +  tableModel.getColumnCount());
							
							for(int i=0 ; i<selectedObject.length;i++){
								
								selectedObject[i] = (Object) table.getModel().getValueAt(selectedRow, i).toString();

								if(log)	
								System.out.println("Obj : " + i+". "+ selectedObject[i]);
								
							}

						}
						else 
							deletePostenMenu.setEnabled(false);

					}
				});

		table.setRowHeight(25);

		table.getTableHeader().setOpaque(false);
		table.getTableHeader().setBackground(Color.GRAY);
		table.getTableHeader().setForeground(Color.WHITE);

		table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		scrollpane = new JScrollPane(table);

		if(log)
		System.out.println("Table länge = " + table.getRowCount());

		contentPanel.removeAll();
		contentPanel.add(scrollpane);
		printAll(getGraphics());

	}
	/**
	 * Liste der Transaktion wird in eine Liste Data-Objekt kopiert. 
	 * @param data
	 * 		Liste der kopierten Daten
	 * @param tran
	 * 		Liste der Transaktionen
	 */

	private void  extractTransactionData(Object data[][], List<Posten> tran) {
		int i = 0;
		for (Posten p : tran) {

			data[i][0] = p.getDatum();
			data[i][1] = p.getKategorie();
			data[i][2] = p.getBezeichnung();
			data[i][3] = String.format("%.2f", p.getBetrag());
			data[i][4] = p.getTransaktionsart();
			data[i][5] = p.getKey();

			i++;
		}
		
	}
	
	/**
	 * Liste der Transaktion wird in eine Liste Data-Objekt kopiert. 
	 * @param data
	 * 		Liste der kopierten Daten
	 * @param tran
	 * 		Liste der Transaktionen
	 */
	
	private void  extractTransactionDataForFilter(Object data[][], List<Posten> tran) {
		int i = 0;
		for (Posten p : tran) {

			data[i][0] = p.getDatum();
			data[i][1] = p.getKategorie();
			data[i][2] = p.getBezeichnung();
			data[i][3] = p.getBetrag();
			data[i][4] = p.getTransaktionsart();
			data[i][5] = p.getKey();

			i++;
		}
		
	}
	
	
}