package utility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import model.Posten;

import com.opencsv.CSVReader;

public class ReadFile  {
	
	/**  
	 * Name der zu öffnenden CSV Datei
	 */
	
	private String fileName = "";
	
	/**  
	 * Liste der Transaktionen aus der CSV Datei
	 */
	
	public List<Posten> posten;
	
	/**
	 * Konstruktor für ReadFile
	 * @param fileName
	 * Name der Datei
	 * @param posten
	 * Liste der Transaktionen
	 */
	public ReadFile(String fileName, List<Posten> posten){
		
		this.fileName = fileName;
		this.posten = posten;
		
	}
	
	/**
	 * Liste der Transaktionen der CSV Datei wird in ein Modell übertragen
	 */
	public void readDatafromFile(){
		
		String nameOfFile = fileName;
		try {
			// Zeilenweises Einlesen der Daten
			CSVReader reader = new CSVReader(new FileReader(nameOfFile),'#');
			String[] nextLine;
		
			while ((nextLine = reader.readNext()) != null) {
				DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN);
				Date datum = df.parse(nextLine[0]);
				String bezeichnung = nextLine[2];
				double betrag = Double.parseDouble(nextLine[3]);
				String kategorie = nextLine[1];
				String Transaktionsart = nextLine[4];
				int key = Integer.parseInt(nextLine[5]);
				
				posten.add(new Posten(key, datum, kategorie, bezeichnung, betrag, Transaktionsart));
			}
			reader.close();

		} catch (FileNotFoundException e) {
			System.err
					.println("Die Datei "+  nameOfFile + " wurde nicht gefunden!");
			System.exit(1);
		} catch (IOException e) {
			System.err
					.println("Probleme beim Oeffnen der Datei " + nameOfFile + "!");
			System.exit(1);
		} catch (ParseException e) {
			System.err
					.println("Formatfehler: Die Datei " + nameOfFile +" konnte nicht eingelesen werden!");
			System.exit(1);
		}
	}

}
