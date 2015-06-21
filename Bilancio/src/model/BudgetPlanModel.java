package model;
import gui.BudgetPlanGUI;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;

import utility.ReadFile;
import utility.WriteFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;


/**
 * Datenmodell des Budgetplaners
 * 
 * Die Daten werden in der Datei data/budget.csv abgespeichert als CSV-Datei.
 * 
 */
public class BudgetPlanModel extends  Observable{
	public List<Posten> ausgaben;
    public String filename = "data/budget.csv" ;
    double Kontostand = 0.0;
    
	public BudgetPlanModel() {
		
		this.ausgaben = new ArrayList<Posten>();
		
		ReadFile rFile = new ReadFile(filename, ausgaben);
		rFile.readDatafromFile();
		

		WriteFile wFile = new WriteFile(filename, ausgaben);
		
		
		this.addObserver(wFile);
		tell("Model has changed.");
	}
	
//	void readDatafromFile(String fileName){
//		String nameOfFile = fileName;
//		try {
//			// Zeilenweises Einlesen der Daten
//			CSVReader reader = new CSVReader(new FileReader(nameOfFile),'#');
//			String[] nextLine;
//			while ((nextLine = reader.readNext()) != null) {
//				DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN);
//				Date datum = df.parse(nextLine[0]);
//				String bezeichnung = nextLine[2];
//				double betrag = Double.parseDouble(nextLine[3]);
//				String kategorie = nextLine[1];
//				String Transaktionsart = nextLine[4];
//				
//				ausgaben.add(new Posten(datum, kategorie, bezeichnung, betrag, Transaktionsart));
//			}
//			reader.close();
//
//		} catch (FileNotFoundException e) {
//			System.err
//					.println("Die Datei "+  nameOfFile + " wurde nicht gefunden!");
//			System.exit(1);
//		} catch (IOException e) {
//			System.err
//					.println("Probleme beim Oeffnen der Datei " + nameOfFile + "!");
//			System.exit(1);
//		} catch (ParseException e) {
//			System.err
//					.println("Formatfehler: Die Datei " + nameOfFile +" konnte nicht eingelesen werden!");
//			System.exit(1);
//		}
//	}
	
//	public void writeDataIntoFile(){
//		
//		 CSVWriter writer = null;
//			String[] line = new String[5];
//			String str;
//			try {
//				writer = new CSVWriter(new FileWriter("data/budget.csv"), '#', CSVWriter.NO_QUOTE_CHARACTER);
//				
//				int i = 0;
//				
//				for (Posten p : this.ausgaben) {
//					//
//
//					line[0] = new SimpleDateFormat("dd.MM.yyyy").format(p.getDatum());
//					line[2] = p.getBezeichnung() ;
//					line[3] = Double.toString( p.getBetrag());
//					//line[2] = String.format("%.2f", p.getBetrag());
//					line[1] = p.getKategorie().toString();
//					line[4] = p.getTransaktionsart().toString();
//					
//					writer.writeNext(line);
//					i++;
//
//				}
//
//				writer.close();
//				
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//	}
	
	
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
			
			
			return tmpKontostand;
			
		}
	
	
		public double getPrognose (List<Posten> transaktionen, int prognoseMonat){
			
			double prognose = 0;
			 Calendar stichTag = Calendar.getInstance(); 
			 int month = stichTag.get(Calendar.MONTH);
		     int day = stichTag.get(Calendar.DAY_OF_MONTH);
		     int year = stichTag.get(Calendar.YEAR) - 1;
		     stichTag.set(Calendar.YEAR,year - 1);
		     
		     List<Posten> transaktionenCopy = transaktionen;
		     
		  // Alle Transaktionen holen, die max 365 tag zurück liegen 
		    for (int i=0; i<transaktionen.size();i++){
		    	
		    	Date transaktDatum = transaktionenCopy.get(i).getDatum();
		    	if (transaktDatum.before(stichTag.getTime())){
		    		
		    		transaktionenCopy.remove(i);
		    		
		    	}
		    
		    
		    }
			
		    double tmpKontostand=0;;
		    int size = transaktionenCopy.size();
			for ( int i=0; i< size ; i++) {
				
				if (transaktionenCopy.get(i).getTransaktionsart().equals("Einnahme") ){
					tmpKontostand +=transaktionenCopy.get(i).getBetrag();
				}
				
				if (transaktionenCopy.get(i).getTransaktionsart().equals("Ausgabe") ){
					tmpKontostand -=transaktionenCopy.get(i).getBetrag();
					}
			
			}
			
			prognose = tmpKontostand*prognoseMonat;
			
			return prognose;
		}
		
		public void tell(String info){
			
			System.out.println("Number of Observers = " + countObservers());
	        if(countObservers()>0){
	        	
	            setChanged();
	            notifyObservers(info);
	        }
	    }
}