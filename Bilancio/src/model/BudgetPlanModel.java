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
 * @author Abraham Amanuel
 * @author Asmelash Samuel
 * @author Lamberty Tim
 */
public class BudgetPlanModel extends  Observable implements IBudgetPlanModel{
	
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
    
    /**
     * EIndeutiger Schlüssel
     */
    
    public int key;
    
    private static boolean log = false;
    
    /**
     * Konstruktor ohne Parameter
     */
    
	public BudgetPlanModel() {
		
		this.ausgaben = new ArrayList<Posten>();
		
		ReadFile rFile = new ReadFile(filename, ausgaben);
		rFile.readDatafromFile();
		
		WriteFile wFile = new WriteFile(filename, ausgaben);
		
		this.addObserver(wFile);
		
		tell("Model has changed.");
	}
	
	/**
	 * Konstruktor mit Dateiname als Parameter
	 * @param file
	 * Dateiname
	 */
	
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
		
	
		@Override
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
		 * Prognosealgorithmus
		 */
		@Override
		public double getPrognose (int prognoseMonat){
			
			double prognose = 0;
			 Calendar stichTag = Calendar.getInstance(); 
		     int year = stichTag.get(Calendar.YEAR) - 1;
		     stichTag.set(Calendar.YEAR,year - 1);
		
			prognose = getKontostand()+(getKontostand()/getAnzahlMonate())*prognoseMonat;
			
			return prognose;
		}
		
		/**
		 * Bei Änderungen werden Nachrichten verschickt
		 */
		@Override
		public void tell(String info){
			if(log)
			System.out.println("Number of Observers = " + countObservers());
	        if(countObservers()>0){
	        	
	            setChanged();
	            notifyObservers(info);
	        }
	    }

		/**
		 * Methode zum Posten hinzufügen. Dabei wird der neue Posten mit einem eindeutigen Schlüssel versehen.
		 */
		@Override
		public void addPosten(Posten posten) {
			
			int key;
			int lastElement =  this.ausgaben.size()-1;
			if(lastElement>-1) 
				key =  this.ausgaben.get(lastElement).getKey();
			else
				key = 0;
			
			posten.setKey(key+1);
			this.ausgaben.add(posten);
			
		}
		/**
		 * Methode, die die Länge der Liste bestimmt.
		 */
		@Override
		public int getSize() {
		
			return this.ausgaben.size();
		}
		/**
		 * Methode zum Löschen von Transaktionen aus der Liste mit bestimmten Schlüssel
		 */
		@Override
		public void removeAusgabe(int key) {

			int i = 0;
			for (Posten p : ausgaben) {
					
				if(key == p.getKey()) {	
					if(log)
					System.out.println( key + " wurde aus der Tranksaktionsliste gelöscht. ");
					ausgaben.remove(i);
					break;
				}
	
				else 	i++;

			}
				
		}
		/**
		 * Gibt die Liste der Transaktionen aus
		 */

		public List<Posten> getTransaction() {
			return ausgaben;
		}
		
		/** 
		 * Bestimmt die Anzahl der Monate für Prognosealgorithmus
		 * @return Anzahl der Monate
		 */
		
	@SuppressWarnings("deprecation")
	public int getAnzahlMonate() {
		int i = 0;
		Date firstDate = ausgaben.get(0).getDatum();

		for (i = 0; i < ausgaben.size(); i++) {
			if (firstDate.after(ausgaben.get(i).getDatum())) {
				firstDate = ausgaben.get(i).getDatum();

			}
		}

		Date lastDate = ausgaben.get(0).getDatum();
		for (i = 0; i < ausgaben.size(); i++) {
			if (lastDate.before(ausgaben.get(i).getDatum())) {
				lastDate = ausgaben.get(i).getDatum();

			}
		}

		
		int jahre = lastDate.getYear() - firstDate.getYear() - 1;
		int firstmonat = firstDate.getMonth();
		int lastmonat = lastDate.getMonth();

		int monate = 12 * jahre + (12 - firstmonat) + (lastmonat +1);
		
		if(log)
		System.out.println("Jahre:"+ jahre);
		if(log)
		System.out.println("Monate:"+ monate);

		return monate;
	}
	
	/**
	 * Gibt Schlüssel einer Transaktion aus
	 */
	@Override
	public int getKey() {
		
		return key;
	}
	/** 
	 * Bestimmt den Schlüssel einer Transaktion 
	 */

	public void setKey() {
		int key =  ausgaben.get(ausgaben.size()-1).getKey();
		this.key = key + 1;
	}

	
}
