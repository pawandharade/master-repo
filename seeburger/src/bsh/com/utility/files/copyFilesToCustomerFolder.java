/*
 * Pattern for a new execute beanshell script
 *
 * Created on 06.08.2017
 * @Author Pawan Dharade
 * 
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

class copyFilesToCustomerFolder
{
	/*
	 * This method is called by the execute task out of a process.
	 * There is no return value, instead of this this method is static an u can use
	 * the "outStream" and the "outParams" to return values to the process.
     */
	
	/*
	 * Description : This script gets the inStream object(Price file's input stream) and save it in a local jvm's temporary location.
	 *               The temp file content will be copied to a new temporary file in another folder where customer could able to
	 *                access it only after copy operation is completed and renaming .tmp file to .txt file extension is done successfully.
	 */
	
    public static void execute(InputStream inStream,
                               OutputStream outStream,
                               HashMap inParams,
                               HashMap outParams,
                               OutputStream logStream,
                               String comment)
    throws Exception
    {
    	final String sGenericPath = inParams.get("GENERIC_PATH").toString(); //Get value from key-value pair parameter in vrExecuteRequest event message; 
    	final String tempDir      = System.getProperty("java.io.tmpdir"); //Get the local default temporary folder
    	final String tempFilename = "Price.tmp"; // Temporary file with this name will be stored in a system's temporary location for copy operation
    	final String destFilename = "Price.txt"; // Temporary file which has been copied to a particular location will be renamed with this name 
    	final String downloadFolderName = "download"; //Folder under customer's folder
    	String[] sSubDirectories = null; // To store customer folder names 
    	long subDirectoryCounter = 0, filesCopiedCounter = 0;
    	Writer w                  = null; // writer object to log script execution
    	File parentTempFile			  = null;
    	File genericDirectory = null; // It is a group directory containing multiple customer folders under it
    	try{
    		w = new OutputStreamWriter(logStream, "UTF-8");	
    		w.append("***********************Script execution starts***********************************\n");
    		parentTempFile = createTempFile(tempDir, tempFilename); //Create temporary file to system's default temporary location	
        	w.append("Temporary file created successfully with filename :" + parentTempFile.getAbsolutePath() + "\n");
    		
    		if(parentTempFile.isFile() && parentTempFile.exists()){ // Check if file has been created successfully to proceed with copy operation
    			genericDirectory   = new File(sGenericPath);
    			if (genericDirectory.exists() && genericDirectory.isDirectory()){ //Check if the given folder path exists
    				w.append("Generic directory found. Checking sub direcotries under " + sGenericPath + "\n");
    				sSubDirectories = genericDirectory.list();
    				for(String sSubDirectory : sSubDirectories){ // Travers through customer folders under generic folder
    					File subDirectory = new File(sGenericPath + "\\" + sSubDirectory);
    					if(subDirectory.exists() && subDirectory.isDirectory()){ //Check if sub directory exists
    						w.append("\n> " + (++subDirectoryCounter) + " Found sub directory " + subDirectory.getAbsolutePath() + "\n");
    						File downloadDirectory = new File(subDirectory.getAbsolutePath() + "\\" + downloadFolderName); 
    						if(downloadDirectory.exists() && downloadDirectory.isDirectory()){ //Check if download folder under sub directory exists
    							w.append("Found download directory " + downloadDirectory.getAbsolutePath() + "\n");
    							File tempDestFile = createTempFile(downloadDirectory.getAbsolutePath(), tempFilename); //Create temp file to copy content from parentTempFile
    							deleteTempFile(tempDestFile); // Delete temp file if exists
    							copy(parentTempFile.getAbsolutePath(), tempDestFile.getAbsolutePath()); // Copy tempParent file content to tempDestFile
    							w.append("TempParentFile content has been copied to TempDestFile successfully.\n");
    							File destFile = new File(tempDestFile.getParent() + "\\" + destFilename); //Change name of Price.tmp to Price.txt
    							tempDestFile.renameTo(destFile); // Rename .tmp to .txt
    							++filesCopiedCounter;
    							w.append("File renamed successfully.\nFilename : " + destFile.getAbsolutePath() + "\n");
    						}
    					}
    				}
    			}else{ //If generic directory not found
        			w.append("Generic directory not found " +  sGenericPath + "\n");
        			outParams.put("STATUS", "GENERIC_DIRECTORY_NOT_FOUND");
        		}
    		}
    		
    		w.append("\nTotal number of sub folders processed = " + subDirectoryCounter + "\n");
    		w.append("Total number of Price files copied     = " + filesCopiedCounter + "\n");
    		w.append("Script executed successfully.\n");
    	}catch(Exception e){
    		outParams.put("ERROR_MESSAGE", e.getMessage().toString());
    	}finally{
    		deleteTempFile(parentTempFile); // Delete parent temporary file from system's temporary location
    		
    		w.append("***********************Script execution ends*************************************\n");
    		w.close(); //close will auto flush data
    	}

    }
    
    public static void copy(String sourcePath, String destinationPath) throws IOException {
		FileOutputStream fos = new FileOutputStream(destinationPath);
	    Files.copy(Paths.get(sourcePath), fos);
	    fos.flush();
	    fos.close();
	}
    
    public static void deleteTempFile(File fileToBeDeleted){
    	if(fileToBeDeleted.exists()){
    		fileToBeDeleted.delete();
    		System.out.println("Temporary file has been deleted successfully. Filename : " + fileToBeDeleted.getAbsolutePath() + "\n");
    	}else{
    		System.out.println("Temporary file doe not exist. Filename : " + fileToBeDeleted.getAbsolutePath() + "\n");
    	}
    }
    
    public static File createTempFile(String tempPath, String tempFilename) {
    	File tempDir = new File(tempPath);
    	File temp    = new File(tempPath, tempFilename);

    	if (temp.exists()) {
    		temp.delete();
    	}

    	try {
    		if(tempDir.isDirectory() && tempDir.exists()){
    			temp.createNewFile();
    		}
    	} catch (IOException ex) {
    		System.out.println("An error occurred while creating temporary file: " + temp.getAbsolutePath() + "\n Error description : " + ex.getMessage() + "\n");
    	}
    	return temp;
    }

}
