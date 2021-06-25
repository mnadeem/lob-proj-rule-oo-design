package com.org.lob.project.engine;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.org.lob.project.engine.expression.Expressions;
// https://docs.oracle.com/javase/tutorial/jaxp/xslt/xpath.html
// https://howtodoinjava.com/java/xml/java-xpath-expression-examples/
// http://learningprogramming.net/java/xpath/use-or-condition-in-xpath-in-java-xml/
// https://www.baeldung.com/java-xpath
// https://docs.microsoft.com/en-us/previous-versions/dotnet/netframework-4.0/ms256086(v=vs.100)?redirectedfrom=MSDN
// https://stackoverflow.com/questions/3418470/using-not-in-xpath/3418510
// https://stackoverflow.com/questions/15909348/how-to-tell-using-xpath-if-an-element-is-present-and-non-empty
public class DefaultRuleEngine {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRuleEngine.class);

	private XPath xPath;
	private Document xmlDocument;

	public DefaultRuleEngine(String xml) {

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

	public String string(String expression, Node node) throws Exception {
		return xPath.compile(expression).evaluate(node);
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
}
