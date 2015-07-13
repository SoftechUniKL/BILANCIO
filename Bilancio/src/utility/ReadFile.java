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
	
	private String fileName = "";
	
	public List<Posten> posten;
	
	public ReadFile(String fileName, List<Posten> posten){
		
		this.fileName = fileName;
		this.posten = posten;
		
	}
	
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
				
				posten.add(new Posten(datum, kategorie, bezeichnung, betrag, Transaktionsart));
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
