package gui;

import javax.swing.table.DefaultTableModel;

public class MyTableModel extends DefaultTableModel{
	
	Object rowData[][];
	Object columnNames[];

	public MyTableModel() {
		// TODO Auto-generated constructor stub
	}

		private static final long serialVersionUID = 1L;

		public MyTableModel(Object rowData[][], Object columnNames[]) {
			super(rowData, columnNames);
		}

		@Override
		public Class getColumnClass(int col) {
			if (col == 3) // second column accepts only Integer values
				return Double.class;

			else
				return String.class; // other columns accept String values
		}

	}


