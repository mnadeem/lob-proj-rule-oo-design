package com.org.lob.project.service.parser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
// https://docs.oracle.com/javase/tutorial/jaxp/xslt/xpath.html
// https://howtodoinjava.com/java/xml/java-xpath-expression-examples/
// http://learningprogramming.net/java/xpath/use-or-condition-in-xpath-in-java-xml/
public class DefaultParser {

	private XPath xPath;
	private Document xmlDocument;

	public DefaultParser(String file) throws Exception {

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();

		this.xmlDocument = builder.parse(new ClassPathResource(file).getInputStream());
		this.xPath = XPathFactory.newInstance().newXPath();
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

	public static void main(String[] args) throws Exception {
		DefaultParser parser = new DefaultParser("employees.xml");
		//Get first match
		//System.out.println(parser.string("/employees/employee/firstName"));

		//Get all matches
		NodeList nodes = (NodeList) parser.xPath.compile("/employees/employee[@id='1' or @id ='2']").evaluate(parser.xmlDocument, XPathConstants.NODESET);
		System.out.println("Count employee: " + nodes.getLength());

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			String id = parser.xPath.compile("./department[name='IT']/id").evaluate(node);
			System.out.println("id: " + id);
		}		
	}
}