package br.com.fiap.validate;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ValidateMove {

	private static Validator validator;
	private static Document document;
	private static String xml;

	public ValidateMove() {
	}

	public ValidateMove(String xml) {
		ValidateMove.xml = xml;
	}

	public static Validator getValidador() {
		return validator;
	}

	public static void setValidador(Validator validador) {
		ValidateMove.validator = validador;
	}

	public static Document getDocument() {
		return document;
	}

	public static void setDocument(Document document) {
		ValidateMove.document = document;
	}

	public static String getXml() {
		return xml;
	}

	public static void setXml(String xml) {
		ValidateMove.xml = xml;
	}

	public static void getXmlSchema() throws SAXException {
		SchemaFactory factory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = factory.newSchema(new File("Move.xsd"));
		validator = schema.newValidator();
	}

	public static void getXmlString() throws ParserConfigurationException,
			SAXException, IOException {

		DocumentBuilder parser = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xml));
		document = parser.parse(is);
		
	}

	public  void validar() throws SAXException, IOException,
			ParserConfigurationException {

		try {
			getXmlSchema();
			getXmlString();
			
		} catch (Exception ex) {
			ex.printStackTrace(); 
			System.out.println("Erro to Get  XML ou Schema"); 
			System.exit(1); 
		}

		try {
			validator.validate(new DOMSource(document));
			System.out.println("XML Validate OK");
		} catch (SAXException s) {
			s.printStackTrace();
		}
	}

}