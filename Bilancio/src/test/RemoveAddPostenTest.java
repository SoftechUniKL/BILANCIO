package test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import model.BudgetPlanModel;
import model.Posten;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RemoveAddPostenTest {
	
	
	private BudgetPlanModel testBudgetPlanModel ;

	@Before
	public void   init() {
		testBudgetPlanModel =  new  BudgetPlanModel();
		
	}

	@Test
	public void testDeleteFirst() {
		
		int listlengthBefore = testBudgetPlanModel.ausgaben.size();
		testBudgetPlanModel.removeAusgabe(0);
		
		int listlengthAfter = testBudgetPlanModel.ausgaben.size();
		
		
		assertEquals(1, listlengthBefore- listlengthAfter);
	}
	
	@Test
	public void testDeleteLast() {
		
		int listlengthBefore = testBudgetPlanModel.ausgaben.size();
		testBudgetPlanModel.removeAusgabe(listlengthBefore-1);
		
		int listlengthAfter = testBudgetPlanModel.ausgaben.size();
		
		
		assertEquals(1, listlengthBefore- listlengthAfter);
	}
	
	@Test
	public void testDeleteRand() {
		
		
		
		int listlengthBefore = testBudgetPlanModel.ausgaben.size();
		
		int rand = (int)(Math.random()*listlengthBefore-1);
		
		testBudgetPlanModel.removeAusgabe(rand);
		
		int listlengthAfter = testBudgetPlanModel.ausgaben.size();
		
		
		assertEquals(1, listlengthBefore- listlengthAfter);
	}

	
	
	@Test
	public void testAddPosten() {
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy",
				Locale.GERMANY);
		
		int listlengthBefore = testBudgetPlanModel.ausgaben.size();
		
		//#Gehalt#Jubi#400.0#Einnahme
		
		Date datum = null;
		String dat = "01.06.2015" ;

		try {
			datum = df.parse(dat);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}  
		Posten posten = new Posten(datum, null, null, listlengthBefore, null);
		
		testBudgetPlanModel.addPosten(posten);;
		
		int listlengthAfter = testBudgetPlanModel.ausgaben.size();
		
		
		assertEquals(1, listlengthBefore- listlengthAfter);
	}

}
