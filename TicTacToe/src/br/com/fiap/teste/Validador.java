package br.com.fiap.teste;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Validador {

	private static Validator validador;
	private static Document document; 
	
	public static void obterXmlSchema() throws SAXException {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = factory.newSchema(new File("SchemaShipOrder.xsd"));
		validador = schema.newValidator();
	}
	
	public static void obterXml() throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        document = parser.parse(new File("Shiporder.xml"));
	}

	public static void validar() throws SAXException, IOException, ParserConfigurationException{
		obterXmlSchema();
		obterXml();
		
		try{
			validador.validate(new DOMSource(document));
			System.out.println("Arquivo Validado com sucesso");
		}catch(SAXException s){
			System.out.println(s.toString());
		}
		
	}
	
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		validar();
	}
}
