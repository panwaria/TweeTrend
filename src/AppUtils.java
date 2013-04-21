import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class AppUtils
{
	public static void printLog(String fileName, String text)
	{
	    if(AppConstants.PRINT_DEST == AppConstants.TO_CONSOLE)
	    {
	    	System.out.println(text);
	    }
	    else if (AppConstants.PRINT_DEST == AppConstants.TO_FILE)
	    {
	    	AppUtils.printToFile(fileName, text);
	    }
	}
    
	public static void printToFile(String fileName, String text)
	{
		if(!doLogsDirectoryExist())
			return;
			
        PrintWriter writer = null;
        try
        {
	        writer = new PrintWriter(new FileWriter(AppConstants.LOGS_DIRECTORY_NAME + "\\" + fileName, true));
        } 
        catch (IOException e)
        {
	        e.printStackTrace();
        } 

    	if(writer != null)
        {
    		writer.println(text);
    		writer.close();
        }
    }
	
	private static boolean doLogsDirectoryExist()
	{
		File f = new File(AppConstants.LOGS_DIRECTORY_NAME);
		
		if(f.exists()) return true;

		try
		{
			if(f.mkdir()) return true; 
			else return false; 	
		}
		catch (Exception e)
		{
			return false;
		}
	}
}
