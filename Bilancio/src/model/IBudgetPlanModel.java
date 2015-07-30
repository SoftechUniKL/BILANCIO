package model;

import java.util.List;
import java.util.Observer;

public interface IBudgetPlanModel {

	/**
	 * 	Kontostand berechnen
	 * @return kontostand
	 */
	public abstract double getKontostand();

	/**
	 * Anhand vorhandenen Daten, Prognose für die nächste Monate treffen.
	 * @param prognoseMonat		// Anzahl der Monate im vorraus
	 * @return Kontostand
	 */
	public abstract double getPrognose(int prognoseMonat);

	/**
	 * 	Message an anderen KLassen, bei Änderungen
	 * @param info		Information über Änderung
	 */
	public abstract void tell(String info);

	/**
	 * Hinzufügen eines Beobachters
	 * @param observer 
	 * beobachter
	 */
	public abstract void addObserver(Observer observer);

	/**
	 * Methode zum Posten hinzufügen. Dabei wird der neue Posten mit einem eindeutigen Schlüssel versehen.
	 * @param posten
	 * Transaktion 
	 */
	
	public abstract void addPosten(Posten posten);
	
	/**
	 * Methode, die die Länge der Liste bestimmt.
	 * @return size 
	 * size
	 */
	
	public abstract int getSize();
	
	/**
	 * Methode zum Löschen von Transaktionen aus der Liste mit bestimmten Schlüssel
	 * @param row
	 * neue Zeile
	 * 
	 */
	
	public abstract void removeAusgabe(int row);
	
	/**
	 * Gibt die Liste der Transaktionen aus
	 * @return Liste der Transaktionen
	 */
	public abstract List<Posten> getTransaction();
	/**
	 * Gibt Schlüssel einer Transaktion aus
	 * @return Schlüssel
	 */
	public abstract int getKey() ;

	//public abstract void setKey();
	
	

}