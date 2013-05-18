import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.Genre;
import com.omertron.themoviedbapi.model.MovieDb;

import twitter4j.*;


public class GoldenDataBuilder
{

	private static Trie mEnglishWordsTrie = null;
	private static Twitter mTwitter;
	private static TheMovieDbApi mTmdb;

	/**
	 * @param args
	 * @throws TwitterException 
	 * @throws MovieDbException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws TwitterException, MovieDbException, IOException
	{
		mTwitter = TwitterFactory.getSingleton();
		mEnglishWordsTrie  = AppUtils.generateEnglishWordsTrie(AppConstants.ENGLISH_WORDS_FILE);
	    
		createGoldenDataFile("", AppConstants.GOLDEN_DATA_LOCATION + "\\" + "golden_tweets" + ".xml");
    }
	
	private static void createGoldenDataFile(String genreName, String fileName) throws TwitterException, IOException
	{
    	File file = new File(fileName);
		
    	FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		Twitter twitter = new TwitterFactory().getInstance(); 
		
		Query query = new Query("Bardem Cruz");
		
		query.setCount(100);
	    QueryResult result = mTwitter.search(query);
	    for (Status status : result.getTweets()) {
	    	String tweet = status.getText();
	    	if(isEnglish(tweet.split(AppConstants.TWEET_DELIMITER_STRING)))
	    		System.out.println(tweet);
	    }
	    
		/*
		for(int page = 1; page <= 50; page++)
		{
			Paging paging = new Paging(page);
			List<Status> statuses = twitter.getUserTimeline("Movies", paging); 
			for (Status status : statuses) {
		    	String tweet = status.getText();
		    	if(isEnglish(tweet.split(AppConstants.TWEET_DELIMITER_STRING)))
		    		bw.write("<tweet>\n\t" + tweet + "\n</tweet>\n");
		    }
		}
		*/
	    
	    bw.close();
	}
	
	
	private static boolean isEnglish(String[] tokens)
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
}
