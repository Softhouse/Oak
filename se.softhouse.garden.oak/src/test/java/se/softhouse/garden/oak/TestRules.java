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

import se.softhouse.garden.oak.model.ABasicDocument;
import se.softhouse.garden.oak.model.ABasicList;
import se.softhouse.garden.oak.model.ADocument;
import se.softhouse.garden.oak.model.AMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/spring/applicationContext-test.xml")
@ActiveProfiles("dev")
public class TestRules {

	@Test
	public void testBasicRule() {
		ABasicDocument doc = new ABasicDocument();
		doc.setParameter("name", "Micke");
		doc.setParameter("age", 40);
		doc.setParameter("salary", new BigDecimal("123.45"));
		doc.setParameter("house", true);

		Assert.assertTrue(EQUALS("name", "Micke").execute(doc));
		Assert.assertTrue(AND(EQUALS("name", "Micke"), EQUALS("age", "40")).execute(doc));
		Assert.assertTrue(AND(EQUALS("name", "Micke"), GT("age", 38), LT("age", 42)).execute(doc));
		Assert.assertTrue(EQUALS("house", true).execute(doc));
	}

	@Test
	public void testExcel() throws InvalidFormatException, IOException {
		DecisionEngine engine = new DecisionEngine();
		ExcelDecisionTableBuilder builder = new ExcelDecisionTableBuilder();
		builder.load("test.xlsx", engine);
		ADocument doc = new ABasicDocument();
		doc.setParameter("C1", 2);
		doc.setParameter("C2", "qwe");
		doc.setParameter("C3", false);
		doc.setParameter("C4", false);
		doc.setParameter("C5", 1);
		engine.execute("Main", doc);
		Assert.assertNull(doc.getParameter("S1"));
		//
		doc.setParameter("C5", 3);
		engine.execute("Main", doc);
		Assert.assertEquals("go", doc.getParameter("S1"));
		Assert.assertEquals(new BigDecimal(5), doc.getParameter("S2"));
		Assert.assertEquals(new BigDecimal("6.1"), doc.getParameter("S3"));
		Assert.assertEquals(new BigDecimal(7), doc.getParameter("S4"));
		Assert.assertEquals("Saab", doc.getParameter("car"));
		Assert.assertEquals("white", doc.getParameter("carColor"));
		Assert.assertNotNull(doc.getParameter("children"));
		Assert.assertEquals(1, ((ABasicList) doc.getParameter("children")).asList().size());
		//
		doc = new ABasicDocument();
		doc.setParameter("C1", 3);
		doc.setParameter("C2", "asd");
		doc.setParameter("C3", true);
		doc.setParameter("C4", "true");
		doc.setParameter("C5", 6);
		engine.execute("Main", doc);
		Assert.assertEquals("walk", doc.getParameter("S1"));
		List<AMap> cars = ((ABasicList) doc.getParameter("cars")).asList();
		Assert.assertEquals(2, cars.size());
		Assert.assertEquals("Huyndai", cars.get(0).getParameter("name"));
		Assert.assertEquals("red", cars.get(0).getParameter("color"));
		Assert.assertEquals("Volvo", cars.get(1).getParameter("name"));
		Assert.assertEquals("green", cars.get(1).getParameter("color"));
		System.out.println(doc);
	}

}
