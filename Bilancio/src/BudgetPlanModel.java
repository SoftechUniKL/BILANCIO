import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;


/**
 * Datenmodell des Budgetplaners
 * 
 * Die Daten werden in der Datei data/budget.csv abgespeichert als CSV-Datei.
 * 
 */
public class BudgetPlanModel {
	List<Posten> ausgaben;
    String filename = "data/budget.csv" ;
    double Kontostand = 0.0;
	public BudgetPlanModel() {
		this.ausgaben = new ArrayList<Posten>();
		readDatafromFile();
	
	}
	
	void readDatafromFile(){
		try {
			// Zeilenweises Einlesen der Daten
			CSVReader reader = new CSVReader(new FileReader(filename),'#');
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN);
				Date datum = df.parse(nextLine[0]);
				String bezeichnung = nextLine[2];
				double betrag = Double.parseDouble(nextLine[3]);
				String kategorie = nextLine[1];
				String Transaktionsart = nextLine[4];
				
				ausgaben.add(new Posten(datum, kategorie, bezeichnung, betrag, Transaktionsart));
			}
			reader.close();

		} catch (FileNotFoundException e) {
			System.err
					.println("Die Datei data/budget.csv wurde nicht gefunden!");
			System.exit(1);
		} catch (IOException e) {
			System.err
					.println("Probleme beim Oeffnen der Datei data/budget.csv!");
			System.exit(1);
		} catch (ParseException e) {
			System.err
					.println("Formatfehler: Die Datei konnte nicht eingelesen werden!");
			System.exit(1);
		}
	}
	
	void writeDataIntoFile(){
		
		 CSVWriter writer = null;
			String[] line = new String[5];
			String str;
			try {
				writer = new CSVWriter(new FileWriter("data/budget.csv"), '#', CSVWriter.NO_QUOTE_CHARACTER);
				
				int i = 0;
				
				for (Posten p : this.ausgaben) {
					//

					line[0] = new SimpleDateFormat("dd.MM.yyyy").format(p.getDatum());
					line[2] = p.getBezeichnung() ;
					line[3] = Double.toString( p.getBetrag());
					//line[2] = String.format("%.2f", p.getBetrag());
					line[1] = p.getKategorie().toString();
					line[4] = p.getTransaktionsart().toString();
					
					writer.writeNext(line);
					i++;

				}

				writer.close();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
	//Anfangseingabe/betrag für aktuellen Kontostand 
	
		void setKontostand (double k ) {
			Kontostand = k; 
		}
		
		void Kontostand_nach_ausgabe (double a)
		{ Kontostand-=a; }
		
		void Kontostand_nach_einzahlung (double e)
		{ Kontostand+=e; }
		
		public double getKontostand()		{ 
			double tmpKontostand = 0;
			int size = ausgaben.size();
			for ( int i=0; i< size ; i++) {
				
				if (ausgaben.get(i).getTransaktionsart().equals("Einnahme") ){
					tmpKontostand +=ausgaben.get(i).getBetrag();
				}
				
				if (ausgaben.get(i).getTransaktionsart().equals("Ausgabe") ){
					tmpKontostand -=ausgaben.get(i).getBetrag();
					}
			
			}
			
			
			return tmpKontostand;}
	
	
}