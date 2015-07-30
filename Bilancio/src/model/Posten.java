package model;
import java.util.Date;

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
	
	private int key ;

	/**
	 * Konstruktor
	 * 
	 * @param key 
	 * 		      Schlüssel für den Posten
	 * 
	 * @param datum
	 *            Datum, wann der Posten verbucht wurde
	 * @param bezeichnung
	 *            Kurze Beschreibung
	 * @param betrag
	 *            Hoehe des Postens
	 * @param kategorie
	 *            Art der Ausgabe
	 *  @param Transaktionsart
	 *  		Art der Transaktion     
	 */
	public Posten(int key, Date datum,String kategorie, String bezeichnung, double betrag, String Transaktionsart) {
		this.bezeichnung = bezeichnung;
		this.datum = datum;
		this.betrag = betrag;
		this.kategorie = kategorie;
		this.Transaktionsart = Transaktionsart; 
		this.key = key;
	}
	
	/**
	 * Gibt Datum aus
	 * @return Datum
	 */

	public Date getDatum() {
		return datum;
	}
	
	/**
	 * Gibt Bezeichnung(Name der Transaktion) aus
	 * @return Bezeichnung
	 */

	public String getBezeichnung() {
		return bezeichnung;
	}
	
	/**
	 * Gibt den Betrag den Transaktion aus
	 * @return Betrag
	 */

	public double getBetrag() {
		return betrag;
	}
	/**
	 * Gibt die Kategorie der Transaktion aus
	 * @return Kategorie
	 */
	
	public String getKategorie() {
		return kategorie;
	}
	
	/**
	 * Gibt die Art der Transaktionaus (Einnahme/Ausgabe)
	 * @return Transaktionsart
	 */
	
	public String getTransaktionsart () {
		return Transaktionsart;
		
	}
	/**
	 * Gibt Schlüssel der Transaktion aus
	 * @return key
	 */

	public int getKey() {
		return key;
	}
	
	/**
	 * legt Schlüssel der Transaktion fest
	 * @param key
	 * Schlüssel
	 */

	public void setKey(int key) {
		this.key = key;
	}


}

