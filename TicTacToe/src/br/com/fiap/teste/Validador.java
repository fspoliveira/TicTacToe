package br.com.fiap.teste;

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

public class Validador {

	private static Validator validador;
	private static Document document; 
	private  static String xmlRecords= "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
            + "<shiporder orderid=\"889923\">"
            + "<orderperson>John Smith</orderperson>"
            + "<shipto> "
            + "<name>Ola Nordmann</name>"
            + "<address>Langgt 23</address>"
            + "<city>4000 Stavanger</city>"
            + "<country>Norway</country>"
            + "</shipto>"
            + "<item>"
            + "<title>Empire Burlesque</title>"
            + "<note>Special Edition</note>"
            + "<quantity>1</quantity>"
            + "<price>10.90</price>"
            + "</item>"
           + "<item>"
     		+ "<title>Hide your heart</title>"
          + "<quantity>1</quantity>"
         + "<price>9.90</price>"
         +"</item>"
         +"</shiporder>";
	
	public static void obterXmlSchema() throws SAXException {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = factory.newSchema(new File("SchemaShipOrder.xsd"));		
		validador = schema.newValidator();
	}
	
	public static void obterXml() throws ParserConfigurationException, SAXException, IOException {
		
		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
     // document = parser.parse(new File("Shiporder.xml"));
        InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmlRecords));
		document = parser.parse(is);
	}

	public static void validar() throws SAXException, IOException, ParserConfigurationException {
		
		try {
			obterXmlSchema();
			obterXml();
			//System.out.println("XML validado com sucesso");
		} catch (Exception ex) {
			ex.printStackTrace(); //pra saber onde foi o erro
			System.out.println("Erro ao obter XML ou Schema"); //pra orientacao
			System.exit(1); //sai porque nao tem sentido continuar sem o schema ou o xml
		}
		
		try {
			validador.validate(new DOMSource(document));
			System.out.println("Arquivo Validado com sucesso");
		} catch (SAXException s) {
			s.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		validar();
	}
}
