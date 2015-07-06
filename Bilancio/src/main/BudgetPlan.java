package main;
import gui.BudgetPlanGUI;
import model.BudgetPlanModel;

/**
 * Anwendung BudgetPlan
 * 
 */

public class BudgetPlan {
	public static void main(String[] args) {
		BudgetPlanModel budget = new BudgetPlanModel(); // Modell
//		BudgetPlanGUI gui =new BudgetPlanGUI(budget); // View und Controller
		
		
		BudgetPlanGUI gui =new BudgetPlanGUI(null); 
		
	}
	

}
