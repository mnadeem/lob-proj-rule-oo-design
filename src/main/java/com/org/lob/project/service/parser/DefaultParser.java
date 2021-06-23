package com.org.lob.project.service.parser;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.org.lob.project.service.parser.expression.Expressions;
// https://docs.oracle.com/javase/tutorial/jaxp/xslt/xpath.html
// https://howtodoinjava.com/java/xml/java-xpath-expression-examples/
// http://learningprogramming.net/java/xpath/use-or-condition-in-xpath-in-java-xml/
public class DefaultParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultParser.class);

	private XPath xPath;
	private Document xmlDocument;

	public DefaultParser(String xml) {

		InputSource source = new InputSource(new StringReader(xml));

		try {
			
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			this.xmlDocument = builder.parse(source);
			this.xPath = XPathFactory.newInstance().newXPath();

		} catch (Exception e) {
			LOGGER.error("Error parsing xml ", e);
			throw new IllegalStateException(e);
		}
	}

	public NodeList nodes(String expression) throws Exception {
		return (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
	}

	public String string(String expression) throws Exception {
		return (String) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.STRING);
	}

	public Boolean bool(String expression) throws Exception {
		return (Boolean) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.BOOLEAN);
	}

	public Integer number(String expression) throws Exception {
		return (Integer) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NUMBER);
	}

	public Node node(String expression) throws Exception {
		return (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);
	}

	public String evaluate(Expressions expressions) {
		
		return null;
	}

	public static void main(String[] args) throws Exception {
		DefaultParser parser = new DefaultParser(asString(new FileSystemResource("./src/main/resources/employees.xml")));
		//Get first match
		System.out.println(parser.string("/employees/employee/firstName"));

		//Get all matches
		NodeList nodes = (NodeList) parser.xPath.compile("/employees/employee[@id='1' or @id ='2']").evaluate(parser.xmlDocument, XPathConstants.NODESET);
		System.out.println("Count employee: " + nodes.getLength());

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			String id = parser.xPath.compile("./department[name='IT']/id").evaluate(node);
			System.out.println("id: " + id);
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