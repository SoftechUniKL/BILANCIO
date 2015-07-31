package gui;

import java.sql.Date;


import javax.swing.table.DefaultTableModel;

/**
 * Klasse zur Erweiterung der DefaultTableModel
 * 
 * @author Abraham Amanuel
 * @author Asmelash Samuel
 * @author Lamberty Tim
 */

public class MyTableModel extends DefaultTableModel {

	Object rowData[][];
	Object columnNames[];

	public MyTableModel() {

	}

	private static final long serialVersionUID = 1L;

	public MyTableModel(Object rowData[][], Object columnNames[]) {
		super(rowData, columnNames);
	}
/**
 * Festlegung welcher Datentyp in welche Spalte kommt
 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int col) {

		if (col == 0)
			return Date.class;
		if (col == 3)
			return Double.class;
		if (col == 5)
			return Integer.class;

		else
			// andere Spalten akzeprieren nur Stringwerte
			return String.class; 
	}
/**
 * Zellen einer Tabelle sind editierbar
 */
	public boolean isCellEditable(int row, int column){
		// Tabellenzeilen sind nicht editierbar
		return false;
	}
}
