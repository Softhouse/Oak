package se.softhouse.garden.oak;

import static se.softhouse.garden.oak.DC.AND;
import static se.softhouse.garden.oak.DC.EQUALS;
import static se.softhouse.garden.oak.DC.GT;
import static se.softhouse.garden.oak.DC.LT;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.softhouse.garden.oak.model.ABasicRegister;
import se.softhouse.garden.oak.model.ARegister;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/spring/applicationContext-test.xml")
@ActiveProfiles("dev")
public class TestRules {

	@Test
	public void testBasicRule() {
		ARegister reg = new ABasicRegister();
		reg.set("name", "Micke");
		reg.set("age", 40);
		reg.set("salary", new BigDecimal("123.45"));
		reg.set("house", true);

		Assert.assertTrue(EQUALS("name", "Micke").execute(reg));
		Assert.assertTrue(AND(EQUALS("name", "Micke"), EQUALS("age", "40")).execute(reg));
		Assert.assertTrue(AND(EQUALS("name", "Micke"), GT("age", 38), LT("age", 42)).execute(reg));
		Assert.assertTrue(EQUALS("house", true).execute(reg));
	}

	@Test
	public void testExcel() throws InvalidFormatException, IOException {
		DecisionEngine engine = new DecisionEngine();
		ExcelDecisionTableBuilder builder = new ExcelDecisionTableBuilder();
		builder.load("test.xlsx", engine);
		ARegister reg = new ABasicRegister();
		reg.set("C1", 2);
		reg.set("C2", "qwe");
		reg.set("C3", false);
		reg.set("C4", false);
		reg.set("C5", 1);
		engine.execute("Main", reg);
		Assert.assertNull(reg.get("S1"));
		//
		reg.set("C5", 3);
		engine.execute("Main", reg);
		Assert.assertEquals("go", reg.get("S1"));
		Assert.assertEquals(new BigDecimal(5), reg.get("S2"));
		Assert.assertEquals(new BigDecimal("6.1"), reg.get("S3"));
		Assert.assertEquals(new BigDecimal(7), reg.get("S4"));
		Assert.assertEquals("Saab", reg.get("car"));
		Assert.assertEquals("white", reg.get("carColor"));
		Assert.assertNotNull(reg.get("children"));
		Assert.assertEquals(1, ((List) reg.get("children")).size());
		//
		reg = new ABasicRegister();
		reg.set("C1", 3);
		reg.set("C2", "asd");
		reg.set("C3", true);
		reg.set("C4", "true");
		reg.set("C5", 6);
		engine.execute("Main", reg);
		System.out.println(reg);
		Assert.assertEquals("walk", reg.get("S1"));
		Assert.assertEquals("Huyndai", reg.get("cars/0/name"));
		Assert.assertEquals("red", reg.get("cars/0/color"));
		Assert.assertEquals("Volvo", reg.get("cars/1/name"));
		Assert.assertEquals("green", reg.get("cars/1/color"));
	}

}
