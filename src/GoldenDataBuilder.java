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
		mTmdb = new TheMovieDbApi(AppConstants.TMDB_API_KEY);
		mTwitter = TwitterFactory.getSingleton();
		mEnglishWordsTrie  = AppUtils.generateEnglishWordsTrie(AppConstants.ENGLISH_WORDS_FILE);
	    
		List<Genre> allGenres = mTmdb.getGenreList("");
        for(Genre genre : allGenres)
        	createGoldenDataFile(genre.getName(), AppConstants.GOLDEN_DATA_LOCATION + "\\" + genre.getName() + ".xml");
    }
	
	private static void createGoldenDataFile(String genreName, String fileName) throws TwitterException, IOException
	{
    	File file = new File(fileName);
		
    	/*
		if (!file.exists())
			file.createNewFile();
		else
			return;
		*/
    	
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		Query query = new Query(genreName);
	    query.setCount(100);
	    QueryResult result = mTwitter.search(query);
	    
	    for (Status status : result.getTweets()) {
	    	String tweet = status.getText();
	    	if(isEnglish(tweet.split(AppConstants.TWEET_DELIMITER_STRING)))
	    		bw.write("<tweet>\n\t" + tweet + "\n</tweet>\n");
	    }
	    
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
