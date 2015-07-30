package test;

import static org.junit.Assert.*;
import model.BudgetPlanModel;

import org.junit.Test;


public class TestKontostand {

	@Test
	public void test1() {
		
		BudgetPlanModel testBudgetPlanModel = new  BudgetPlanModel();
		
		double Kontostand =  testBudgetPlanModel.getKontostand();
		
		assertEquals(65.0, Kontostand, 0.0001);
		
		
	}
}

