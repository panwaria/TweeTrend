/**
 * Class to store application wide constants.
 */
public class AppConstants
{
	// DEBUGGING Related
	public static final int PROCESSING_TWEET_LIMIT = 1000;
	public static final boolean PRINT_TO_CONSOLE_ALLOWED = false;
	
	// TAXONOMY Related
	public static final String TAXONOMY_SOURCE_XML = "C:\\Tweets\\Taxonomy_Movies.xml";
	public static final String TAXONOMY_DELIMITER_STRING = "\\s+|:|,|\\.|&|-";
	public static final String GO_WORDS_SOURCE_FILE = "src//go_words.dat";
	
	// TWEETS Related
	public static final String TWITTER_DATA_FILE = "C:\\Tweets\\twitter_20110731-12.log";
	public static final String TWEET_DELIMITER_STRING = "\\s+|:|,|\\.|&|-|\\?";
	
	// DEBUGGING Related
	public static final String LOGS_DIRECTORY_NAME = "logs";
	public static final int TO_NONE = 0;
	public static final int TO_CONSOLE = 1;
	public static final int TO_FILE = 2;
	public static final int PRINT_DEST = TO_FILE;

}
