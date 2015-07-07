package gui;

import java.sql.Date;

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
			
			if (col == 0)
				return  Date.class;
			if (col == 3) // second column accepts only Integer values
				return Double.class;

			else
				return String.class; // other columns accept String values
		}
		
		
		    public boolean isCellEditable(int row, int column)      //override isCellEditable
		    //PRE:  row > 0, column > 0
		    //POST: FCTVAL == false always
		    {
		        return false;
		    }
		}

	

