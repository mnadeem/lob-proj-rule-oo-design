package com.org.lob.project.engine;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.org.lob.project.engine.expression.Expressions;
import com.org.lob.project.engine.expression.Operator;
import com.org.lob.project.engine.expression.ReturnExpression;
import com.org.lob.project.engine.expression.RuleExpression;

class DefaultRuleEngineTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRuleEngineTest.class);

	private static DefaultRuleEngine targetBeingTested;

	@BeforeAll
	public static void doBeforeAll() {
		targetBeingTested = new DefaultRuleEngine(asString(new FileSystemResource("./src/main/resources/employees.xml")));
	}

	@Test
	void andExpressionsEngineTest() throws Exception {

		Expressions exp = andExpression();

		RuleEngineResult result = targetBeingTested.evaluate(exp);
		assertTrue(Boolean.valueOf(result.getResult()));
	}

	@Test
	void andExpressionsEngineFalseTest() throws Exception {

		Expressions exp = andExpressionFalse();

		RuleEngineResult result = targetBeingTested.evaluate(exp);
		assertFalse(Boolean.valueOf(result.getResult()));
	}

	private Expressions andExpressionFalse() {
		return Expressions
						.builder(1L, RuleExpression
								.builder()
								.path("/employees/employee")
								//.subPath(null)
								.tag("firstName")
								.operatorBuild("Is Not Null")
								.build()
							)
						.and(RuleExpression
								.subPathBuilder()
								//.subPath(null)
								.tag("department/id")
								.operator(Operator.EQUAL)
								.value("10100")
								.build()
							)
					.build();
	}

	@Test
	void returnExpressionsEngineTest() throws Exception {

		Expressions exp = returnExpression();

		RuleEngineResult result = targetBeingTested.evaluate(exp);
		assertEquals("IT", result);
		assertFalse(Boolean.valueOf(result.getResult()));
	}

	private Expressions returnExpression() {
		return Expressions
						.builder(1L, RuleExpression
								.builder()
								.path("/employees/employee")
								//.subPath(null)
								.tag("firstName")
								.operator(Operator.EQUAL)
								.value("Nadeem")
								.build()
							)
						.and(RuleExpression
								.subPathBuilder()
								//.subPath(null)
								.tag("department/id")
								.operator(Operator.EQUAL)
								.value("101")
								.build()
							)
						.returnExpression(ReturnExpression
								.builder()
								.subPath(null)
								.tag("department/name")
								.returnType("XML value")
								.build())
					.build();
	}
	
	@Test
	void andExpressions() throws Exception {

		String exp = andExpression().buildEvaluationExpression();

		NodeList nodes = targetBeingTested.nodes(exp);
		
		assertEquals(1, nodes.getLength());
		Node node = nodes.item(0);

		String id = targetBeingTested.string("./@id", node);
		assertEquals("1", id);
	}

	private Expressions andExpression() {
		return Expressions
						.builder(1L, RuleExpression
								.builder()
								.path("/employees/employee")
								//.subPath(null)
								.tag("firstName")
								.operatorBuild("Is Not Null")
								.build()
							)
						.and(RuleExpression
								.subPathBuilder()
								//.subPath(null)
								.tag("department/id")
								.operator(Operator.EQUAL)
								.value("101")
								.build()
							)
					.build();
	}
	
	@Test
	void orExpressions() throws Exception {

		String exp = Expressions
						.builder(1L, RuleExpression
								.builder()
								.path("/employees/employee")
								//.subPath(null)
								.tag("firstName")
								.operator(Operator.EQUAL)
								.value("Nadeem")
								.build()
							)
						.or(RuleExpression
								.subPathBuilder()
								//.subPath(null)
								.tag("department/id")
								.operator(Operator.EQUAL)
								.value("101")
								.build()
							)
						.or(RuleExpression
								.subPathBuilder()
								//.subPath(null)
								.tag("department/id")
								.operator(Operator.EQUAL)
								.value("102")
								.build()
							)
						.or(RuleExpression
								.subPathBuilder()
								//.subPath(null)
								.tag("department/id")
								.operatorBuild("Is Not Null")
								.build()
							)
					.build()
				.buildEvaluationExpression();

		NodeList nodes = targetBeingTested.nodes(exp);

		assertEquals(1, nodes.getLength());
		Node node = nodes.item(0);

		String id = targetBeingTested.string("./@id", node);
		assertEquals("1", id);
	}
	
	@Test
	void andOrExpressions() throws Exception {

		String exp = Expressions
						.builder(1L, RuleExpression
								.builder()
								.path("/employees/employee")
								//.subPath(null)
								.tag("firstName")
								.operator(Operator.EQUAL)
								.value("Nadeem")
								.build()
							)
						.and(RuleExpression
								.subPathBuilder()
								//.subPath(null)
								.tag("lastName")
								.operator(Operator.EQUAL)
								.value("Mohammad")
								.build()
							)
						.and(RuleExpression
								.subPathBuilder()
								//.subPath(null)
								.tag("contractor")
								.operator(Operator.EQUAL)
								.value("FALSE")
								.build()
							)
						.or(RuleExpression
								.subPathBuilder()
								//.subPath(null)
								.tag("department/id")
								.operator(Operator.EQUAL)
								.value("101")
								.build()
							)
						.or(RuleExpression
								.subPathBuilder()
								//.subPath(null)
								.tag("department/id")
								.operator(Operator.EQUAL)
								.value("102")
								.build()
							)
					.build()
				.buildEvaluationExpression();

		NodeList nodes = targetBeingTested.nodes(exp);

		assertEquals(1, nodes.getLength());
		Node node = nodes.item(0);

		String id = targetBeingTested.string("./@id", node);
		assertEquals("1", id);
	}
	
	@Test
	void mainExpressionIsNull() throws Exception {

		String exp = Expressions.builder(1L, RuleExpression.builder()
						.path("/employees/employee")
						//.subPath(null)
						.tag("contractor")
						.operatorBuild("Is Null")
						.build())
					.build()
				.buildEvaluationExpression();
		
		NodeList nodes = targetBeingTested.nodes(exp);
		
		assertEquals(1, nodes.getLength());
		Node node = nodes.item(0);

		String id = targetBeingTested.string("./@id", node);
		assertEquals("5", id);
	}

	@Test
	void mainExpressionIsNotNull() throws Exception {

		String exp = Expressions.builder(1L, RuleExpression.builder()
						.path("/employees/employee")
						//.subPath(null)
						.tag("contractor")
						.operatorBuild("Is Not Null")
						.build())
					.build()
				.buildEvaluationExpression();
	
		NodeList nodes = targetBeingTested.nodes(exp);

		assertEquals(9, nodes.getLength());
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			String id = targetBeingTested.string("./id", node);
			assertNotNull(id);
		}
	}

	@Test
	void mainExpressionEqualsNull() throws Exception {

		String exp = Expressions.builder(1L, RuleExpression.builder()
						.path("/employees/employee")
						//.subPath(null)
						.tag("firstName")
						.operator("Equal")
						.value("Nadeem1")
						.build())
					.build()
				.buildEvaluationExpression();

		Node node = targetBeingTested.node(exp);
		assertNull(node);
	}

	@Test
	void mainExpressionEqualsNonNull() throws Exception {
		
		String exp = Expressions.builder(1L, RuleExpression.builder()
						.path("/employees/employee")
						//.subPath(null)
						.tag("firstName")
						.operator("Equal")
						.value("Nadeem")
						.build())
					.build()
				.buildEvaluationExpression();

		NodeList nodes = targetBeingTested.nodes(exp);
		assertEquals(1, nodes.getLength());

		String firstName = targetBeingTested.string("./firstName", nodes.item(0));
		assertEquals("Nadeem", firstName);		
	}

	@Test
	void mainExpressionNotEqualsNonNull() throws Exception {

		String exp = Expressions.builder(1L, RuleExpression.builder()
						.path("/employees/employee")
						//.subPath(null)
						.tag("firstName")
						.operator(Operator.NOT_EQUAL)
						.value("Brian")
						.build())
					.build()
				.buildEvaluationExpression();

		NodeList nodes = targetBeingTested.nodes(exp);

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			String firstName = targetBeingTested.string("./firstName", node);

			//LOGGER.info("firstName: {}", firstName);
			assertNotEquals("Brian", firstName);
		}
		assertEquals(9, nodes.getLength());
	}

	@Test
	void mainExpressionListNonNull() throws Exception {

		String exp = Expressions.builder(1L, RuleExpression.builder()
						.path("/employees/employee")
						//.subPath(null)
						.tag("firstName")
						.operator("List")
						.value("Nadeem|Brian")
						.build())
					.build()
				.buildEvaluationExpression();

		NodeList nodes = targetBeingTested.nodes(exp);
		assertEquals(2, nodes.getLength());

		String firstName = targetBeingTested.string("./firstName", nodes.item(0));
		assertEquals("Nadeem", firstName);		
	}

	@Test
	void exampleOne() throws Exception {
		//Get all matches
		NodeList nodes = targetBeingTested.nodes("/employees/employee[@id='1' or @id ='2']");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			String id = targetBeingTested.string("./department[name='IT' or  name='HR']/id/text()", node);

			assertNotNull(id);
		}
	}

	@Test
	void exampleTwo() throws Exception {
		//Get all matches
		NodeList nodes = targetBeingTested.nodes("/employees/employee[not(boolean(firstName='Nadeem'))]/department[name='IT' and id='101']");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			String id = targetBeingTested.string("./id", node);
			assertNotNull(id);
		}
	}

	public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
        	LOGGER.error("Error Proessing ", e);
            throw new UncheckedIOException(e);
        }
	}
}
