package src.com.mapping;

import java.util.List;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import src.com.structure.csv.data.collect.CSVDataFetchResult;

public class GenerateMessageStructure {
	
	private static final String RELATIONSHIP_ONE_TO_ONE   = "1-1";
	private static final String RELATIONSHIP_ONE_TO_MANY  = "1-m";
	private static final String RELATIONSHIP_MANY_TO_MANY = "m-m";
	private static final String RELATIONSHIP_ZERO_TO_ONE  = "0-1";
	private static final String RELATIONSHIP_ZERO_TO_MANY = "0-m";
	
	public void generateMessageStructure(List<CSVDataFetchResult> csvDataFetchResults, String sMessageStructureName, String sMessageStructureDescription, String sFilePath){
		try{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			
			doc = createCSVMsgHeaderInfo(doc, sMessageStructureName, sMessageStructureDescription);
			
			for(CSVDataFetchResult csvDataFetchResult : csvDataFetchResults){
				if (csvDataFetchResult.getRecordType().toUpperCase().equals("RECORD")){
					doc = createCSVMessageRecord(csvDataFetchResult, doc);
				}else if(csvDataFetchResult.getRecordType().toUpperCase().equals("FIELD")){
					doc = createCSVMessageField(csvDataFetchResult, doc);
				}
			}
			
			// write the content into xml file
			doc.getDocumentElement().normalize();
			writeToFile(doc, sFilePath, sMessageStructureName);
			
		}catch(ParserConfigurationException pce){
			pce.printStackTrace();
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	protected Document createCSVMsgHeaderInfo(Document doc, String sMessageStructureName, String sMessageStructureDescription) {
		// root elements
		Element rootElement = doc.createElement("message");
		doc.appendChild(rootElement);
		
		// set attribute to root element
		Attr attrVersion = doc.createAttribute("version");
		attrVersion.setValue("6.1");
		rootElement.setAttributeNode(attrVersion);
		
		Element name = doc.createElement("name");
		name.setTextContent(sMessageStructureName);
		rootElement.appendChild(name);
		
		Element kind = doc.createElement("kind");
		kind.setTextContent("11");
		rootElement.appendChild(kind);
		
		Element description = doc.createElement("description");
		
		Element standardTitle = doc.createElement("standardTitle");
		standardTitle.setTextContent(sMessageStructureDescription);
		description.appendChild(standardTitle);
		
		Element standardText = doc.createElement("standardText");
		standardText.setTextContent(sMessageStructureDescription);
		description.appendChild(standardText);
		rootElement.appendChild(description);
		
		return doc;
	}
	
	protected Document createCSVMessageRecord(CSVDataFetchResult csvDataFetchResult, Document doc){
		
			Element record = doc.createElement("record");
			
			//name
			Element name = doc.createElement("name");
			name.setTextContent(csvDataFetchResult.getName());
			record.appendChild(name);
			
			//description
			Element description = doc.createElement("description");
			
			if (!csvDataFetchResult.getTitle().equals("")){
				Element standardTitle = doc.createElement("standardTitle");
				standardTitle.setTextContent(csvDataFetchResult.getTitle());
				description.appendChild(standardTitle);
			}
			
			if(!csvDataFetchResult.getDescription().equals("")){
				Element standardText = doc.createElement("standardText");
				standardText.setTextContent(csvDataFetchResult.getDescription());
				description.appendChild(standardText);
			}
			record.appendChild(description);
			
			//min occurrence
			Element min = doc.createElement("min");
			//max occurrence
			Element max = doc.createElement("max");
			
			String rltn = getRelationship(csvDataFetchResult.getRelationship());
			String[] rltns = rltn.split(",");
			min.setTextContent(rltns[0]);
			max.setTextContent(rltns[1]);
			
			record.appendChild(min);
			record.appendChild(max);
			
			//optional/mandatory
			Element mo = doc.createElement("option");
			if (csvDataFetchResult.getSubRecordOf().toUpperCase().equals("ROOT")){
				mo.setTextContent("1");
			}else{
				mo.setTextContent(csvDataFetchResult.getOptionalMandatory().trim().equals("M") ? "1" : "0");
			}
			
			record.appendChild(mo);
			
			if (csvDataFetchResult.getSubRecordOf().toUpperCase().equals("ROOT")){
				doc.getFirstChild().appendChild(record);
			}else{
				NodeList records = doc.getElementsByTagName("record");
				Element rec = null;
				for(int i=0; i<records.getLength();i++){
					rec = (Element) records.item(i);
					if(rec.getFirstChild().getNodeName().equals("name")){
						
						if(rec.getFirstChild().getTextContent().equals(csvDataFetchResult.getSubRecordOf())){
							rec.appendChild(record);
							break;
						}
					}
				}
			}
				
		return doc;
	}
	
	protected Document createCSVMessageField(CSVDataFetchResult csvDataFetchResult, Document doc){
		Element field = doc.createElement("field");
		
		//name
		Element name = doc.createElement("name");
		name.setTextContent(csvDataFetchResult.getName());
		field.appendChild(name);
		
		//min occurrence
		Element min = doc.createElement("min");
		//max occurrence
		Element max = doc.createElement("max");
		
		if (csvDataFetchResult.getDataType().toUpperCase().equals("KEY")){
			min.setTextContent("1");
			max.setTextContent("1");
		}else{
			String rltn = getRelationship(csvDataFetchResult.getRelationship());
			String[] rltns = rltn.split(",");
			min.setTextContent(rltns[0]);
			max.setTextContent(rltns[1]);
		}
		
		if(csvDataFetchResult.getOptionalMandatory().trim().equals("M")){
			min.setTextContent("1");
		}
		
		field.appendChild(min);
		field.appendChild(max);
		
		//regularExpression
		Element regularExpression = doc.createElement("regularExpression");
		if (!csvDataFetchResult.getRegEx().equals("")){
			regularExpression.setTextContent(csvDataFetchResult.getRegEx());
			field.appendChild(regularExpression);
		}
		
		//datatype
		Element type = doc.createElement("type");
		type.setTextContent(getDatatype(csvDataFetchResult.getDataType().trim().toUpperCase()));
		field.appendChild(type);
		
		//minLength
		Element minLength = doc.createElement("minLength");
		minLength.setTextContent( Integer.toString(csvDataFetchResult.getMinLength()));
		field.appendChild(minLength);
		
		//maxLength
		Element maxLength = doc.createElement("maxLength");
		maxLength.setTextContent(Integer.toString(csvDataFetchResult.getMaxLength()));
		field.appendChild(maxLength);
		
		//optional/mandatory
		Element mo = doc.createElement("option");
		mo.setTextContent(csvDataFetchResult.getOptionalMandatory().trim().equals("M") ? "1" : "0");
		field.appendChild(mo);
		
		//isQualifier
		Element isQualifier = doc.createElement("isQualifier");
		isQualifier.setTextContent("0");
		field.appendChild(isQualifier);
		
		//isQualifier
		Element value = doc.createElement("value");
		value.setTextContent(csvDataFetchResult.getDefaultValue());
		field.appendChild(value);
		
		NodeList records = doc.getElementsByTagName("record");
		Element rec = null;
		for(int i=0; i<records.getLength();i++){
			rec = (Element) records.item(i);
			if(rec.getFirstChild().getNodeName().equals("name")){
				
				if(rec.getFirstChild().getTextContent().equals(csvDataFetchResult.getSubRecordOf())){
					rec.appendChild(field);
					break;
				}
			}
		}
		
		return doc;
	}
	
	protected void writeToFile(Document doc, String sFilePath, String sMessageStructureName){
		try{
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			
			Transformer transformer = transformerFactory.newTransformer();
			
			DOMSource source = new DOMSource(doc);
			
			File file = new File(sFilePath + "msg_" + sMessageStructureName + ".xml");
			
			if(!file.exists()){file.createNewFile();}
			
			StreamResult result = new StreamResult(file);
			
			transformer.transform(source, result);
			
		}catch (TransformerException tfe){
			tfe.printStackTrace();
		}catch (IOException io) {
			io.printStackTrace();
		}
	}
	
	protected String getRelationship(String rltn){
		switch (rltn) {
			case RELATIONSHIP_ONE_TO_ONE   :
				return "1,1";
			case RELATIONSHIP_ONE_TO_MANY  :
				return "1,-1";
			case RELATIONSHIP_MANY_TO_MANY :
				return "-1,-1";
			case RELATIONSHIP_ZERO_TO_ONE  :
				return "0,1";
			case RELATIONSHIP_ZERO_TO_MANY :
				return "0,-1";
			default :
				return "0,1";
		}
	}
	
	protected String getDatatype(String dataType){
		
		if (dataType.equals("KEY")){
			return "12";
		}else if(dataType.equals("AN")){
			return "5";
		}else if(dataType.equals("N")){
			return "6";
		}
		return "";
	}
}
