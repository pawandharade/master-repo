package src.com.util;

public class Utilities {
	
	public static final String FILE_SEPARATOR = "\\";
	
	public static String getFileDirectoryPath(String filePath){
		return "" + filePath.substring(0,filePath.lastIndexOf(FILE_SEPARATOR)) + "\\";
	}
	
	public static boolean isNumeric(String value){
		try{
			double d = Double.parseDouble(value);
			return true;
		}catch(NumberFormatException nfe){
			return false;
		}
	}
}
