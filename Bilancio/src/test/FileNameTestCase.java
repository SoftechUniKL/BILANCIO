package test;

import static org.junit.Assert.*;
import model.BudgetPlanModel;

import org.junit.Test;

/**
 * Testcase für Dateiname
 * 
 * @author Abraham Amanuel
 * @author Asmelash Samuel
 * @author Lamberty Tim
 * 
 *
 */
public class FileNameTestCase {

	@Test
	public void test() {
		
		BudgetPlanModel testBudgetPlanModel = new  BudgetPlanModel();
		
		String file =  testBudgetPlanModel.filename;
		
		assertEquals("data/budget.csv", file);
	}

}
