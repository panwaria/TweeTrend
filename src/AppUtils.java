import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AppUtils
{
	public static void println(String text)
	{
		if(AppConstants.PRINT_TO_CONSOLE_ALLOWED)
			System.out.println(text);
	}

	public static void print(String text)
	{
		if(AppConstants.PRINT_TO_CONSOLE_ALLOWED)
			System.out.print(text);
	}
	
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
	
	public static double getRoundedVal(double val)
	{
		DecimalFormat df = new DecimalFormat("0.00");
		String formate = df.format(val); 
		double finalValue = -1.0;
        try
        {
            finalValue = Double.parseDouble(formate);
        } 
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        
        return finalValue;
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
	
	public static Trie generateGoWordsTrie(String fileName)
	{
		Trie goWordsTrie = new Trie();
		
		Scanner fileScanner = null;
		try 
		{
			fileScanner = new Scanner(new File(fileName));
		} 
		catch(FileNotFoundException e) 
		{
			System.err.println("File- '" + fileName + "' not found!");
			return null;
		}
		
		while(fileScanner.hasNext()) 
		{
			String line = fileScanner.nextLine().trim();
			
			// Skip the comments or the blank lines.
			if(line.length() == 0 || line.startsWith("//")) 
				continue;
			
			String[] parts = line.split("\\s+");
			
			double val = -1.0;
			try
			{
				val = Double.parseDouble(parts[1]);
			}
			catch(NumberFormatException e)
			{
				continue;
			}
			
			goWordsTrie.insert(parts[0], val);
		}
		
		fileScanner.close();
		
		return goWordsTrie;
	}
	
	public static Map<String, Double> generateGoWordsMap(String fileName)
	{
		Map<String, Double> goWordsMap = null;

		Scanner fileScanner = null;
		try 
		{
			fileScanner = new Scanner(new File(fileName));
		} 
		catch(FileNotFoundException e) 
		{
			System.err.println("File- '" + fileName + "' not found!");
			return null;
		}
		
		goWordsMap = new HashMap<String, Double>();
		
		while(fileScanner.hasNext()) 
		{
			String line = fileScanner.nextLine().trim();
			
			// Skip the comments or the blank lines.
			if(line.length() == 0 || line.startsWith("//")) 
				continue;
			
			String[] parts = line.split("\\s+");
			
			double val = -1.0;
			try
			{
				val = Double.parseDouble(parts[1]);
			}
			catch(NumberFormatException e)
			{
				continue;
			}
			
			goWordsMap.put(parts[0], val);
		}
		
		fileScanner.close();
		
		return goWordsMap;
	}
	
	public static void printGoWordsMap(Map<String, Double> goWordsMap)
	{
		println("\n--------------------------\nPRINTING GO-WORDS MAP\n--------------------------\n");

		if(goWordsMap != null && goWordsMap.size() > 0)
		{
			for (Map.Entry<String, Double> entry : goWordsMap.entrySet())
				println("[" + entry.getKey() + ", " + entry.getValue() + "]");
		}
		else
			println("No entries in the Go-Words Map!\n");
	}
	
	public static Double normalizeValues(Double a, Double b)
	{
		return (a + b - (a*b));
	}
	
	public static Trie generateEnglishWordsTrie(String fileName)
	{
		Trie englishWordsTrie = new Trie();
		
		Scanner fileScanner = null;
		try 
		{
			fileScanner = new Scanner(new File(fileName));
		} 
		catch(FileNotFoundException e) 
		{
			System.err.println("File- '" + fileName + "' not found!");
			return null;
		}
		
		while(fileScanner.hasNext()) 
		{
			String line = fileScanner.nextLine().trim();
			
			// Skip the comments or the blank lines.
			if(line.length() == 0 || line.startsWith("//")) 
				continue;
			
			englishWordsTrie.insert(line);
		}
		
		fileScanner.close();
		
		return englishWordsTrie;
	}
	
	public static int getTagCloudLevel(double score, double[] thresholdArr)
	{
		int index = 0;
		for(double threshold : thresholdArr)
		{
			if(score > threshold)
				return 10 - index;
			
			index++;
		}
		
		return -1;
	}
	
	public static int getTagCloudLevel(double score, double min, double max)
	{
		double normTotal = max - min;
		double normScore = (score - min) / normTotal;
		
		if (normScore < 0.0 || normScore > 1.0)
		{
			System.err.println("Invalid Score:" + score);
			return -1;
		}
		
		int level =  (int) (normScore * 10);
		
		return level + 1;
	}
}
