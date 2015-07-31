package main;
import gui.BudgetPlanGUI;
import model.BudgetPlanModel;

/**
 * Anwendung BudgetPlan
 * @author Abraham Amanuel
 * @author Asmelash Samuel
 * @author Lamberty Tim
 * 
 */

public class BudgetPlan {
	public static void main(String[] args) {
		
		new BudgetPlanModel(); // Modell
		new BudgetPlanGUI(null); // View und Controller, enabele file open
		
		
	}
	

}
