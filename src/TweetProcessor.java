import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


/**
 * TODOs:
 * 1. See if it important to normalize values, as most of them are reaching 0.99999.
 * 2. Class to read nodescore from file and display output.
 * 3. Integrate OpenCloud Library.
 * 
 * 1. [OPTIMIZATION] Find part of speech, and we can pass on the 'nouns' to check for the movie rather than all the tokens/
 * 2. Give more weight to the node if it appears in #hashtag
 *
 */
public class TweetProcessor
{
	
	private static final long MAX_LIMIT = 200000;
	private static final long MIN_LIMIT = 0;

	
	/**
	 * Default Constructor
	 * @param tweetSourceFileName	Twitter Data Source
	 */
	public TweetProcessor(TaxonomyTree taxonomyTree, String tweetSourceFileName)
	{
		// Variables from Main Class
		mTaxonomyTree = taxonomyTree;
		mNodeNameArray = mTaxonomyTree.getTaxonomyNodeNameArray();
		mTweetSourceFileName = tweetSourceFileName;
		
		// Initialize data structures to store mentions needed to evaluate user's query.
		mCurrentMentions = new HashMap<Long, Double>();
		mTaxonomyNodeScoreMap = new HashMap<String, TaxonomyNodeScore> ();
		
		// Create Go-Words Trie for multiplication factor
		mGoWordsTrie = AppUtils.generateGoWordsTrie(AppConstants.GO_WORDS_SOURCE_FILE);
		System.out.println("CHECKPOINT: GoWords Trie Generated.");
		//AppUtils.printGoWordsMap(mGoWordsMap);
		
		mEnglishWordsTrie = AppUtils.generateEnglishWordsTrie(AppConstants.ENGLISH_WORDS_FILE);
		System.out.println("CHECKPOINT: English Dictionary Trie Generated.");
		
	}
	
	/**
	 * Method to read the tweet and start processing it.
	 */
	private void processTweets()
	{
		// Set LOG FILE NAME
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd_HH_mm_ss");
		Date date = new Date();
		LOG_FILE_NAME = dateFormat.format(date) + "_" + LOG_FILE_NAME;
		
		String encoding = "UTF-8";
		BufferedReader reader = null;
		
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(mTweetSourceFileName), encoding));
		    
			long numTweets = 0;
			
			for (String line; (line = reader.readLine()) != null /*&& numTweets < MAX_LIMIT*/; numTweets++)
		    {
		    	if(numTweets < MIN_LIMIT) continue;
		    	
		    	resetCurrentMentions();
		    	// [STEP 01] Find the actual Tweet JSON
		    	try
		    	{
			    	String[] parts = line.split("\\[Twitter Firehose Receiver\\]");
			    	String tweet = parts[1];
			        //printLog("<<TWEET>> : \t" + tweet);
			    	
			    	// [STEP 01_05] Get TweetID
			    	String tweetID = ""; // TODO: Implement getTweetID(tweet);
			    	
			    	
			        // [STEP 02] Find the Tweet Message
			        // TODO: SEE IF IT IS GETTING CORRECT MESSAGE, AS THERE ARE MULTIPLE KEYS WITH 'text" as name.
			        String tweetMessage = getTweetMessage(tweet);
			        if((tweetMessage == null) || (tweetMessage.equals("")))
			        	continue;
			        
			        // [STEP 03] Pre-process the tweet message
			        ArrayList<String> normalTokens = new ArrayList<String>();
			        ArrayList<String> hashTokens = new ArrayList<String>();
			        preprocessTweetMessage(tweetMessage, normalTokens, hashTokens);
			        
			        if(normalTokens.size() == 0 && hashTokens.size() == 0) continue;

			        //System.out.println(tweetMessage + "\n");
			        
			        //String webContext = getWebContext(tweetMessage);
			        //String[] webTokens = preprocessTweetMessage(webContext);
			        
			        // [STEP 04] Next Step: Compare the tweet with prefixMap. OUTPUT: Map<nodeID, score>
			        extractMentions(normalTokens, false);
			        extractMentions(hashTokens, true);
			        //extractMentions(webTokens);
			        //AppUtils.println("After Simply Extracting Mentions");
			        //printCurrentMentions();
			        
			        // [STEP 04_05] Next Step: Get Multiplication Factor and apply it on current mentions
			        double mulFactor = getMultiplicationFactor(normalTokens, false);
			        applyMultiplicationFactor(mulFactor);
			        
			        mulFactor = getMultiplicationFactor(hashTokens, true);
			        applyMultiplicationFactor(mulFactor);

			        //AppUtils.println("After Applying Multiplication Factor");
			        //printCurrentMentions();
			        
			        // [STEP 05] Next Step: Filter the mentions from the previous step. using a threshold. OUTPUT: Map<nodeID, score>
			        filterMentions(THRESHOLD_VAL);
			        
			        AppUtils.println("After Filtering Mentions");
			        printCurrentMentions();
			        
			        // [STEP 06] Next Step: Update List<NodeName, List<TweetID>, cumulativeScore> 
			        updateTaxonomyNodeScoreMap(tweetID);
			        
//			        printLog("\n*************************************************\n");
			        //break;	// TODO: Processing just one tweet for now.
		    	}
		    	catch (IndexOutOfBoundsException e)
		    	{
		    		// Just ignore this tweet
		    		continue;
		    	}
		    }
		    
		    // Here, you'd have got FINAL List<NodeName, List<TweetID>, cumulativeScore> , which 
		    // we can compare to the query of the user, and print tagCloud.
		    printFinalTaxonomyNodeScoreMapToResultFile();
//	        printFinalTaxonomyNodeScoreMap();
	        AppUtils.println("We processed " + numTweets + " tweets!!");

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
		
		Date newDate = new Date();
		double diffMinutes = (newDate.getTime() - date.getTime())/ ((double)1000 * 60);
		printLog("Total Time Taken: " + diffMinutes);
	}
	
	private boolean isEnglish(String[] tokens)
	{
		int numEnglishWords = 0;
		for(String token : tokens)
		{
			if(mEnglishWordsTrie.containsStrict(token))
				numEnglishWords++;
		}
		
		if(numEnglishWords > tokens.length / 2)
			return true;
		
		return false;
	}

	/**
	 * Get Web Context of the message.
	 * TODO: We can scale this method to go through *all* the URLs of the tweet.
	 */
	private String getWebContext(String tweetMessage)
	{
		// Separate input by spaces ( URLs don't have spaces )
        String [] parts = tweetMessage.split("\\s");

        // Attempt to convert each item into an URL.   
        for(String item : parts) 
        try
        {
            URL url = new URL(item);
            
            // If possible then replace with anchor...
            AppUtils.print("URL FOUND---------->>>> " + url + "\n");//<a href=\"" + url + "\">"+ url + "</a> " ); 
            
        	try 
        	{
        		Document doc = Jsoup.connect(url.toString())
          			.timeout(10*1000)
          			.get();
        		
        		if(doc != null)
        		{
        			String title = doc.title();
        			AppUtils.print("TITLE---------->>>> " + title + "\n");
        			
        			return title;
        		}
        	}
	    	catch (IOException | IllegalArgumentException e) 
	    	{
	            continue;	// Go to next URL, if any.
	    	}
        } 
        catch (MalformedURLException e)
        {
            // Just ignore, see if next item is a URL.
        }

		return null;
	}
	
	/**
	 * JSON Library: https://code.google.com/p/json-simple/
	 * @param tweet
	 * @return
	 */
	private String getTweetMessage(String tweet)
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
			//printLog("<<TWEET MESSAGE>> : \t" + tweetMessage);
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
	private void preprocessTweetMessage(String tweetMessage, ArrayList<String> normalTokens, ArrayList<String> hashTokens)
	{
		if(tweetMessage == null) return;
		
		// TODO: Return null if the language is not English.
		
		// Tokenize the strings using a set of Delimiters.
		String[] tokens = tweetMessage.split(AppConstants.TWEET_DELIMITER_STRING);
		
		if(!isEnglish(tokens)) return;
		
		for(String token : tokens)
		{
			if(token.startsWith("#"))
			{
				hashTokens.add(token.substring(1));
				continue;
			}
			
			if(!token.startsWith("@") || !token.equals("RT"))
				normalTokens.add(token);				
		}
		
		// Logging Pre-processed Tweet
	    if((AppConstants.PRINT_DEST != AppConstants.TO_NONE) && tokens != null)
		{
	    	String outputString = "";
	        for(String token: normalTokens) 
	        	outputString += token + " ";
	        outputString = "<<PREPROCESSED NORMAL>> : \t" + outputString;
	        
	    	outputString = "";
	        for(String token: hashTokens) 
	        	outputString += token + " ";
	        outputString = "<<PREPROCESSED HASH>> : \t" + outputString;
	        
	        //printLog(outputString);
		}
	}
	
	private void resetCurrentMentions()
	{
		mCurrentMentions.clear();
	}
	
	/**
	 * Method to extract mentions.
	 * 
	 * @param tokens	Tokens from tweet
	 */
	private void extractMentions(ArrayList<String> tokens, boolean isHash)
	{
		double SCORE = isHash ? 1.0 : 0.5;
		
        TaxonomyPrefixMap prefixMap = TaxonomyPrefixMap.getPrefixMap();
        String currentToken = "";
        
        for(int curIndex = 0; curIndex < tokens.size(); curIndex++)
        {
        	String token = tokens.get(curIndex);
        	
        	if(!currentToken.equals(""))
        		currentToken += " ";
        	currentToken += token;
        	TaxonomyPrefixMapValue a = prefixMap.retrieve(currentToken);

        	if( a!= null && a.getNodeId() != -1)
    		{
    			AppUtils.println("Mention Found: token-[" + token + "]");
    			mCurrentMentions.put(a.getNodeId(), SCORE);
    			
    			if(a.isLast())
    			{
    				currentToken = ""; 
    				continue;
    			}
    			
    			for (int i = curIndex + 1; i < tokens.size(); i++)
    			{
    				String newToken = tokens.get(i);
    				
    	        	currentToken += " ";
    	        	currentToken += newToken;
    	        	
    	        	TaxonomyPrefixMapValue b = prefixMap.retrieve(currentToken);
    	        	
    	        	if(b!= null && b.getNodeId() != -1)
    	        	{
    	        		AppUtils.println("Mention Found: token-[" + currentToken + "]");
            			mCurrentMentions.put(b.getNodeId(), SCORE);
            			
    	        		if(b.isLast())
    	        			break;
    	        	}
    	        	else
    	        		break;
    	        	
    			}
    		}
        	currentToken = "";
        }

	}
	
	private void printCurrentMentions()
	{
		AppUtils.println("-------------------------------------------");
		AppUtils.println("PRINTING CURRENT MENTIONS");
		AppUtils.println("-------------------------------------------\n");

		if(mCurrentMentions.size() > 0)
		{
			for (Map.Entry<Long, Double> entry : mCurrentMentions.entrySet())
			{
				AppUtils.println("[" + entry.getKey() + ", " + entry.getValue() + "]");
			}
		}
		else
			AppUtils.println("No current mentions till now!\n");
	}
	
	/**
	 * TODO: Make it more efficient. Do we really need Map for GoWords?
	 * @param tokens
	 * @return
	 */
	private double getMultiplicationFactor(ArrayList<String> tokens, boolean isHash)
	{
		if(tokens == null || tokens.size() <= 0)
			return 0.0;

		double mulFactor = 0.0;
		double defaultMulFactor = mGoWordsTrie.getDefaultValue();
		boolean goWordFound = false;
		
		/*
		for (Map.Entry<String, Double> entry : mGoWordsMap.entrySet())
		{
			String goWord = entry.getKey();
			Double goWordScore = entry.getValue();
			
			boolean goWordFound = false;
			for(String token : tokens)
			{
				if(token.startsWith(goWord))
				{
					mulFactor = AppUtils.normalizeValues(mulFactor, goWordScore);
					goWordFound = true;
				}
			}
			
			if(!goWordFound)	// If goWord is not found, then the multiplication factor should be even less.
				mulFactor = AppUtils.normalizeValues(mulFactor, 1 - goWordScore);
		}
		*/
		
		for(String token : tokens)
		{
			double goWordScore = mGoWordsTrie.get(token);
			if(goWordScore != 0.0)
			{
				mulFactor = AppUtils.normalizeValues(mulFactor, goWordScore);
				goWordFound = true;
			}
		}
		
		if(goWordFound)
		{
			if(isHash)
				return 2*mulFactor;
			else
				return mulFactor;
		}
		
		return defaultMulFactor;
	}
	
	/**
	 * Apply Multiplication Factor
	 * @param mulFactor
	 */
	private void applyMultiplicationFactor(Double mulFactor)
	{
		if(mCurrentMentions != null && mCurrentMentions.size() > 0)
		{
			for (Map.Entry<Long, Double> entry : mCurrentMentions.entrySet())
				mCurrentMentions.put(entry.getKey(), mulFactor * entry.getValue());
		}
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
	 * Method to update Final TaxonomyNode Score Map.
	 * @param tweetID
	 */
	private void updateTaxonomyNodeScoreMap(String tweetID)
	{
		if(mCurrentMentions != null && mCurrentMentions.size() > 0)
		{
			for (Map.Entry<Long, Double> entry : mCurrentMentions.entrySet())
			{
				long nodeID = entry.getKey();
				String nodeName = mNodeNameArray[(int) nodeID];
				
				if(mTaxonomyNodeScoreMap.containsKey(nodeName))
				{
					// Update List of TweetIDs associated with the node -- as Metadata for TagCloud
					mTaxonomyNodeScoreMap.get(nodeName).mTweetIDList.add(tweetID);
					
					// Update nodeScore -- This will act as weight of the word in the TagCloud.
					double oldScore = mTaxonomyNodeScoreMap.get(nodeName).mNodeScore;
					mTaxonomyNodeScoreMap.get(nodeName).mNodeScore = AppUtils.normalizeValues(oldScore,  entry.getValue());
				}
				else
				{
					TaxonomyNodeScore taxonomyNodeScore = new TaxonomyNodeScore();
					taxonomyNodeScore.mNodeScore = entry.getValue();
					
					mTaxonomyNodeScoreMap.put(nodeName, taxonomyNodeScore);
				}
			}
		}
	}
	
	private void printFinalTaxonomyNodeScoreMapToResultFile()
	{
		if(mTaxonomyNodeScoreMap.size() > 0)
		{
			for (Map.Entry<String, TaxonomyNodeScore> entry : mTaxonomyNodeScoreMap.entrySet())
			{
				AppUtils.printToFile(TAXONOMY_NODE_SCORE_FILENAME, entry.getKey() + "~~" + entry.getValue().mNodeScore);
			}
		}
	}
	
	/**
	 * Print Final Taxonomy Node Score Map
	 */
	private void printFinalTaxonomyNodeScoreMap()
	{
		printLog("-------------------------------------------");
		printLog("PRINTING FINAL **TAXONOMY NODE SCORE** MAP");
		printLog("-------------------------------------------\n");

		if(mTaxonomyNodeScoreMap.size() > 0)
		{
			for (Map.Entry<String, TaxonomyNodeScore> entry : mTaxonomyNodeScoreMap.entrySet())
			{
				printLog("[" + entry.getKey() + ", " + entry.getValue().mNodeScore + "]");
			}
		}
		else
			printLog("No entries in the Taxonomy Node Score Map!\n");
	}
	
	public Map<String, TaxonomyNodeScore> getTaxonomyNodeScoreMap()
	{
		boolean needToProcessTweets = false;
		
		Scanner fileScanner = null;
		try 
		{
			fileScanner = new Scanner(new File(AppConstants.LOGS_DIRECTORY_NAME + "\\" + TAXONOMY_NODE_SCORE_FILENAME));
		} 
		catch(FileNotFoundException e) 
		{
			System.err.println("File- '" + TAXONOMY_NODE_SCORE_FILENAME + "' not found!");
			needToProcessTweets = true;
		}
		
		if(!needToProcessTweets)
		{
			while(fileScanner.hasNext()) 
			{
				String line = fileScanner.nextLine().trim();
				
				// Skip the comments or the blank lines.
				if(line.length() == 0 || line.startsWith("//") || !line.contains("~~")) 
					continue;
				
				String[] parts = line.split("~~");
				
				double val = -1.0;
				try
				{
					val = Double.parseDouble(parts[1]);
				}
				catch(NumberFormatException e)
				{
					needToProcessTweets = true;
					break;
				}
				
				TaxonomyNodeScore taxonomyNodeScore = new TaxonomyNodeScore();
				taxonomyNodeScore.mNodeScore = val;
				mTaxonomyNodeScoreMap.put(parts[0], taxonomyNodeScore);
			}
			
			fileScanner.close();
		}
		
		if(needToProcessTweets || mTaxonomyNodeScoreMap.size() == 0)
		{
			System.out.println("CHECKPOINT: Processing Tweets");
			
			mTaxonomyNodeScoreMap.clear();
			processTweets();
		}
//		else
//			printFinalTaxonomyNodeScoreMap();
		
		return mTaxonomyNodeScoreMap;
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
	private static final String TAXONOMY_NODE_SCORE_FILENAME = "taxonomy_node_score.dat";
	private double THRESHOLD_VAL = 0.0;
	
	//private Map<String, Double> mGoWordsMap = null;
	
	private Map<Long /*nodeID*/, Double /*score*/> mCurrentMentions = null;
	private Map<String, TaxonomyNodeScore> mTaxonomyNodeScoreMap = null;
	
	private TaxonomyTree mTaxonomyTree = null;
	private String[] mNodeNameArray = null;
	
	private String mTweetSourceFileName = null;
	
	private Trie mGoWordsTrie = null;
	private Trie mEnglishWordsTrie = null;
}
