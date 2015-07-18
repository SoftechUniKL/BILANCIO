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
	 * Anfangseingabe/betrag f�r aktuellen Kontostand 
	 * @param k  Betrag
	 */
		void setKontostand (double k ) {
			Kontostand = k; 
		}
		
	/* (non-Javadoc)
	 * @see model.IBudgetPlanModel#getKontostand()
	 */
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
	
		/* (non-Javadoc)
		 * @see model.IBudgetPlanModel#getPrognose(java.util.List, int)
		 */
		@Override
		public double getPrognose (int prognoseMonat){
			
			double prognose = 0;
			 Calendar stichTag = Calendar.getInstance(); 
		     int year = stichTag.get(Calendar.YEAR) - 1;
		     stichTag.set(Calendar.YEAR,year - 1);
		     
		  // Alle Transaktionen holen, die max 365 tag zur�ck liegen 
		   /* for (int i=0; i<transaktionen.size();i++){
		    	
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
			
			} */
			prognose = getKontostand()+(getKontostand()/getAnzahlMonate())*prognoseMonat;
			
			return prognose;
		}
		
		/* (non-Javadoc)
		 * @see model.IBudgetPlanModel#tell(java.lang.String)
		 */
		@Override
		public void tell(String info){
			
			System.out.println("Number of Observers = " + countObservers());
	        if(countObservers()>0){
	        	
	            setChanged();
	            notifyObservers(info);
	        }
	    }

		@Override
		public void addAusgabe(Posten posten) {
			this.ausgaben.add(posten);
			
		}
		
		@Override
		public int getSize() {
			// TODO Auto-generated method stub
			return this.ausgaben.size();
		}
		
		@Override
		public void removeAusgabe(int row) {
			// TODO Auto-generated method stub
			this.ausgaben.remove(row);
		}

		public List<Posten> getAusgabe() {
			return ausgaben;
		}
		
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
		
		System.out.println("Jahre:"+ jahre);
		System.out.println("Monate:"+ monate);

		return monate;
	}
}

	
