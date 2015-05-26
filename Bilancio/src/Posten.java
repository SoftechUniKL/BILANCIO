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
	
	double Kontostand; 

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
	public Posten(Date datum, String bezeichnung, double betrag, String kategorie) {
		this.bezeichnung = bezeichnung;
		this.datum = datum;
		this.betrag = betrag;
		this.kategorie = kategorie;
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
	


}

