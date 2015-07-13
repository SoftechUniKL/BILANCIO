package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import utility.ReadFile;
import utility.WriteFile;


/**
 * Datenmodell des Budgetplaners
 * 
 * Die Daten werden in der Datei data/budget.csv abgespeichert als CSV-Datei.
 * 
 */
public class BudgetPlanModel extends  Observable{
	
	/**
	 *  Liste der Transaktionen bzw. Posten
	 */
	public List<Posten> ausgaben;
	/**
	 *  Name der Datei
	 */
    public String filename = "data/budget.csv" ;
    /**
     * Aktueller Kontostand
     */
    double Kontostand = 0.0;
    
	public BudgetPlanModel() {
		
		this.ausgaben = new ArrayList<Posten>();
		
		ReadFile rFile = new ReadFile(filename, ausgaben);
		rFile.readDatafromFile();
		
		WriteFile wFile = new WriteFile(filename, ausgaben);
		
		this.addObserver(wFile);
		
		tell("Model has changed.");
	}
	
public BudgetPlanModel(String file) {
		
	this.filename = file;
		
		this.ausgaben = new ArrayList<Posten>();
		
		ReadFile rFile = new ReadFile(filename, ausgaben);
		rFile.readDatafromFile();
		
		WriteFile wFile = new WriteFile(filename, ausgaben);
		
		this.addObserver(wFile);
		
		tell("Model has changed.");
	}		
	
	/**
	 * Anfangseingabe/betrag für aktuellen Kontostand 
	 * @param k  Betrag
	 */
		void setKontostand (double k ) {
			Kontostand = k; 
		}
		
	/**
	 * 	Kontostand berechnen
	 * @return kontostand
	 */
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
	
		/**
		 * Anhand vorhandenen Daten , Prognose für die nächste Monate treffen.
		 * @param transaktionen		// Daten
		 * @param prognoseMonat		// Anzahl der Monate im vorraus
		 * @return prognose			// Prognose
		 */
		public double getPrognose (List<Posten> transaktionen, int prognoseMonat){
			
			double prognose = 0;
			 Calendar stichTag = Calendar.getInstance(); 
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
			prognose = getKontostand() + tmpKontostand/12*prognoseMonat;
			
			return prognose;
		}
		
		/**
		 * 	Message an anderen KLassen , bei Änderungen
		 * @param info		Information über Änderung
		 */
		public void tell(String info){
			
			System.out.println("Number of Observers = " + countObservers());
	        if(countObservers()>0){
	        	
	            setChanged();
	            notifyObservers(info);
	        }
	    }
}