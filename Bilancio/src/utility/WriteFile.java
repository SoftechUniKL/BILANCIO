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
	/** 
	 * Name der CSV Datei
	 */
	
	String fileName;
	/**
	 * Liste der Transaktionen
	 */
	
	public List<Posten> posten;
	
	private static boolean log = false;
	
	/**
	 * Konstruktor für WriteFile
	 * @param fileName
	 * Name der Datei
	 * @param posten
	 * Name der Transaktionen
	 */
	public WriteFile(String fileName, List<Posten> posten){
		
		this.fileName = fileName;
		
		this.posten = posten;
		
	}
	
	/**
	 * Transaktionen werden aus dem Modell in die CSV Datei geschrieben
	 */
	
	public void writeDataIntoFile(){
		
		String nameOfFile = fileName;
		
		 CSVWriter writer = null;
			String[] line = new String[6];
			
			try {
				writer = new CSVWriter(new FileWriter(nameOfFile), '#', CSVWriter.NO_QUOTE_CHARACTER);
				
				for (Posten p : this.posten) {

					line[0] = new SimpleDateFormat("dd.MM.yyyy").format(p.getDatum());
					line[1] = p.getKategorie().toString();
					line[2] = p.getBezeichnung() ;
					line[3] = Double.toString( p.getBetrag());
					line[4] = p.getTransaktionsart().toString();
					line[5] = Integer.toString(p.getKey());
					
					writer.writeNext(line);
				}
				writer.close();
				
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
	}

/**
 * Update für das Programm falls eine Änderung der Transaktionenliste stattfindet
 */
	@Override
	public void update(Observable o, Object arg) {
		
		if(log)
		System.out.println(this.toString() +" : Message from Model  = "+(String ) arg);
		
		writeDataIntoFile();
	}

}
