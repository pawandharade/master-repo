package src.com.structure.csv.builder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import src.com.structure.csv.data.collect.CSVDataFetchResult;
import src.com.mapping.GenerateMessageStructure;
import src.com.util.Utilities;

public class CSVBuilder {

	protected String recordType = "", subRecordOf = "", name = "", relationship = "", dataType = "",
			optionalMandatory = "", title = "", defaultValue = "", description = "", regEx = "";

	int minLength = 0,  maxLength = 0;

	protected String sMessageStructureName = "";
	protected String sMessageStructureDescription = "";
	protected String sFilePath = "";
	public String constructCSVMessageStructure(String input, String sMessageName, String sMessageDescription) throws Exception {
		sMessageStructureName = sMessageName;
		sMessageStructureDescription = sMessageDescription;
		sFilePath = Utilities.getFileDirectoryPath(input);
		InputStream file = new FileInputStream(input);
		readExcelFile(file);
		return "SUCCESS";
	}

	protected void readExcelFile(InputStream file) throws Exception {
		// Get the workbook instance for XLS file
		Workbook wb = WorkbookFactory.create(file);
		// Get first sheet from the workbook
		Sheet sheet = wb.getSheetAt(0);

		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();
		
		List<CSVDataFetchResult> csvDataFetchResults = new ArrayList<CSVDataFetchResult>();

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			
			if (row.getRowNum() > 0) {
				
				CSVDataFetchResult csvData = new CSVDataFetchResult();
				
				// Get iterator to all cells of current row
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					
					switch (cell.getCellType()) {

						case Cell.CELL_TYPE_STRING:
							getCSVMessageStructureData(cell.getStringCellValue(), cell.getColumnIndex());
							break;
						case Cell.CELL_TYPE_NUMERIC:
							getCSVMessageStructureData("" + cell.getNumericCellValue() + "", cell.getColumnIndex());
							break;
						case Cell.CELL_TYPE_BLANK:
							getCSVMessageStructureData(cell.getStringCellValue(), cell.getColumnIndex());
							break;
					}
				} //Column Information Ends
				
				csvData = storeCSVData(); // Set Data row by row
				csvDataFetchResults.add(csvData);// add row data to result
			} //Row Information Ends
		}
		GenerateMessageStructure genMsgStruct = new GenerateMessageStructure();
		genMsgStruct.generateMessageStructure(csvDataFetchResults, sMessageStructureName, sMessageStructureDescription, sFilePath);
		file.close();
		wb.close();
	}
	
	protected CSVDataFetchResult storeCSVData(){
		CSVDataFetchResult csvData = new CSVDataFetchResult();
		csvData.setRecordType(recordType);
		csvData.setSubRecordOf(subRecordOf);
		csvData.setName(name);
		csvData.setRelationship(relationship);
		csvData.setDataType(dataType);
		csvData.setMinLength(minLength);
		csvData.setMaxLength(maxLength);
		csvData.setOptionalMandatory(optionalMandatory);
		csvData.setTitle(title);
		csvData.setDefaultValue(defaultValue);
		csvData.setDescription(description);
		csvData.setRegEx(regEx);
		return csvData;
	}
	
	protected void getCSVMessageStructureData(String cellData, int columnIndex){
		switch(columnIndex){
			case 0: //Record type (Record/Field)
				recordType = cellData;
				break;
			case 1: //Sub-Record of
				subRecordOf = cellData;
				break;
			case 2: //Record/field name
				name = cellData;
				break;
			case 3: //Relationship
				relationship = cellData;
				break;
			case 4: //Datatype
				dataType = cellData;
				break;
			case 5: //Min length
				if (Utilities.isNumeric(cellData.trim())){
					minLength = (int) Double.parseDouble(cellData.trim());
				}else{
					minLength = 0;
				}
				break;
			case 6: //Max length
				if (Utilities.isNumeric(cellData.trim())){
					maxLength = (int) Double.parseDouble(cellData.trim());
				}else{
					maxLength = -1;
				}
				break;
			case 7: //Mandatory/Optional
				optionalMandatory = cellData;
				break;
			case 8: //Title
				title = cellData;
				break;
			case 9: //Default value
				defaultValue = cellData;
				break;
			case 10: //Description
				description = cellData;
				break;
			case 11: //Regular expression
				regEx = cellData;
				break;
		}
	}
}
