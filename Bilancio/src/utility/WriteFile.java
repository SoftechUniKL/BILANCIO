package utility;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import model.Posten;

import com.opencsv.CSVWriter;

public class WriteFile implements Observer {
	
	String fileName;
	
	public List<Posten> posten;
	
	public WriteFile(String fileName, List<Posten> posten){
		
		this.fileName = fileName;
		
		this.posten = posten;
		
	}
	
	public void writeDataIntoFile(){
		
		String nameOfFile = fileName;
		
		 CSVWriter writer = null;
			String[] line = new String[5];
			
			try {
				writer = new CSVWriter(new FileWriter(nameOfFile), '#', CSVWriter.NO_QUOTE_CHARACTER);
				
				for (Posten p : this.posten) {

					line[0] = new SimpleDateFormat("dd.MM.yyyy").format(p.getDatum());
					line[1] = p.getKategorie().toString();
					line[2] = p.getBezeichnung() ;
					line[3] = Double.toString( p.getBetrag());
					line[4] = p.getTransaktionsart().toString();
					
					writer.writeNext(line);
				}
				writer.close();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}


	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		System.out.println(this.toString() +" : Message from Model  = "+(String ) arg);
		
		writeDataIntoFile();
	}

}
