package model;

import gui.BudgetPlanGUI;

import java.util.List;
import java.util.Observer;

public interface IBudgetPlanModel {

	/**
	 * 	Kontostand berechnen
	 * @return kontostand
	 */
	public abstract double getKontostand();

	/**
	 * Anhand vorhandenen Daten , Prognose f�r die n�chste Monate treffen.
	 * @param transaktionen		// Daten
	 * @param prognoseMonat		// Anzahl der Monate im vorraus
	 * @return prognose			// Prognose
	 */
	public abstract double getPrognose(List<Posten> transaktionen,
			int prognoseMonat);

	/**
	 * 	Message an anderen KLassen , bei �nderungen
	 * @param info		Information �ber �nderung
	 */
	public abstract void tell(String info);

	public abstract void addObserver(Observer observer);

	public abstract void addAusgabe(Posten posten);
	
	public abstract int getSize();
	
	public abstract void removeAusgabe(int row);
	
	public abstract List<Posten> getAusgabe();
	
	

}