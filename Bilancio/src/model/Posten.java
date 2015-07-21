package model;
import java.util.Date;
import java.util.Random;

/**
 * Posten in der Budgetplanung
 */
public class Posten {
	/**
	 * Datum, wann der Posten verbucht wurden
	 */
	private Date datum;
	/**
	 * Kurze Beschreibung
	 */
	private String bezeichnung;
	/**
	 * Hoehe des Postens
	 */
	private double betrag;
	/**
	 * Kategorie der Ausgabe / Einnahme
	 */
	private String kategorie;
	
	private String Transaktionsart;
	
	double Kontostand; 
	
	private double key = 0.0 ;

	/**
	 * Konstruktor
	 * 
	 * @param datum
	 *            Datum, wann der Posten verbucht wurde
	 * @param bezeichnung
	 *            Kurze Beschreibung
	 * @param betrag
	 *            Hoehe des Postens
	 * @param kategorie
	 *            Art der Ausgabe
	 */
	public Posten(Date datum,String kategorie, String bezeichnung, double betrag, String Transaktionsart) {
		this.bezeichnung = bezeichnung;
		this.datum = datum;
		this.betrag = betrag;
		this.kategorie = kategorie;
		this.Transaktionsart = Transaktionsart; 
		//this.key +=1;
	}

	public Date getDatum() {
		return datum;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public double getBetrag() {
		return betrag;
	}
	
	public String getKategorie() {
		return kategorie;
	}
	
	public String getTransaktionsart () {
		return Transaktionsart;
		
	}

	public double getKey() {
		return key;
	}


}

