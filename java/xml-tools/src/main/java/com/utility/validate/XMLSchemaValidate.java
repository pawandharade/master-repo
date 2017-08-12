/*
 * XMLSchemaValidate.java
 *
 * created at Aug 11, 2017 by Pawan Dharade <pawandharade@gmail.com>
 * 
 */

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;

public class XMLSchemaValidate {
	public static void main(String[] args) {
		Source xmlFile = new StreamSource(new File("C:\\Users\\K277600\\Desktop\\FI\\Out1.xml"));
		File schemaFile = new File("C:\\Users\\K277600\\Desktop\\FI\\Validate\\pain.001.001.03_FI.xsd");
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			Schema schema = schemaFactory.newSchema(schemaFile);
			Validator validator = schema.newValidator();
			validator.validate(xmlFile);
			System.out.println(xmlFile.getSystemId() + " is valid");
		}catch (SAXException ex) {
			  System.out.println(xmlFile.getSystemId() + " is NOT valid.\nReason:" + ex);
		} catch (IOException ex) {
			System.out.println(ex);
		}catch(Exception ex){
			System.out.println(ex);
		}
	}
}

