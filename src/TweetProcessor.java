import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TweetProcessor
{
	/**
	 * Default Constructor
	 * @param tweetSourceFileName	Twitter Data Source
	 */
	public TweetProcessor(String tweetSourceFileName)
	{
		mTweetSourceFileName = tweetSourceFileName;
	}
	
	/**
	 * Method to read the tweet and start processing it.
	 */
	public void read()
	{
		// Set LOG FILE NAME
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd_HH_mm_ss");
		LOG_FILE_NAME = dateFormat.format(new Date()) + "_" + LOG_FILE_NAME;
		
		String encoding = "UTF-8";
		BufferedReader reader = null;

		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(mTweetSourceFileName), encoding));
		    
		    for (String line; (line = reader.readLine()) != null;)
		    {
		    	// [STEP 01] Find the actual Tweet JSON
		    	String[] parts = line.split("\\[Twitter Firehose Receiver\\]");
		    	String tweet = parts[1];
		        //printLog("<<TWEET>> : \t" + tweet);
		        
		        // [STEP 02] Find the Tweet Message
		        // TODO: SEE IF IT IS GETTING CORRECT MESSAGE, AS THERE ARE MULTIPLE KEYS WITH 'text" as name.
		        String tweetMessage = getTweetMessage(tweet);
		        
		        // [STEP 03] Pre-process the tweet message
		        String[] tokens = preprocessTweetMessage(tweetMessage);
		        
		        // [STEP 04] Next Step: Compare the tweet with prefixMap. OUTPUT: Map<nodeID, score>
		        
		        // [STEP 05] Next Step: Filter the mentions from the previous step. using a threshold. OUTPUT: Map<nodeID, score>
		        //		         filterMentions(threshold);
		        
		        // [STEP 06] Next Step: Update List<NodeName, List<TweetID>, cumulativeScore> 
		        
		        printLog("\n*************************************************\n");
		        //break;	// TODO: Processing just one tweet for now.
		    }
		    
		    // Here, you'd have got FINAL List<NodeName, List<TweetID>, cumulativeScore> , which 
		    // we can compare to the query of the user, and print tagCloud.
		} 
		catch (UnsupportedEncodingException | FileNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("File [" + AppConstants.TWITTER_DATA_FILE +"] not found!");
        } 
		catch (IOException e)
        {
	        e.printStackTrace();
        }
		finally
		{
		    try
            {
	            reader.close();
            } 
		    catch (IOException e)
            {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
            }
		}
	}
	
	/**
	 * JSON Library: https://code.google.com/p/json-simple/
	 * @param tweet
	 * @return
	 */
	public String getTweetMessage(String tweet)
	{
		String tweetMessage = null;
		
		/* See <https://code.google.com/p/json-simple/wiki/DecodingExamples>
		 * for more decoding examples.
		 */
		try
		{
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(tweet);
			tweetMessage = (String) obj.get("text");
			
			// Logging Tweet Message
			printLog("<<TWEET MESSAGE>> : \t" + tweetMessage);
		}
		catch(ParseException pe)
		{
		    System.out.println("position: " + pe.getPosition());
		    System.out.println(pe);
		}
		
		return tweetMessage;
	}
	
	/**
	 * Method to Preprocess the Tweet Message. Remove unnecessary words.
	 * @param tweetMessage
	 * @return
	 */
	public String[] preprocessTweetMessage(String tweetMessage)
	{
		// TODO: Return null if the language is not English.
		
		// Tokenize the strings using a set of Delimiters.
		String tokens[] = null;
		if(tweetMessage != null)
		{
			tokens = tweetMessage.split(AppConstants.TWEET_DELIMITER_STRING);
			
			// Logging Pre-processed Tweet
		    if((AppConstants.PRINT_DEST != AppConstants.TO_NONE) && tokens != null)
			{
		    	String outputString = "";
		        for(String token: tokens) 
		        	outputString += token + " ";
		        outputString = "<<PREPROCESSED>> : \t" + outputString;
		        
		        printLog(outputString);
			}
		}
		
		return tokens;
	}
	
	/**
	 * Method to filter the mentions.
	 */
	private void filterMentions(double threshold)
	{
		if(mCurrentMentions != null && mCurrentMentions.size() > 0)
		{
			for (Map.Entry<Long, Double> entry : mCurrentMentions.entrySet())
			{
				if(entry.getValue().compareTo(threshold) < 0)
					mCurrentMentions.remove(entry);
			}
		}
	}
	
	/**
	 * Method to print any data in the log files.
	 */
	private void printLog(String text)
	{
		AppUtils.printLog(LOG_FILE_NAME, text);
	}
	
	// Member Variables
	private static String LOG_FILE_NAME = "tweet_log.txt";
	
	private Map<Long, Double> mCurrentMentions = null;
	
	
	private String mTweetSourceFileName = null;
	
}
