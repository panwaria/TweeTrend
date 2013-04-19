import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TweetReader
{
	public static void read()
	{
		String encoding = "UTF-8";
		BufferedReader reader = null;

		try
		{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(AppConstants.TWITTER_DATA_FILE), encoding));
		    
		    for (String line; (line = reader.readLine()) != null;)
		    {
		    	// Find the actual Tweet JSON
		    	String[] parts = line.split("\\[Twitter Firehose Receiver\\]");
		    	String tweet = parts[1];
		        System.out.println("TWEET: \t" + tweet);
		        
		        // Find the Tweet Message
		        // TODO: SEE IF IT IS GETTING CORRECT MESSAGE, AS THERE ARE MULTIPLE KEYS WITH 'text" as name.
		        String tweetMessage = getTweetMessage(tweet);
		        System.out.println("TWEET MESSAGE: \t" + tweetMessage);
		        
		        // Pre-process the tweet message
		        String[] tokens = preprocessTweetMessage(tweetMessage);
		        if(tokens != null)
	        	{
		        	String outputString = "";
			        for(String token: tokens) 
			        {
			        	outputString += token + " ";
			        }
			        System.out.println("PREPROCESSED TWEET MESSAGE: \t" + outputString);
	        	}
		        
		        // Next Step: Compare the tweet with prefixMap
		        
		        System.out.println();
		        //break;	// TODO: Processing just one tweet for now.
		    }
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
	public static String getTweetMessage(String tweet)
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
		}
		catch(ParseException pe)
		{
		    System.out.println("position: " + pe.getPosition());
		    System.out.println(pe);
		}
		
		return tweetMessage;
	}
	
	public static String[] preprocessTweetMessage(String tweetMessage)
	{
		if(tweetMessage != null)
			return tweetMessage.split(AppConstants.TWEET_DELIMITER_STRING);

		return null;
	}
	
}
