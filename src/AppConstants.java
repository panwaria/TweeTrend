/**
 * Class to store application wide constants.
 */
public class AppConstants
{
	
	// TAXONOMY Related
	//public static final String TAXONOMY_SOURCE_XML = "B:\\Codebase\\ws_cs784\\TweeTrend\\src\\Taxonomy_Movies.xml";
	public static final String TAXONOMY_SOURCE_XML = "C:\\Tweets\\Taxonomy_Movies.xml";
	
	public static final String TAXONOMY_DELIMITER_STRING = "\\s+|:|,|\\.|&|-";
	
	// TWEETS Related
	//public static final String TWITTER_DATA_FILE = "C:\\Users\\Prakhar\\Desktop\\Spring 2013\\cs784\\ProjectWork\\TwitterData\\twitter_20110731-12.log";
	public static final String TWITTER_DATA_FILE = "C:\\Tweets\\twitter_20110731-12.log";
	public static final String TWEET_DELIMITER_STRING = "\\s+|:|,|\\.|&|-|\\?";
	
	// DEBUGGING Related
	public static final String LOGS_DIRECTORY_NAME = "logs";
	public static final int TO_NONE = 0;
	public static final int TO_CONSOLE = 1;
	public static final int TO_FILE = 2;
	public static final int PRINT_DEST = TO_FILE;

}
