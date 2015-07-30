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
	 * Anhand vorhandenen Daten, Prognose f�r die n�chste Monate treffen.
	 * @param prognoseMonat		// Anzahl der Monate im vorraus
	 * @return Kontostand
	 */
	public abstract double getPrognose(int prognoseMonat);

	/**
	 * 	Message an anderen KLassen, bei �nderungen
	 * @param info		Information �ber �nderung
	 */
	public abstract void tell(String info);

	/**
	 * Hinzuf�gen eines Beobachters
	 * @param observer 
	 * beobachter
	 */
	public abstract void addObserver(Observer observer);

	/**
	 * Methode zum Posten hinzuf�gen. Dabei wird der neue Posten mit einem eindeutigen Schl�ssel versehen.
	 * @param posten
	 * Transaktion 
	 */
	
	public abstract void addPosten(Posten posten);
	
	/**
	 * Methode, die die L�nge der Liste bestimmt.
	 * @return size 
	 * size
	 */
	
	public abstract int getSize();
	
	/**
	 * Methode zum L�schen von Transaktionen aus der Liste mit bestimmten Schl�ssel
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
	 * Gibt Schl�ssel einer Transaktion aus
	 * @return Schl�ssel
	 */
	public abstract int getKey() ;

	//public abstract void setKey();
	
	

}