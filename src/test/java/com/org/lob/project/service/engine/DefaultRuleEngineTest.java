package com.org.lob.project.service.engine;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

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

import com.org.lob.project.service.engine.expression.Expressions;
import com.org.lob.project.service.engine.expression.Operator;
import com.org.lob.project.service.engine.expression.RuleExpression;

class DefaultRuleEngineTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRuleEngineTest.class);

	private static DefaultRuleEngine targetBeingTested;

	@BeforeAll
	public static void doBeforeAll() {
		targetBeingTested = new DefaultRuleEngine(asString(new FileSystemResource("./src/main/resources/employees.xml")));
	}

	@Test
	void mainExpressionIsNull() throws Exception {

		String exp = Expressions.builder(RuleExpression.builder()
						.path("/employees/employee")
						//.subPath(null)
						.tag("contractor")
						.operatorBuild("Is Null")
						.build())
					.build()
				.buildExpression();
		
		LOGGER.info(" Exp : {} ", exp);
		
		NodeList nodes = targetBeingTested.nodes(exp);
		
		assertEquals(1, nodes.getLength());
		Node node = nodes.item(0);

		String id = targetBeingTested.string("./@id", node);
		assertEquals("5", id);
	}
	
	@Test
	void mainExpressionIsNotNull() throws Exception {

		String exp = Expressions.builder(RuleExpression.builder()
						.path("/employees/employee")
						//.subPath(null)
						.tag("contractor")
						.operatorBuild("Is Not Null")
						.build())
					.build()
				.buildExpression();
		
		LOGGER.info(" Exp : {} ", exp);
		
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

		String exp = Expressions.builder(RuleExpression.builder()
						.path("/employees/employee")
						//.subPath(null)
						.tag("firstName")
						.operator("Equal")
						.value("Lokesh1")
						.build())
					.build()
				.buildExpression();

		Node node = targetBeingTested.node(exp);
		LOGGER.info(" Exp : {} , result : {}", exp, node);
		assertNull(node);
	}

	@Test
	void mainExpressionEqualsNonNull() throws Exception {
		
		String exp = Expressions.builder(RuleExpression.builder()
						.path("/employees/employee")
						//.subPath(null)
						.tag("firstName")
						.operator("Equal")
						.value("Lokesh")
						.build())
					.build()
				.buildExpression();
		
		LOGGER.info(" Exp : {} ", exp);

		NodeList nodes = targetBeingTested.nodes(exp);
		assertEquals(1, nodes.getLength());

		String firstName = targetBeingTested.string("./firstName", nodes.item(0));
		assertEquals("Lokesh", firstName);		
	}

	@Test
	void mainExpressionNotEqualsNonNull() throws Exception {

		String exp = Expressions.builder(RuleExpression.builder()
						.path("/employees/employee")
						//.subPath(null)
						.tag("firstName")
						.operator(Operator.NOT_EQUAL)
						.value("Brian")
						.build())
					.build()
				.buildExpression();

		NodeList nodes = targetBeingTested.nodes(exp);

		LOGGER.info(" Exp : {}, Count employee: {}", exp, nodes.getLength());
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

		String exp = Expressions.builder(RuleExpression.builder()
						.path("/employees/employee")
						//.subPath(null)
						.tag("firstName")
						.operator("List")
						.value("Lokesh|Brian")
						.build())
					.build()
				.buildExpression();

		LOGGER.info(" Exp : {}", exp);

		NodeList nodes = targetBeingTested.nodes(exp);
		assertEquals(2, nodes.getLength());

		String firstName = targetBeingTested.string("./firstName", nodes.item(0));
		assertEquals("Lokesh", firstName);		
	}

	@Test
	void exampleOne() throws Exception {
		//Get all matches
		NodeList nodes = targetBeingTested.nodes("/employees/employee[@id='1' or @id ='2']");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			String id = targetBeingTested.string("./department[name='IT' or  name='HR']/id/text()", node);
			LOGGER.info(" id : {}", id);
			assertNotNull(id);
		}
	}

	@Test
	void exampleTwo() throws Exception {
		//Get all matches
		NodeList nodes = targetBeingTested.nodes("/employees/employee[not(boolean(firstName='Lokesh'))]/department[name='IT' and id='101']");

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
