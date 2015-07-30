package test;

import static org.junit.Assert.*;
import model.BudgetPlanModel;

import org.junit.Test;

public class Postsize {

	@Test
	public void test() {
		
			BudgetPlanModel testBudgetPlanModel = new  BudgetPlanModel();
			
			int listlength = testBudgetPlanModel.ausgaben.size();
			assertEquals(7,listlength);
			
			double Kontostand =  testBudgetPlanModel.getKontostand();
			assertEquals(65, Kontostand, 0.0001);
		}	
	
	
	

	
	}


