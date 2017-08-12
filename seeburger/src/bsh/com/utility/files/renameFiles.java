/*
 * Pattern for a new execute beanshell script
 *
 * Created on 04.08.2017
 * @Author Pawan Dharade
 * Copyright (c) 2017 SEEBURGER AG, Germany. All Rights Reserved.
 */

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.UUID;

class renameFiles
{
	/*
	 * This method is called by the execute task out of a process.
	 * There is no return value, instead of this this method is static an u can use
	 * the "outStream" and the "outParams" to return values to the process.
	 * 
	 * The below code searches file with name as Price.txt and rename it by appending unique auto-generated id
	 * files are residing in multiple customer directories on server location and will be renamed if exists - 
	 * as a result, existing file won't be overwritten by newly received file to respective customer directories.
     */
    public static void execute(InputStream inStream,
                               OutputStream outStream,
                               HashMap inParams,
                               HashMap outParams,
                               OutputStream logStream,
                               String comment)
    throws Exception
    {
    	final String sDownloadFolderName = "download", sPriceFilenameToBeRenamed = "Price.txt";
    	String sGenericPath = ""; 
    	String[] sSubDirectories = null;  
    	long subDirectoryCounter = 0, renamedFilesCounter = 0;
    	File renamedFile = null, genericDirectory = null;
    	
    	Writer w = new OutputStreamWriter(logStream, "UTF-8"); // writer object to log script execution
    	w.append("***********************Script execution starts***********************************\n");
    	
    	try{
    		sGenericPath       = inParams.get("GENERIC_PATH").toString(); //Get value from key-value pair parameter in vrExecuteRequest event message
    		genericDirectory   = new File(sGenericPath);
    		if (genericDirectory.exists() && genericDirectory.isDirectory()){ //Check if the given folder path exists
    			w.append("Generic directory found. Checking sub direcotries under " + sGenericPath + "\n");
    			sSubDirectories = genericDirectory.list();
    			for(String sSubDirectory : sSubDirectories){
    				File subDirectory = new File(sGenericPath + "\\" + sSubDirectory);
    				if(subDirectory.exists() && subDirectory.isDirectory()){ //Check if sub directory exists
    					w.append("\n> " + (++subDirectoryCounter) + " Found sub directory " + subDirectory.getAbsolutePath() + "\n");
    					File downloadDirectory = new File(subDirectory.getAbsolutePath() + "\\" + sDownloadFolderName); 
    					if(downloadDirectory.exists() && downloadDirectory.isDirectory()){ //Check if download folder under sub directory exists
    						w.append("Found download directory " + downloadDirectory.getAbsolutePath() + "\n");
    						File priceFileToBeRenamed = new File(downloadDirectory.getAbsolutePath() + "\\" + sPriceFilenameToBeRenamed);
    						if(priceFileToBeRenamed.exists() && priceFileToBeRenamed.isFile()){ //Check if Price.txt exist in the download directory
    							w.append("Found existing file with name as " + priceFileToBeRenamed.getAbsolutePath() + "\n");
    							renamedFile = new File(priceFileToBeRenamed.getParent() +"\\" + "Price_" + getUniqueID() + ".txt");
    							if(priceFileToBeRenamed.renameTo(renamedFile)){ //Renaming file
    								++renamedFilesCounter;
    								w.append("Existing file has been renamed successfully as " + renamedFile.getAbsolutePath() + "\n");
    							}else{
    								w.append("Failed to rename file " + priceFileToBeRenamed.getAbsolutePath() + "\n");
    							}
    						}
    					}
    				}
    			}
    		}else{ //If generic directory not found
    			w.append("Generic directory not found " +  sGenericPath + "\n");
    			outParams.put("RENAME_STATUS", "GENERIC_DIRECTORY_NOT_FOUND");
    		}
    		w.append("\nTotal number of sub folders processed = " + subDirectoryCounter + "\n");
    		w.append("Total number of Price files renamed     = " + renamedFilesCounter + "\n");
    		outParams.put("RENAME_STATUS", "SUCCESS");
    	}catch(Exception e){
    		outParams.put("RENAME_STATUS", "EXCEPTION_OCCURRED");
    		outParams.put("ERROR_MESSAGE", e.getMessage().toString());
    	}finally{	
    		w.append("***********************Script execution end***************************************\n");
    		w.close(); //close will auto flush data
    	}
    }
    
    private static String getUniqueID(){
    	return String.valueOf(UUID.randomUUID());
    }

}